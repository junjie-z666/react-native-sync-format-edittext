package com.syncformatedittext

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.facebook.react.views.textinput.ReactEditText

class SyncFormatEdittextView(context: Context) : ReactEditText(context) {
  private var rawCursorPos = 0
  private var isFormatting = false
  private var lastFormattedText = ""
  private var lastFormattedCursorPos = 0
  private var onFormatListener: ((String, Int) -> Unit)? = null
  var formatModule: FormatModuleImpl? = null

  init {
    addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isFormatting) return
        rawCursorPos = if (count > 0) start + count else start
      }
      override fun afterTextChanged(s: Editable?) {
        if (isFormatting) return
        val currentText = s?.toString() ?: ""

        // Skip if text matches our last formatted result (e.g. from JS value prop update)
        if (currentText == lastFormattedText && selectionStart == lastFormattedCursorPos) {
          onFormatListener?.invoke(currentText, lastFormattedCursorPos)
          return
        }

        val module = formatModule
        val viewTag = id

        if (module != null && viewTag > 0 && module.hasFormat(viewTag)) {
          try {
            val result = module.formatText(viewTag, currentText, rawCursorPos)
            if (result.text == currentText) {
              val pos = result.cursorPos.coerceIn(0, currentText.length)
              if (selectionStart != pos) {
                setSelection(pos)
              }
              lastFormattedText = result.text
              lastFormattedCursorPos = pos
              onFormatListener?.invoke(result.text, pos)
              return
            }
            val newCursorPos = result.cursorPos.coerceIn(0, result.text.length)
            isFormatting = true
            s?.replace(0, s.length, result.text)
            setSelection(newCursorPos)
            rawCursorPos = newCursorPos
            isFormatting = false
            lastFormattedText = result.text
            lastFormattedCursorPos = newCursorPos
            onFormatListener?.invoke(result.text, newCursorPos)
          } catch (e: Exception) {
            onFormatListener?.invoke(currentText, selectionEnd.coerceAtLeast(0))
          }
        }
      }
    })
  }

  fun setOnFormatListener(listener: (String, Int) -> Unit) {
    onFormatListener = listener
  }
}
