package com.syncformatedittext

import android.util.Log
import com.facebook.proguard.annotations.DoNotStrip
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod

class FormatModule(reactContext: ReactApplicationContext) :
    NativeFormatModuleSpec(reactContext) {

    @ReactMethod
    @DoNotStrip
    override fun install(promise: Promise) {
        try {
            FormatModuleImpl.instance?.install()
            promise.resolve(null)
        } catch (e: Exception) {
            Log.w("FormatModule", "install failed: ${e.message}")
            promise.reject("INSTALL_FAILED", e.message, e)
        }
    }

    companion object {
        const val NAME = "FormatModule"
    }
}
