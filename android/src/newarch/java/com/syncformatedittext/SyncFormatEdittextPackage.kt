package com.syncformatedittext

import com.facebook.react.BaseReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.facebook.react.uimanager.ViewManager

class SyncFormatEdittextViewPackage : BaseReactPackage() {
  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return listOf(SyncFormatEdittextViewManager())
  }

  override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
    return when (name) {
      FormatModule.NAME -> {
        FormatModuleImpl.instance = FormatModuleImpl(reactContext)
        FormatModule(reactContext)
      }
      else -> null
    }
  }

  override fun getReactModuleInfoProvider() = ReactModuleInfoProvider {
    mapOf(
      FormatModule.NAME to ReactModuleInfo(
        FormatModule.NAME,
        FormatModule.NAME,
        false,  // canOverrideExistingModule
        true,   // needsEagerInit
        false,  // isCxxModule
        true    // isTurboModule
      )
    )
  }
}
