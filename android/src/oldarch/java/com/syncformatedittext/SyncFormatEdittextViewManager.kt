package com.syncformatedittext

import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.annotations.ReactProp

@ReactModule(name = SyncFormatEdittextViewManager.NAME)
class SyncFormatEdittextViewManager : SimpleViewManager<SyncFormatEdittextView>() {

  override fun getName(): String {
    return NAME
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
    return MapBuilder.of<String, Any>(
      "topSyncFormatChange",
      MapBuilder.of<String, String>("registrationName", "onSyncFormatChange")
    )
  }

  public override fun createViewInstance(context: ThemedReactContext): SyncFormatEdittextView {
    val view = SyncFormatEdittextView(context)
    view.setOnChangeListener { text, cursorPos ->
      val dispatcher = context.getNativeModule(UIManagerModule::class.java)!!.eventDispatcher
      dispatcher.dispatchEvent(
        SyncFormatEditTextChangeEvent(
          view.id,
          text,
          cursorPos
        )
      )
    }
    return view
  }

  @ReactProp(name = "value")
  fun setValue(view: SyncFormatEdittextView?, value: String?) {
    SyncFormatEdittextHelper.setValue(view, value)
  }

  @ReactProp(name = "placeholder")
  fun setPlaceholder(view: SyncFormatEdittextView?, placeholder: String?) {
    SyncFormatEdittextHelper.setPlaceholder(view, placeholder)
  }

  companion object {
    const val NAME = "SyncFormatEdittextView"
  }
}
