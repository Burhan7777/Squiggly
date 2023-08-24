package com.pzbdownloaders.scribble.common.domain.utils

sealed class GetResult(val exception: String?) {
    object Success : GetResult(null)
    class Failure(exception: String?) : GetResult(exception)
}
