# Issue 4: 输入回退防闪烁 ✅

## What to build

在 TextWatcher 的 afterTextChanged 中，检测到用户输入后立刻用 `Editable.replace()` 回退到 `lastFormattedText`（上次格式化结果），防止未格式化文本闪现。同时发 onChange 事件到 JS。JS 格式化后通过 value prop 回写新值。

## Acceptance criteria

- [x] 用户输入后，EditText 不显示未格式化的原始文本
- [x] 用户输入后，EditText 显示上次格式化结果（或空字符串首次输入时）
- [x] JS 格式化后 value 回写，EditText 更新为格式化后的文本
- [x] `Editable.replace()` 不会递归触发 TextWatcher（isReverted flag 防护）
- [x] 快速连续输入时，回退逻辑正确，不会丢失输入

## Blocked by

- Issue 3
