import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)

    //plugin para serilazation
    kotlin("plugin.serialization") version("1.9.20")

    //plugin para gerar build config
    id("com.github.gmazzo.buildconfig") version "5.4.0"


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

          }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        androidMain.dependencies {
            implementation(libs.viewModel.ktx)
            implementation(libs.ktor.client.android)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
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
