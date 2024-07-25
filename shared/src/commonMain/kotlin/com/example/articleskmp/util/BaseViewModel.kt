package com.example.articleskmp.util

import kotlinx.coroutines.CoroutineScope


//open e para permitir heranca assim consigo adicoinar metodos e alteralos
//sem open se tornaria final
expect  open class BaseViewModel() {

    val scope: CoroutineScope

}