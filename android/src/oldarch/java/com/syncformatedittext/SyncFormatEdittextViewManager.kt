package com.syncformatedittext

import android.text.SpannableString
import com.facebook.react.common.MapBuilder
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.views.text.ReactTextUpdate
import com.facebook.react.views.textinput.ReactEditText
import com.facebook.react.views.textinput.ReactTextInputManager

@ReactModule(name = SyncFormatEdittextViewManager.NAME)
class SyncFormatEdittextViewManager : ReactTextInputManager() {

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): SyncFormatEdittextView {
    val view = SyncFormatEdittextView(context)
    view.formatModule = FormatModuleImpl.instance
    return view
  }

  override fun addEventEmitters(context: ThemedReactContext, view: ReactEditText) {
    super.addEventEmitters(context, view)
    val editText = view as SyncFormatEdittextView
    editText.setOnFormatListener { text, cursorPos ->
      val dispatcher = context.getNativeModule(UIManagerModule::class.java)!!.eventDispatcher
      dispatcher.dispatchEvent(
        SyncFormatEditTextChangeEvent(
          editText.id,
          text,
          cursorPos
        )
      )
    }
  }

  @ReactProp(name = "value")
  fun setValue(view: ReactEditText, value: String?) {
    val text = value ?: ""
    val currentText = view.text.toString()
    if (currentText == text) return
    val spannable = SpannableString(text)
    val eventCount = view.incrementAndGetEventCounter()
    view.maybeSetTextFromJS(
      ReactTextUpdate(spannable, eventCount, false, 0, 0, 0)
    )
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> {
    val constants = super.getExportedCustomDirectEventTypeConstants().toMutableMap()
    constants["topSyncFormatChange"] = MapBuilder.of("registrationName", "onSyncFormatChange")
    return constants
  }

  companion object {
    const val NAME = "SyncFormatEdittextView"
  }
}
