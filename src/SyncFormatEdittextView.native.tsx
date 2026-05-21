import { useCallback, useEffect, useRef } from 'react';
import {
  findNodeHandle,
  TurboModuleRegistry,
  type NativeSyntheticEvent,
  type TextInputProps,
} from 'react-native';
import SyncFormatEdittextViewNativeComponent from './SyncFormatEdittextViewNativeComponent';

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

// Install JSI bindings synchronously via @ReactMethod(isBlockingSynchronousMethod=true)
try {
  const fmtModule = TurboModuleRegistry.getEnforcing('FormatModule') as unknown as { install(): void };
  fmtModule.install();
} catch {}

const formatModule = (globalThis as any).__formatModule as {
  setFormat(viewTag: number, fn: FormatFn): void;
  removeFormat(viewTag: number): void;
} | undefined;

export function SyncFormatEdittextView({
  format,
  onSyncFormatChange,
  onChange,
  onChangeText,
  ...rest
}: SyncFormatEditTextProps) {
  const viewRef = useRef(null);

  // Register format function with native via JSI
  useEffect(() => {
    if (!format || !formatModule) return;
    const tag = findNodeHandle(viewRef.current);
    if (tag) {
      formatModule.setFormat(tag, format);
    }
    return () => {
      if (tag) {
        formatModule.removeFormat(tag);
      }
    };
  }, [format]);

  const handleSyncFormatChange = useCallback(
    (event: SyncFormatChangeEvent) => {
      const { text, cursorPos } = event.nativeEvent;
      onSyncFormatChange?.(text, cursorPos);
    },
    [onSyncFormatChange]
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
