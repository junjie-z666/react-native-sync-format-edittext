import type { CodegenTypes } from 'react-native';
import { codegenNativeComponent, type ViewProps } from 'react-native';

interface NativeProps extends ViewProps {
  value?: string;
  placeholder?: string;
  onChange?: CodegenTypes.DirectEventHandler<{
    text: string;
    cursorPos: CodegenTypes.Double;
  }>;
}

export default codegenNativeComponent<NativeProps>('SyncFormatEdittextView');
