package com.example.homebankfront.feature.utility

import android.util.Log
import com.example.homebankfront.BuildConfig

val Any.className: String get() = this::class.java.simpleName

fun Any.logDebug(message: String) {
    if (BuildConfig.DEBUG) Log.d(className, message)
}

fun Any.logError(message: String) = Log.e(className, message)

class Logger {
    companion object {
        fun d(
            tag: String = getCallerClassName(),
            message: String
        ) {
            if (BuildConfig.DEBUG) Log.d(tag, message)
        }

        fun e(tag: String = getCallerClassName(), message: String) = Log.e(tag, message)


        /**
         * Returns the name of the class that called the logging function.
         */
        private fun getCallerClassName(): String {
            return Throwable().stackTrace[2].className
        }
    }
}

//class Logger {
//    companion object {
//        inline fun <reified T> d(message: String) {
//            if (BuildConfig.DEBUG) {
//                Log.d(T::class.java.simpleName, message)
//            }
//        }
//
//        inline fun <reified T> e(message: String) = Log.e(T::class.java.simpleName, message)
//    }
//}
