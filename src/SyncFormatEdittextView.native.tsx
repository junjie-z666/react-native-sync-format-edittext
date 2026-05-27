import { useCallback, useEffect, useRef } from 'react';
import {
  findNodeHandle,
  type NativeSyntheticEvent,
  type TextInputProps,
} from 'react-native';
import SyncFormatEdittextViewNativeComponent from './SyncFormatEdittextViewNativeComponent';
import NativeFormatModule from './NativeFormatModule';

type FormatFn = (
  text: string,
  cursorPos: number
) => { text: string; cursorPos: number };

type SyncFormatChangeEvent = NativeSyntheticEvent<{
  text: string;
  cursorPos: number;
}>;

export type SyncFormatEditTextProps = TextInputProps & {
  format?: FormatFn;
  onSyncFormatChange?: (text: string, cursorPos: number) => void;
};

// Install JSI bindings on first use
const installPromise: Promise<void> = (() => {
  try {
    return NativeFormatModule.install();
  } catch (e) {
    console.warn('FormatModule install failed:', e);
    return Promise.reject(e);
  }
})();

export function SyncFormatEdittextView({
  format,
  onSyncFormatChange,
  onChange,
  onChangeText,
  ...rest
}: SyncFormatEditTextProps) {
  const viewRef = useRef(null);

  useEffect(() => {
    if (!format || !viewRef.current) return;

    let mounted = true;
    const currentRef = viewRef.current;

    const formatModule = (globalThis as any).__formatModule as
      | {
          setFormat(viewTag: number, fn: FormatFn): void;
          removeFormat(viewTag: number): void;
        }
      | undefined;
    const tag = findNodeHandle(viewRef.current);
    // TODO: 2026/5/27 installPromise执行过一次后就设置为null，后续判断是null就直接注册，不用then，可能涉及测试热更新和reload是否有问题。
    installPromise.then(() => {
      if (!mounted || !viewRef.current) return;
      if (!formatModule) return;

      if (tag) {
        formatModule.setFormat(tag, format);
      }
    });

    return () => {
      mounted = false;
      if (!formatModule || !currentRef) return;
      if (tag) {
        formatModule.removeFormat(tag);
      }
    };
  }, [viewRef.current, format]);

  const handleSyncFormatChange = useCallback(
    (event: SyncFormatChangeEvent) => {
      const { text, cursorPos } = event.nativeEvent;
      onSyncFormatChange?.(text, cursorPos);
      onChangeText?.(text);
    },
    [onSyncFormatChange, onChangeText]
  );

  return (
    <SyncFormatEdittextViewNativeComponent
      ref={viewRef}
      {...(rest as any)}
      onChange={onChange as any}
      onChangeText={onChangeText as any}
      onSyncFormatChange={handleSyncFormatChange}
    />
  );
}
