# Issue 3: onChange 事件

## What to build

为 SyncFormatEditextView 添加 TextWatcher，在用户输入时通过 EventDispatcher 发送 onChange 事件到 JS。事件携带 `{ text: string, cursorPos: number }`。在 codegen spec 中声明 DirectEventHandler。

## Acceptance criteria

- [ ] 用户输入字符时，JS 侧 onChange 回调被调用
- [ ] onChange 事件携带正确的 text（用户输入后的原始文本）和 cursorPos
- [ ] 快速连续输入时，每个字符都触发独立的 onChange 事件
- [ ] value prop 更新（来自 JS 回写）不触发 onChange 事件

## Blocked by

- Issue 2
