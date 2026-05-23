## What to build

更新导出类型和 example app，验证全部 props 和事件正常工作。

具体变更：
- 更新 `src/index.tsx` 导出类型包含 `TextInputProps & { format?, onSyncFormatChange? }`
- 更新 example app 使用 TextInputProps：
  - 手机号输入：添加 `keyboardType="phone-pad"`
  - 字母输入：添加 `autoCapitalize="characters"`、`maxLength={10}`
  - 新增一个密码输入示例：`secureTextEntry={true}`
- 验证全部 props 生效、回调正常、受控/非受控模式正常

## Acceptance criteria

- [ ] 导出类型包含 TextInputProps + format + onSyncFormatChange
- [ ] example app 中 keyboardType, maxLength, secureTextEntry, placeholder 等 props 生效
- [ ] onChange, onChangeText 返回格式化后文本
- [ ] onSyncFormatChange 回调正常
- [ ] 受控 value 模式正常
- [ ] 非受控模式正常（不传 value，等同标准 TextInput）
- [ ] 删除、光标移动、粘贴不卡顿

## Blocked by

- Issue 011 (ViewManager 合并继承)
- Issue 012 (JS 封装层)
