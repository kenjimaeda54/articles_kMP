import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)

    //plugin para serilazation
    kotlin("plugin.serialization") version("1.9.20")

    //plugin para gerar build config
    id("com.github.gmazzo.buildconfig") version "5.4.0"

    //plugin para gerar o viewModel para IOS
    id("co.touchlab.skie") version "0.8.3"

    //plugin sqldelight
    alias(libs.plugins.sqldelight)
 }




kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    //https://stackoverflow.com/questions/74743976/kotlin-multiplatform-accessing-build-variables-in-code
    //kotlin multiplataform precisa de plugin

    //local onde esta o arquiivo local.properties
    //https://stackoverflow.com/questions/20673378/where-does-local-properties-go-for-android-project
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").inputStream())
    val apiKey = properties.getProperty("API_KEY")
    buildConfig {
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
    }


    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(libs.coroutines.ktx)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.sql.coroutines.extensions)
          }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sql.native.driver)
        }

        androidMain.dependencies {
            implementation(libs.viewModel.ktx)
            implementation(libs.ktor.client.android)
            implementation(libs.koin.android)
            implementation(libs.sql.android.driver)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.sql.native.driver)

        }
    }
}

android {
    namespace = "com.example.articleskmp"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
dependencies {
    implementation(libs.androidx.lifecycle.viewmodel.android)
}


sqldelight {
    databases {
        create("ArticlesDB") {
            packageName = "com.example.articleskmp.db"
         }
     }
}