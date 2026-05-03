# Issue 6: placeholder + color props ✅

## What to build

为 SyncFormatEditextView 添加 `placeholder` 和 `color` @ReactProp。placeholder 显示为 EditText 的 hint，color 设置背景色。在 JS codegen spec 中声明对应 props。

## Acceptance criteria

- [x] `placeholder="请输入电话号码"` 在 EditText 中显示为 hint 文本
- [x] `color="#ff0000"` 设置 EditText 背景为红色
- [x] placeholder 和 color 可以同时使用
- [x] 不传时使用默认值（无 hint，透明背景）

## Blocked by

- Issue 1
