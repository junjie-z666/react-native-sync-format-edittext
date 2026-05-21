import { View } from 'react-native';
import type { SyncFormatEditTextProps } from './SyncFormatEdittextView.native';

export function SyncFormatEdittextView({ format, onSyncFormatChange, ...rest }: SyncFormatEditTextProps) {
  return <View {...(rest as any)} />;
}
