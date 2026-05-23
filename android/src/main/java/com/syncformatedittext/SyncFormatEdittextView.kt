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
  private var rawText = ""
  private var onChangeListener: ((String, Int) -> Unit)? = null
  var significantCharPredicate: (Char) -> Boolean = { it.isDigit() }

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
        isReverting = true
        val editable = text
        if (editable != null) {
          editable.replace(0, editable.length, lastFormattedText)
        }
        rawText = currentText
        onChangeListener?.invoke(currentText, rawCursorPos)
        isReverting = false
      }
    })
  }

  fun setOnChangeListener(listener: (String, Int) -> Unit) {
    onChangeListener = listener
  }

  fun setFormattedText(text: String, cursorPos: Int) {
    val savedRawText = rawText
    val savedRawCursorPos = rawCursorPos
    isReverting = true
    setText(text)
    val safeCursorPos = cursorPos.coerceIn(0, text.length)
    setSelection(safeCursorPos)
    lastFormattedText = text
    rawText = savedRawText
    rawCursorPos = savedRawCursorPos
    isReverting = false
  }

  fun computeFormattedCursorPos(formattedText: String): Int {
    return computeFormattedCursorPos(rawText, rawCursorPos, formattedText)
  }

  private fun computeFormattedCursorPos(rawText: String, cursorPos: Int, formattedText: String): Int {
    val predicate: (Char) -> Boolean = if (formattedText.any(significantCharPredicate)) {
      significantCharPredicate
    } else {
      { it.isLetter() }
    }
    val charCount = rawText.substring(0, cursorPos.coerceAtMost(rawText.length))
      .count(predicate)
    if (charCount == 0) return 0
    var charsSeen = 0
    for (i in formattedText.indices) {
      if (predicate(formattedText[i])) {
        charsSeen++
        if (charsSeen == charCount) return i + 1
      }
    }
    return formattedText.length
  }
}
