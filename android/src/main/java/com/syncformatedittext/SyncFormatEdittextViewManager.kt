package com.syncformatedittext

import android.text.Editable
import android.text.Selection
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.Gravity
import com.facebook.react.module.annotations.ReactModule
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.UIManagerHelper
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.views.text.ReactTextUpdate
import com.facebook.react.views.textinput.ReactEditText
import com.facebook.react.views.textinput.ReactTextInputManager

@ReactModule(name = SyncFormatEdittextViewManager.NAME)
class SyncFormatEdittextViewManager : ReactTextInputManager() {

  override fun getName(): String = NAME

  override fun createViewInstance(context: ThemedReactContext): SyncFormatEdittextView {
    return SyncFormatEdittextView(context)
  }

  override fun addEventEmitters(reactContext: ThemedReactContext, editText: ReactEditText) {
    super.addEventEmitters(reactContext, editText)
    val view = editText as SyncFormatEdittextView
    view.addTextChangedListener(view.FormatTextWatcher())
    view.setOnSyncFormatChangeListener { text, cursorPos ->
      val dispatcher = UIManagerHelper.getEventDispatcherForReactTag(reactContext, view.id)
      dispatcher?.dispatchEvent(
        SyncFormatEditTextChangeEvent(
          UIManagerHelper.getSurfaceId(view),
          view.id,
          text,
          cursorPos
        )
      )
    }
  }

  @ReactProp(name = "value")
  fun setValue(view: SyncFormatEdittextView?, value: String?) {
    view?.setValueFromJS(value ?: "")
  }

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Any> {
    return mapOf(
      "topSyncFormatChange" to mapOf("registrationName" to "onSyncFormatChange")
    )
  }

  companion object {
    const val NAME = "SyncFormatEdittextView"
  }
}
