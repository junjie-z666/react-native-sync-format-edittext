package com.syncformatedittext

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class SyncFormatEdittextViewPackage : ReactPackage {
  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return listOf(SyncFormatEdittextViewManager())
  }

  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    val module = FormatModule(reactContext)
    FormatModule.instance = module
    return listOf(module)
  }
}
