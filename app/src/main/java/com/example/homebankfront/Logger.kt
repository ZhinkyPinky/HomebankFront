package com.example.homebankfront

import android.util.Log
import com.example.homebankfront.BuildConfig

class Logger {
    companion object {
        fun d(
            tag: String = getCallerClassName(),
            message: String
        ) {
            if (BuildConfig.DEBUG) {
                Log.d(tag, message)
            }
        }

        fun e(tag: String = getCallerClassName(), message: String) {
            Log.e(tag, message)
        }

        /**
         * Returns the name of the class that called the logging function.
         */
        private fun getCallerClassName(): String {
            return Throwable().stackTrace[2].className
        }
    }
}
