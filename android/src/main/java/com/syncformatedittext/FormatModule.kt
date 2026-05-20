package com.syncformatedittext

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.turbomodule.core.interfaces.CallInvokerHolder

data class FormatResult(val text: String, val cursorPos: Int)

@ReactModule(name = FormatModule.NAME)
class FormatModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = NAME

    @ReactMethod(isBlockingSynchronousMethod = true)
    fun install() {
        if (!nativeLibLoaded) return
        val context = reactApplicationContext
        val runtimeRef = context.javaScriptContextHolder!!.get()
        val callInvokerHolder = context.jsCallInvokerHolder!!
        nativeInstall(runtimeRef, callInvokerHolder)
    }

    fun formatText(viewTag: Int, text: String, cursorPos: Int): FormatResult {
        if (!nativeLibLoaded) return FormatResult(text, cursorPos)
        val json = nativeFormatText(viewTag, text, cursorPos)
        return parseFormatResult(json, text, cursorPos)
    }

    fun removeFormat(viewTag: Int) {
        if (!nativeLibLoaded) return
        nativeRemoveFormat(viewTag)
    }

    companion object {
        const val NAME = "FormatModule"

        @JvmStatic
        var instance: FormatModule? = null

        private var nativeLibLoaded = false

        init {
            try {
                System.loadLibrary("syncformatedittext")
                nativeLibLoaded = true
            } catch (_: UnsatisfiedLinkError) {
            }
        }

        @JvmStatic
        external fun nativeInstall(runtimeRef: Long, callInvokerHolder: CallInvokerHolder)

        @JvmStatic
        external fun nativeFormatText(viewTag: Int, text: String, cursorPos: Int): String

        @JvmStatic
        external fun nativeRemoveFormat(viewTag: Int)

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
}
