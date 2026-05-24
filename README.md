# react-native-sync-format-edittext

React Native 同步格式化输入框 — 无闪烁的实时文本格式化

## 功能介绍

在 React Native 中对 TextInput 做实时格式化（如手机号 `138-0013-8000`），通常需要监听 `onChangeText`，在 JS 层格式化后再通过 `value` 回写。这个过程中文本要经过异步桥接往返，导致输入时出现明显的闪烁和光标跳动。

本库通过 **JSI 同步调用**，让原生层在文本变化时直接同步调用 JS 的格式化函数，格式化结果立即生效，彻底消除闪烁。同时继承自原生 TextInput，支持所有原生 props。

### 平台支持

| 平台 | 状态 |
|---|---|
| Android | ✅ 已支持 |
| iOS | 🚧 开发中 |

## 演示效果

**原生 TextInput 格式化 — 闪烁明显**

![原生格式化](./docs/native-format.gif)

**SyncFormatEditText — 无闪烁**

![同步格式化](./docs/sync-format.gif)

## 安装

```sh
npm install react-native-sync-format-edittext
```

支持新架构和旧架构，autolink 自动完成链接。

## 使用

```tsx
import { SyncFormatEdittextView } from 'react-native-sync-format-edittext';

// 只允许输入数字
function formatDigits(text: string, cursorPos: number) {
  const digits = text.replace(/\D/g, '');
  return {
    text: digits,
    cursorPos: Math.min(cursorPos, digits.length),
  };
}

<SyncFormatEdittextView
  value={code}
  format={formatDigits}
  onChangeText={setCode}
  placeholder="请输入验证码"
  style={styles.input}
/>
```

## API

### Props

继承所有 `TextInputProps`，额外支持以下 props：

| Prop | 类型 | 说明 |
|---|---|---|
| `format` | `(text: string, cursorPos: number) => { text: string; cursorPos: number }` | 格式化函数，接收当前文本和光标位置，返回格式化后的文本和调整后的光标位置 |
| `onSyncFormatChange` | `(text: string, cursorPos: number) => void` | 格式化完成后的回调，返回格式化后的文本和光标位置 |

### `format` 函数详解

`cursorPos` 是字符在字符串中的索引（从 0 开始）。格式化后文本长度可能改变，光标位置需要相应调整，否则会跳到错误位置。

**原理**：`cursorPos` 表示"光标在第几个字符前面"。格式化后，你需要计算光标在格式化文本中的对应位置。如果格式化只是过滤字符（文本变短），光标位置通常不变或取 min；如果格式化插入了分隔符（文本变长），光标需要加上它前方新增的字符数。

**示例 1：只过滤，不插入字符**

输入 `a1b2`，光标在末尾（cursorPos=4）。过滤非数字后得到 `12`，光标仍在末尾，即 cursorPos=2。

```ts
function formatDigits(text: string, cursorPos: number) {
  const digits = text.replace(/\D/g, '');
  return {
    text: digits,
    cursorPos: Math.min(cursorPos, digits.length),
  };
}
```

过滤不改变字符顺序，光标前被过滤掉的字符数 = 原光标位置 - 格式化后光标前字符数，所以直接用 `Math.min(cursorPos, digits.length)` 即可。

**示例 2：插入分隔符**

输入 `1380013`，光标在末尾（cursorPos=7）。格式化为 `138-0013`，光标前多了一个 `-`，所以 cursorPos=8。

```ts
function formatPhone(text: string, cursorPos: number) {
  const digits = text.replace(/\D/g, '').slice(0, 11);
  let formatted = '';
  let newCursorPos = cursorPos;
  if (digits.length <= 3) {
    formatted = digits;
  } else if (digits.length <= 7) {
    formatted = `${digits.slice(0, 3)}-${digits.slice(3)}`;
    if (cursorPos > 3) newCursorPos = cursorPos + 1; // 光标前多了一个 '-'
  } else {
    formatted = `${digits.slice(0, 3)}-${digits.slice(3, 7)}-${digits.slice(7)}`;
    if (cursorPos > 3) newCursorPos = cursorPos + 1;  // 第一个 '-'
    if (cursorPos > 7) newCursorPos = cursorPos + 2;  // 两个 '-'
  }
  return {
    text: formatted,
    cursorPos: Math.min(newCursorPos, formatted.length),
  };
}
```

关键逻辑：光标每跨过一个分隔符位置，`cursorPos` 就 +1。`138-0013-8000` 中分隔符在索引 3 和 8，所以光标在 3 之后时 +1，在 7 之后时 +2。

更多示例参见 [example](./example/src/App.tsx)。

## License

MIT
