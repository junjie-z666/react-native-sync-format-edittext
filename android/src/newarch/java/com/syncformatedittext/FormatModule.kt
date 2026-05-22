package com.syncformatedittext

import com.facebook.proguard.annotations.DoNotStrip
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod

class FormatModule(reactContext: ReactApplicationContext) :
    NativeFormatModuleSpec(reactContext) {

    @ReactMethod(isBlockingSynchronousMethod = true)
    @DoNotStrip
    override fun install() {
        FormatModuleImpl.instance?.install()
    }

    companion object {
        const val NAME = "FormatModule"
    }
}
