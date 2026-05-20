# Issue 拆分：Android 老架构兼容

## Issue 1：Gradle source set 分离 + 代码迁移 ✅

**类型**: AFK
**Blocked by**: None

### What to build

在 `android/` 下创建 `src/newarch/` 和 `src/oldarch/` 目录。将现有 `SyncFormatEdittextViewManager.kt`、`SyncFormatEditTextChangeEvent.kt`、`SyncFormatEdittextViewPackage.kt` 从 `src/main/` 移到 `src/newarch/`。修改 `build.gradle` 添加 source set 切换逻辑，根据 `newArchEnabled` 属性选择 newarch 或 oldarch 目录。

### Acceptance criteria

- [x] `src/newarch/` 包含 ViewManager、Event、Package 三个文件
- [x] `src/main/` 只保留 `SyncFormatEdittextView.kt`
- [x] `build.gradle` 根据 `newArchEnabled` 切换 source set
- [ ] 新架构编译通过，功能不变

---

## Issue 2：oldarch Event + Package ✅

**类型**: AFK
**Blocked by**: Issue 1

### What to build

在 `src/oldarch/` 创建老架构版本的 `SyncFormatEditTextChangeEvent.kt` 和 `SyncFormatEdittextViewPackage.kt`。Event 使用 `Event(viewTag)` 构造函数 + `getUniqueID()`。Package 实现标准 `ReactPackage` 接口。

### Acceptance criteria

- [x] Event 类使用老架构 `Event` 基类构造函数
- [x] Package 实现 `ReactPackage.createViewManagers()`
- [ ] 编译通过（此时 ViewManager 还缺，用空列表临时占位）

---

## Issue 3：oldarch ViewManager ✅

**类型**: AFK
**Blocked by**: Issue 2

### What to build

在 `src/oldarch/` 创建老架构版本的 `SyncFormatEdittextViewManager.kt`。去掉 codegen delegate/interface，手动 `@ReactProp` 注解。事件派发使用 `UIManagerModule.eventDispatcher.dispatchEvent()`。

### Acceptance criteria

- [x] 不依赖 codegen 生成的接口/委托
- [x] `@ReactProp` 手动处理 value、placeholder
- [x] 事件通过 `UIManagerModule` 的 `eventDispatcher` 派发
- [ ] `newArchEnabled=false` 时编译通过

---

## Issue 4：example app 老架构测试配置 ✅

**类型**: AFK
**Blocked by**: Issue 3

### What to build

在 `example/android/gradle.properties` 添加 `newArchEnabled=false` 配置。确保 example app 可以在老架构模式下编译运行，SyncFormatEditText 功能正常。

### Acceptance criteria

- [x] `gradle.properties` 包含 `newArchEnabled` 配置及切换说明
- [ ] `yarn example android` 老架构编译通过
- [ ] example app 中输入格式化功能正常工作

---

## 依赖关系

```
Issue 1 → Issue 2 → Issue 3 → Issue 4
```

## 总结

| Issue | 类型 | 内容 |
|-------|------|------|
| 1 | AFK | 目录结构 + build.gradle 切换 |
| 2 | AFK | oldarch Event + Package |
| 3 | AFK | oldarch ViewManager |
| 4 | AFK | example app 测试配置 |
