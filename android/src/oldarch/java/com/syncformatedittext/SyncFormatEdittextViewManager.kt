package com.syncformatedittext

import android.util.Log
import com.facebook.react.common.MapBuilder
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.annotations.ReactProp

@ReactModule(name = SyncFormatEdittextViewManager.NAME)
class SyncFormatEdittextViewManager : SimpleViewManager<SyncFormatEdittextView>() {

  init {
    Log.d("SyncFormatEdittext", "Using OLD architecture ViewManager")
  }

  override fun getName(): String {
    return NAME
  }

  public override fun createViewInstance(context: ThemedReactContext): SyncFormatEdittextView {
    val view = SyncFormatEdittextView(context)
    view.formatModule = FormatModule.instance
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
    view?.setFormattedText(value ?: "", (value ?: "").length)
  }

  @ReactProp(name = "placeholder")
  fun setPlaceholder(view: SyncFormatEdittextView?, placeholder: String?) {
    view?.hint = placeholder
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any>? {
    return MapBuilder.of(
      "topSyncFormatChange",
      MapBuilder.of("registrationName", "onSyncFormatChange")
    )
  }

  companion object {
    const val NAME = "SyncFormatEdittextView"
  }
}
