package com.dantol.overseastoolkit.utils

import android.util.Log
import com.bosphere.filelogger.FL

class Logger {
    companion object {
        private const val PACKAGE_NAME = "com.dantol.overseastoolkit"
        private val loggerClassName = Logger::class.java.simpleName

        fun i(msg: String) {
            val backstackEntry = getCorrectBackstackEntry()
            Log.i(tag(backstackEntry), msg)
            FL.i(tag(backstackEntry), msg)
        }

        fun w(msg: String) {
            val backstackEntry = getCorrectBackstackEntry()
            Log.w(tag(backstackEntry), msg)
            FL.w(tag(backstackEntry), msg)
        }

        fun e(msg: String) {
            val backstackEntry = getCorrectBackstackEntry()
            Log.e(tag(backstackEntry), msg)
            FL.e(tag(backstackEntry), msg)
        }

        fun d(msg: String) {
            val backstackEntry = getCorrectBackstackEntry()
            Log.d(tag(backstackEntry), msg)
            FL.d(tag(backstackEntry), msg)
        }

        private fun getCorrectBackstackEntry(): StackTraceElement =
            Thread.currentThread().stackTrace.first { e ->
                e.className.contains(PACKAGE_NAME) && !e.className.contains(loggerClassName)
            }

        private fun tag(backstackEntry: StackTraceElement): String =
            "${backstackEntry.className.simpleClassName()}#${backstackEntry.methodName}"

        private fun String.simpleClassName(): String = this.split('.').last()
    }
}