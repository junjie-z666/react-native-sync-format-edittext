# Issue 2: value 受控 prop ✅

## What to build

为 SyncFormatEditextView 添加 `value` @ReactProp。JS 侧通过 value prop 控制 EditText 显示的文本。原生侧实现 `setFormattedText(text, cursorPos)` 方法，用 `isReverting` flag 防止 setText 触发 TextWatcher 递归。

## Acceptance criteria

- [x] JS 侧传 `value="hello"` 时，EditText 显示 "hello"
- [x] JS 侧更新 value prop 时，EditText 文本同步更新
- [x] setText 不会触发 TextWatcher 递归（isReverted flag 正确工作）
- [x] 光标位置跟随 value 更新

## Blocked by

- Issue 1
