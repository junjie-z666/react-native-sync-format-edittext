package com.syncformatedittext

object SyncFormatEdittextHelper {

  fun setValue(view: SyncFormatEdittextView?, value: String?) {
    if (view != null && value != null) {
      val cursorPos = view.computeFormattedCursorPos(value)
      view.setFormattedText(value, cursorPos)
    } else {
      view?.setFormattedText(value ?: "", (value ?: "").length)
    }
  }

  fun setPlaceholder(view: SyncFormatEdittextView?, placeholder: String?) {
    view?.hint = placeholder
  }
}
