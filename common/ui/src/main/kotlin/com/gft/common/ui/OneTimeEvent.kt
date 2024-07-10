package com.gft.common.ui

class OneTimeEvent<out T>(private val content: T) {

    private var hasBeenHandled = false

    @Synchronized
    fun getContent(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}
