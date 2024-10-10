package com.example.homebankfront.feature.utility

class Event<out T>(private val consumable: T) {
    private var consumed: Boolean = false

    fun consume(): T? {
        return if (consumed) {
            null
        } else {
            consumed = true
            consumable
        }
    }
}