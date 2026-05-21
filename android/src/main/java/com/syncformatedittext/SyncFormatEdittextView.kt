package com.syncformatedittext

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import com.facebook.react.views.textinput.ReactEditText

class SyncFormatEdittextView(context: Context) : ReactEditText(context) {
  private var rawCursorPos = 0
  private var isFormatting = false
  private var onFormatListener: ((String, Int) -> Unit)? = null
  var formatModule: FormatModule? = null

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

        val module = formatModule
        val viewTag = id

        if (module != null && viewTag > 0 && module.hasFormat(viewTag)) {
          try {
            val result = module.formatText(viewTag, currentText, rawCursorPos)
            if (result.text == currentText) {
              onFormatListener?.invoke(result.text, rawCursorPos)
              return
            }
            val newCursorPos = computeFormattedCursorPos(currentText, rawCursorPos, result.text)
            isFormatting = true
            s?.replace(0, s.length, result.text)
            setSelection(newCursorPos.coerceIn(0, result.text.length))
            isFormatting = false
            onFormatListener?.invoke(result.text, newCursorPos)
          } catch (e: Exception) {
            // Format failed — leave text as-is, dispatch raw
            onFormatListener?.invoke(currentText, selectionEnd.coerceAtLeast(0))
          }
        }
      }
    })
  }

  fun setOnFormatListener(listener: (String, Int) -> Unit) {
    onFormatListener = listener
  }

  private fun computeFormattedCursorPos(rawText: String, cursorPos: Int, formattedText: String): Int {
    val digitCount = rawText.substring(0, cursorPos.coerceAtMost(rawText.length))
      .count { it.isDigit() }
    if (digitCount == 0) return 0
    var digitsSeen = 0
    for (i in formattedText.indices) {
      if (formattedText[i].isDigit()) {
        digitsSeen++
        if (digitsSeen == digitCount) return i + 1
      }
    }
    return formattedText.length
  }
}
