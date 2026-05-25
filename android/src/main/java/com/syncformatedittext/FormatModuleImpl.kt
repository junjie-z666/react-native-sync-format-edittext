package com.syncformatedittext

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.turbomodule.core.interfaces.CallInvokerHolder

data class FormatResult(val text: String, val cursorPos: Int)

class FormatModuleImpl(private val reactContext: ReactApplicationContext) {

    companion object {
        private const val TAG = "FormatModule"

        @JvmStatic
        var instance: FormatModuleImpl? = null

        private var nativeLibLoaded = false
        private var _installed = false
        private var _installedRuntimeRef = 0L

        init {
            try {
                System.loadLibrary("syncformatedittext")
                nativeLibLoaded = true
            } catch (e: UnsatisfiedLinkError) {
                Log.e(TAG, "Native lib load failed: ${e.message}")
            }
        }

        @JvmStatic
        external fun nativeInstall(runtimeRef: Long, callInvokerHolder: CallInvokerHolder)

        @JvmStatic
        external fun nativeFormatText(viewTag: Int, text: String, cursorPos: Int): String

        @JvmStatic
        external fun nativeRemoveFormat(viewTag: Int)

        @JvmStatic
        external fun nativeHasFormat(viewTag: Int): Boolean

        private fun parseFormatResult(json: String, fallbackText: String, fallbackCursor: Int): FormatResult {
            return try {
                val textMatch = Regex("\"text\"\\s*:\\s*\"((?:[^\"\\\\]|\\\\.)*)\"").find(json)
                val cursorMatch = Regex("\"cursorPos\"\\s*:\\s*(\\d+)").find(json)
                val parsedText = textMatch?.groupValues?.get(1)
                    ?.replace("\\\"", "\"")
                    ?.replace("\\\\", "\\")
                    ?.replace("\\n", "\n")
                    ?.replace("\\r", "\r")
                    ?.replace("\\t", "\t")
                    ?: fallbackText
                val parsedCursor = cursorMatch?.groupValues?.get(1)?.toIntOrNull() ?: fallbackCursor
                FormatResult(parsedText, parsedCursor)
            } catch (e: Exception) {
                FormatResult(fallbackText, fallbackCursor)
            }
        }
    }

    /**
     * @return true if installed, false if already installed (skip)
     * @throws Exception if install fails
     */
    fun install(): Boolean {
        if (!nativeLibLoaded) throw Exception("Native lib not loaded")

        val runtimeRef = reactContext.javaScriptContextHolder?.get() ?: 0L
        if (runtimeRef == 0L) throw Exception("JSI runtime not available")

        if (_installed && runtimeRef == _installedRuntimeRef) return false

        val callInvokerHolder = reactContext.jsCallInvokerHolder
            ?: throw Exception("CallInvokerHolder not available")
        if (!reactContext.hasActiveReactInstance()) throw Exception("No active React instance")

        nativeInstall(runtimeRef, callInvokerHolder)
        _installed = true
        _installedRuntimeRef = runtimeRef
        return true
    }

    fun formatText(viewTag: Int, text: String, cursorPos: Int): FormatResult {
        if (!nativeLibLoaded) return FormatResult(text, cursorPos)
        val json = nativeFormatText(viewTag, text, cursorPos)
        return parseFormatResult(json, text, cursorPos)
    }

    fun hasFormat(viewTag: Int): Boolean {
        if (!nativeLibLoaded) return false
        return nativeHasFormat(viewTag)
    }

    fun removeFormat(viewTag: Int) {
        if (!nativeLibLoaded) return
        nativeRemoveFormat(viewTag)
    }
}
