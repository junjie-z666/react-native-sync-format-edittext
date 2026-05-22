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
    FormatModuleImpl.instance = FormatModuleImpl(reactContext)
    val module = FormatModule(reactContext)
    return listOf(module)
  }
}
