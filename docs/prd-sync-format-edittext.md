# PRD: SyncFormatEditText

## Problem Statement

React Native 的 TextInput 组件不支持在用户输入时同步调用 JS 函数进行格式化。开发者需要自定义原生 EditText 组件，实现"用户输入 → JS 格式化 → 回写显示"的流程，用于电话号码、银行卡号、身份证号等掩码输入场景。

## Solution

提供一个受控的 `SyncFormatEditText` 组件，用户输入时通过输入回退机制防止未格式化文本闪现，同时异步调用 JS 格式化函数，格式化后通过 value prop 回写显示。

## User Stories

1. As a RN developer, I want to pass a format function as a prop, so that user input is automatically formatted on every keystroke
2. As a RN developer, I want the component to be controlled (value + onChange), so that I can read and manage the formatted text state in JS
3. As a RN developer, I want the unformatted input to never flash on screen, so that the user experience is smooth
4. As a RN developer, I want the cursor position to be correct after formatting, so that the user can continue typing without the cursor jumping
5. As a RN developer, I want to set a placeholder text, so that the user knows what to input
6. As a RN developer, I want to set a background color, so that the component matches my app's design
7. As a RN developer, I want the component to work on Android, so that I can use it in my Android app
8. As a RN developer, I want a web fallback that renders a basic View, so that my app doesn't crash on web
9. As a RN developer, I want the format function to receive both the text and cursor position, so that I can implement cursor-aware formatting (e.g., phone number masks)
10. As a RN developer, I want the format function to return both formatted text and new cursor position, so that the cursor stays in the right place after formatting
11. As a RN developer, I want fast consecutive inputs to be handled correctly, so that rapid typing doesn't produce incorrect results
12. As a RN developer, I want the component to handle the case where the format function returns the same text (no formatting needed), so that I don't need to add special logic
13. As a RN developer, I want the component to support standard View style props, so that I can customize layout and appearance
14. As a user of the app, I want my phone number to be formatted as I type (e.g., "138-1234-5678"), so that I can verify my input is correct
15. As a user of the app, I want my bank card number to be formatted as I type (e.g., "6222 0000 0000 0000"), so that I can easily read and verify the number
16. As a user of the app, I want the cursor to stay in the correct position after formatting, so that I can continue typing without confusion

## Implementation Decisions

### Modules to Build/Modify

**Module 1: SyncFormatEditextView (Kotlin)**
- Extends AppCompatEditText
- Contains TextWatcher with `isReverting` flag and `Editable.replace()` for input reversion
- Stores `lastFormattedText` to support reversion
- Emits onChange events with text and cursorPos
- Exposes `setFormattedText(text, cursorPos)` for value prop updates

**Module 2: SyncFormatEditextViewManager (Kotlin)**
- SimpleViewManager for SyncFormatEditextView
- ReactProps: `value` (String), `placeholder` (String), `color` (Int)
- Registers onChange event via event dispatcher
- Uses SyncFormatEditextViewManagerDelegate (codegen)

**Module 3: SyncFormatEdittextViewNativeComponent (TS)**
- Codegen spec with NativeProps: value, placeholder, color, onChange
- onChange uses DirectEventHandler with { text: string, cursorPos: number }

**Module 4: SyncFormatEdittextView.native.tsx (TS)**
- Controlled component wrapping the native component
- Accepts props: value, format, onChange, placeholder, color, style
- Invokes format function on onChange, passes result to parent onChange
- No state management — purely passes through value and format results

**Module 5: SyncFormatEdittextView.tsx (TS)**
- Web fallback: renders a plain View with backgroundColor

### Key Interfaces

- Format function signature: `(text: string, cursorPos: number) => { text: string, cursorPos: number }`
- Component props: `{ value: string, format: FormatFn, onChange: (text: string, cursorPos: number) => void, placeholder?: string, color?: ColorValue, style?: ViewStyle }`

### Architectural Decisions

1. Format function executes in JS, not native — avoids C++/JSI complexity
2. Input reversion via `Editable.replace()` + `isReverting` flag prevents flicker
3. No TurboModule needed — standard RN controlled component pattern
4. RN FIFO event queue guarantees ordering for fast consecutive inputs

## Testing Decisions

### What makes a good test
- Test external behavior: input → formatted output, cursor position, no flicker
- Do NOT test TextWatcher internals or isReverting flag directly

### Modules to test
- Format function (pure function, easily unit-testable)
- Component integration (input → onChange → format → value → display)

### Prior art
- Standard React Native component tests using @testing-library/react-native

## Out of Scope

- iOS implementation (Android only for now)
- Custom keyboard types
- maxLength prop
- onFocus/onBlur events
- Multiline input
- Selection/selectionColor props
- Undo/redo support

## Further Notes

- The 16-50ms delay between input and formatted display is acceptable for the target use cases (phone, bank card, ID number formatting)
- If true synchronous formatting is needed in the future, a JSI-based approach can be revisited (see ADR 0001 for trade-offs)
