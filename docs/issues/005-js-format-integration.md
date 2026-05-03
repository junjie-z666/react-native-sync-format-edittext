# Issue 5: JS format 函数集成 ✅

## What to build

在 JS 组件层实现 format 函数调用逻辑。组件接受 `format` prop（格式化函数），收到 onChange 事件后调用 format 函数，将结果通过 onChange 回调传给父组件。格式化函数签名：`(text: string, cursorPos: number) => { text: string, cursorPos: number }`。

## Acceptance criteria

- [x] 传入 format 函数后，用户输入自动被格式化
- [x] 电话号码格式化示例：输入 "13812345678" 显示 "138-1234-5678"
- [x] 光标位置正确（跟随格式化后的 cursorPos）
- [x] 不传 format prop 时，组件正常工作（不做格式化）
- [x] format 函数返回的 text 和 cursorPos 都被正确应用

## Blocked by

- Issue 4
