## What to build

将 `SyncFormatEdittextView.kt` 的继承从 `AppCompatEditText` 改为 `ReactEditText`。移除旧的防闪烁机制和光标映射逻辑，添加自定义 TextWatcher 发 `onSyncFormatChange` 事件。

具体变更：
- 继承改为 `ReactEditText(context)`
- 移除 `isReverting`、`lastFormattedText`、`setFormattedText()`、`setOnChangeListener()`、`computeFormattedCursorPos()`、`significantCharPredicate`、`rawCursorPos`、`rawText`
- 添加自定义 TextWatcher：`afterTextChanged` 中获取当前文本和光标位置（`Selection.getSelectionStart()`），通过回调通知 ViewManager 发 `onSyncFormatChange` 事件
- cursorPos 为格式化文本中的光标位置，不做 native 侧映射
- 删除 `SyncFormatEdittextHelper.kt`（功能由 ReactTextInputManager 基类 + 自定义 @ReactProp 替代）

## Acceptance criteria

- [ ] SyncFormatEdittextView 继承 ReactEditText
- [ ] 自定义 TextWatcher 在 afterTextChanged 中通过回调通知文本变化
- [ ] 回调携带 text（当前文本）和 cursorPos（格式化文本中的光标位置）
- [ ] 移除 isReverting / lastFormattedText / setFormattedText / setOnChangeListener / computeFormattedCursorPos / significantCharPredicate
- [ ] SyncFormatEdittextHelper.kt 已删除
- [ ] 编译通过

## Blocked by

None - can start immediately
