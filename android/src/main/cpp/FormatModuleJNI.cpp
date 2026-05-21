#include "FormatHostObject.h"
#include <jni.h>
#include <fbjni/fbjni.h>
#include <jsi/jsi.h>
#include <ReactCommon/CallInvoker.h>
#include <ReactCommon/CallInvokerHolder.h>

namespace facebook::react {

static void installFormatModule(jsi::Runtime& runtime, const std::shared_ptr<CallInvoker>& callInvoker) {
  auto hostObject = std::make_shared<FormatHostObject>(callInvoker);
  FormatHostObject::setInstance(hostObject.get());

  runtime.global().setProperty(
      runtime,
      "__formatModule",
      jsi::Object::createFromHostObject(runtime, std::move(hostObject)));
}

} // namespace facebook::react

extern "C" {

JNIEXPORT void JNICALL
Java_com_syncformatedittext_FormatModule_nativeInstall(
    JNIEnv* env,
    jclass clazz,
    jlong jsiRuntimeRef,
    jobject callInvokerHolder) {
  auto runtime = reinterpret_cast<facebook::jsi::Runtime*>(jsiRuntimeRef);
  auto invoker = facebook::jni::alias_ref<facebook::react::CallInvokerHolder::javaobject>{
      reinterpret_cast<facebook::react::CallInvokerHolder::javaobject>(callInvokerHolder)};
  auto callInvoker = invoker->cthis()->getCallInvoker();
  facebook::react::installFormatModule(*runtime, callInvoker);
}

JNIEXPORT jstring JNICALL
Java_com_syncformatedittext_FormatModule_nativeFormatText(
    JNIEnv* env,
    jclass clazz,
    jint viewTag,
    jstring text,
    jint cursorPos) {
  auto* instance = facebook::react::FormatHostObject::getInstance();
  const char* textStr = env->GetStringUTFChars(text, nullptr);
  std::string textCpp(textStr);
  env->ReleaseStringUTFChars(text, textStr);

  std::string result;
  if (instance) {
    result = instance->formatText(static_cast<int>(viewTag), textCpp, static_cast<int>(cursorPos));
  } else {
    result = "{\"text\":\"" + textCpp + "\",\"cursorPos\":" + std::to_string(cursorPos) + "}";
  }

  return env->NewStringUTF(result.c_str());
}

JNIEXPORT void JNICALL
Java_com_syncformatedittext_FormatModule_nativeRemoveFormat(
    JNIEnv* env,
    jclass clazz,
    jint viewTag) {
  auto* instance = facebook::react::FormatHostObject::getInstance();
  if (instance) {
    instance->removeFormat(static_cast<int>(viewTag));
  }
}

JNIEXPORT jboolean JNICALL
Java_com_syncformatedittext_FormatModule_nativeHasFormat(
    JNIEnv* env,
    jclass clazz,
    jint viewTag) {
  auto* instance = facebook::react::FormatHostObject::getInstance();
  if (!instance) return JNI_FALSE;
  return instance->hasFormat(static_cast<int>(viewTag)) ? JNI_TRUE : JNI_FALSE;
}

} // extern "C"
