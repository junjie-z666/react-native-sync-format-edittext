package com.syncformatedittext

import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.module.annotations.ReactModule

@ReactModule(name = FormatModule.NAME)
class FormatModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = NAME

    @ReactMethod
    fun install(promise: Promise) {
      Log.i("FormatModule", "old arch install: ")
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
