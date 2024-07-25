package com.example.articleskmp.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel

//SwfitUI nao possui viewwModel
actual  open class BaseViewModel {

    actual val scope = CoroutineScope(Dispatchers.IO)

    fun clear() {
        scope.cancel()
    }
}