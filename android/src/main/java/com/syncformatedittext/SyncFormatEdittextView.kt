package com.syncformatedittext

import android.content.Context
import android.text.Editable
import android.text.Selection
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.Gravity
import com.facebook.react.views.text.ReactTextUpdate
import com.facebook.react.views.textinput.ReactEditText

class SyncFormatEdittextView(context: Context) : ReactEditText(context) {
  private var onSyncFormatChangeListener: ((String, Int) -> Unit)? = null

  fun setOnSyncFormatChangeListener(listener: (String, Int) -> Unit) {
    onSyncFormatChangeListener = listener
  }

  fun setValueFromJS(value: String) {
    val eventCount = incrementAndGetEventCounter()
    val spannable = SpannableStringBuilder.valueOf(value)
    maybeSetTextFromJS(
      ReactTextUpdate(
        spannable,
        eventCount,
        false,
        0f,
        0f,
        0f,
        0f,
        Gravity.NO_GRAVITY,
        0,
        0
      )
    )
  }

  inner class FormatTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
      val text = s?.toString() ?: ""
      val cursorPos = Selection.getSelectionStart(s).coerceAtLeast(0)
      onSyncFormatChangeListener?.invoke(text, cursorPos)
    }
  }
}