package com.ingatkoding.blog

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.EmptyCoroutineContext

actual open class BaseViewModel {
    actual val scope: CoroutineScope
        get() = CoroutineScope(EmptyCoroutineContext)

    fun destroy() {
        scope.cancel()
    }
}