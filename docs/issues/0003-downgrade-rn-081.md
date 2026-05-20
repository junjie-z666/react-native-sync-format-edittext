# Issue 拆分：降级 RN 到 0.81.6

## Issue 1：重新生成 example app 骨架 ✅

**类型**: AFK
**Blocked by**: None

### What to build

用 `npx @react-native-community/cli init` 生成 RN 0.81.6 的项目骨架，替换 `example/android/` 和 `example/ios/` 目录。保留 `example/src/`（App.tsx 等业务代码）和 `example/package.json` 中的业务依赖。更新 `example/package.json` 中 react 到 19.1.4、react-native 到 0.81.6、所有 @react-native/* 到 0.81.6。删除 gradle.properties 中的 `edgeToEdgeEnabled`，确保 `newArchEnabled=true`。

### Acceptance criteria

- [x] `example/android/` 使用 RN 0.81.6 模板的 Gradle 配置
- [x] `example/ios/` 使用 RN 0.81.6 模板的 Podfile
- [x] `example/src/App.tsx` 业务代码保留
- [x] `example/package.json` 中 react/react-native/@react-native/* 版本正确
- [x] gradle.properties 无 `edgeToEdgeEnabled`，有 `newArchEnabled=true`

---

## Issue 2：更新根 package.json 依赖版本 ✅

**类型**: AFK
**Blocked by**: Issue 1

### What to build

更新根目录 `package.json` 中的依赖版本：react 降到 19.1.4，react-native 降到 0.81.6，@react-native/babel-preset、@react-native/eslint-config 降到 0.81.6。移除 @react-native/jest-preset（0.81.x 不存在），jest preset 改为 `react-native`。执行 `yarn install` 重新生成 lock 文件。

### Acceptance criteria

- [x] react 版本 19.1.4
- [x] react-native 版本 0.81.6
- [x] @react-native/* 包版本 0.81.6
- [x] yarn.lock 重新生成，无版本冲突

---

## Issue 3：编译验证新架构 ✅

**类型**: AFK
**Blocked by**: Issue 2

### What to build

用 `newArchEnabled=true` 编译 example app，验证新架构功能正常。SyncFormatEditText 输入格式化功能可用。

### Acceptance criteria

- [x] 新架构编译通过
- [ ] example app 安装成功（无连接设备，编译本身通过）
- [ ] 输入格式化功能正常

---

## Issue 4：编译验证老架构 ✅

**类型**: AFK
**Blocked by**: Issue 3

### What to build

将 gradle.properties 中 `newArchEnabled` 改为 `false`，clean 后重新编译，验证老架构功能正常。SyncFormatEditText 输入格式化功能可用。

### Acceptance criteria

- [x] 老架构编译通过
- [ ] example app 安装成功（无连接设备，编译本身通过）
- [ ] 输入格式化功能正常
- [x] 恢复 `newArchEnabled=true`

---

## 依赖关系

```
Issue 1 → Issue 2 → Issue 3 → Issue 4
```

## 总结

| Issue | 类型 | 内容 |
|-------|------|------|
| 1 | AFK | 重新生成 example app 骨架（0.81.6） |
| 2 | AFK | 更新根 package.json 依赖 |
| 3 | AFK | 新架构编译验证 |
| 4 | AFK | 老架构编译验证 |
