package com.syncformatedittext

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class SyncFormatEditextView : AppCompatEditText {
  private var isReverting = false
  private var lastFormattedText = ""
  private var onChangeListener: ((String, Int) -> Unit)? = null

  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  )

  init {
    addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
      override fun afterTextChanged(s: Editable?) {
        if (isReverting) return
        val currentText = s?.toString() ?: ""
        if (currentText == lastFormattedText) return
        isReverting = true
        val editable = text
        if (editable != null) {
          editable.replace(0, editable.length, lastFormattedText)
        }
        val cursorPos = selectionEnd.coerceAtLeast(0)
        onChangeListener?.invoke(currentText, cursorPos)
        isReverting = false
      }
    })
  }

  fun setOnChangeListener(listener: (String, Int) -> Unit) {
    onChangeListener = listener
  }

  fun setFormattedText(text: String, cursorPos: Int) {
    isReverting = true
    setText(text)
    val safeCursorPos = cursorPos.coerceIn(0, text.length)
    setSelection(safeCursorPos)
    lastFormattedText = text
    isReverting = false
  }
}
