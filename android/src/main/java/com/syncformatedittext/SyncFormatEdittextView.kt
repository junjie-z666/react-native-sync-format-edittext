package com.syncformatedittext

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class SyncFormatEdittextView : AppCompatEditText {
  private var isReverting = false
  private var lastFormattedText = ""
  private var rawCursorPos = 0
  private var onChangeListener: ((String, Int) -> Unit)? = null
  var formatModule: FormatModule? = null

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr
  )

  init {
    addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (isReverting) return
        rawCursorPos = if (count > 0) start + count else start
      }
      override fun afterTextChanged(s: Editable?) {
        if (isReverting) return
        val currentText = s?.toString() ?: ""
        if (currentText == lastFormattedText) return

        val module = formatModule
        val viewTag = id

        if (module != null && viewTag > 0 && module.hasFormat(viewTag)) {
          // JSI sync path: call format function synchronously
          try {
            val result = module.formatText(viewTag, currentText, rawCursorPos)
            val newCursorPos = computeFormattedCursorPos(currentText, rawCursorPos, result.text)
            isReverting = true
            s?.replace(0, s.length, result.text)
            setSelection(newCursorPos.coerceIn(0, result.text.length))
            lastFormattedText = result.text
            isReverting = false
            onChangeListener?.invoke(result.text, newCursorPos)
          } catch (e: Exception) {
            // Format failed: revert to last known good text
            isReverting = true
            s?.replace(0, s.length, lastFormattedText)
            setSelection(lastFormattedText.length.coerceAtLeast(0))
            isReverting = false
            onChangeListener?.invoke(currentText, selectionEnd.coerceAtLeast(0))
          }
        } else {
          // Async fallback: revert to last formatted text + dispatch event
          isReverting = true
          s?.replace(0, s.length, lastFormattedText)
          val cursorPos = selectionEnd.coerceAtLeast(0)
          isReverting = false
          onChangeListener?.invoke(currentText, cursorPos)
        }
      }
    })
  }

  fun setOnChangeListener(listener: (String, Int) -> Unit) {
    onChangeListener = listener
  }

  fun setFormattedText(text: String, cursorPos: Int) {
    if (text == lastFormattedText) return
    isReverting = true
    setText(text)
    val safeCursorPos = cursorPos.coerceIn(0, text.length)
    setSelection(safeCursorPos)
    lastFormattedText = text
    isReverting = false
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
