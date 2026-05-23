## What to build

合并 newarch/oldarch ViewManager 到 `src/main/`，继承 `ReactTextInputManager`，实现 codegen delegate 接口。

具体变更：
- 新建 `src/main/java/com/syncformatedittext/SyncFormatEdittextViewManager.kt`，继承 `ReactTextInputManager`
- `getName()` 返回 `"SyncFormatEdittextView"`
- `createViewInstance()` 创建 `SyncFormatEdittextView`，设置 onSyncFormatChange 回调
- 添加 `@ReactProp(name="value")` 调 `view.maybeSetTextFromJS(ReactTextUpdate)`
- 重写 `addEventEmitters()`：调 super（注册 ReactTextInputTextWatcher 等标准 watcher）+ 注册自定义 TextWatcher 发 `onSyncFormatChange` 事件
- 新架构：实现 codegen 生成的 `ViewManagerDelegate` 接口，持有 `mDelegate` 字段，重写 `getDelegate()` 返回
- 事件分发：自定义 TextWatcher 的回调中 dispatch `SyncFormatEditTextChangeEvent`
- 删除 `src/newarch/` 下的 ViewManager 和 ChangeEvent
- 删除 `src/oldarch/` 下的 ViewManager 和 ChangeEvent
- Package 类仍分 newarch/oldarch source set，但引用改为 `src/main/` 的 ViewManager

## Acceptance criteria

- [ ] ViewManager 继承 ReactTextInputManager，合并到 src/main/
- [ ] 全部 TextInput props 自动生效（keyboardType, maxLength, secureTextEntry 等）
- [ ] value prop 通过 maybeSetTextFromJS 处理
- [ ] onSyncFormatChange 事件正常分发
- [ ] newArchEnabled=true 编译通过
- [ ] newArchEnabled=false 编译通过
- [ ] 删除 newarch/oldarch 下的旧 ViewManager 和 ChangeEvent

## Blocked by

- Issue 009 (View 继承 ReactEditText)
- Issue 010 (Codegen spec 全量声明)
