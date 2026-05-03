# Issue 7: Web fallback

## What to build

更新 `src/SyncFormatEdittextView.tsx`（web/non-native fallback），使其接受新的 props（value, format, onChange, placeholder, color）并在 web 平台渲染一个基本的 View 或 input 元素。

## Acceptance criteria

- [ ] Web 平台不崩溃，渲染一个基本元素
- [ ] 接受和 native 组件相同的 props 接口
- [ ] TypeScript 类型导出和 native 组件一致

## Blocked by

None - can start immediately
