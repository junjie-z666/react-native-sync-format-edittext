import type { CodegenTypes } from 'react-native';
import { codegenNativeComponent, type ViewProps } from 'react-native';

interface NativeProps extends ViewProps {
  // Custom event
  onSyncFormatChange?: CodegenTypes.DirectEventHandler<{
    text: string;
    cursorPos: CodegenTypes.Double;
  }>;

  // TextInput standard props
  autoCapitalize?: CodegenTypes.WithDefault<
    'none' | 'sentences' | 'words' | 'characters',
    'none'
  >;
  autoCorrect?: boolean;
  autoFocus?: boolean;
  allowFontScaling?: boolean;
  maxFontSizeMultiplier?: CodegenTypes.Float;
  editable?: boolean;
  keyboardType?: CodegenTypes.WithDefault<
    | 'default'
    | 'number-pad'
    | 'decimal-pad'
    | 'numeric'
    | 'email-address'
    | 'phone-pad'
    | 'visible-password'
    | 'ascii-capable-number-pad'
    | 'numbers-and-punctuation'
    | 'url'
    | 'name-phone-pad'
    | 'twitter'
    | 'web-search',
    'default'
  >;
  returnKeyType?: CodegenTypes.WithDefault<
    | 'done'
    | 'go'
    | 'next'
    | 'none'
    | 'previous'
    | 'search'
    | 'send'
    | 'emergency-call',
    'done'
  >;
  maxLength?: CodegenTypes.Int32;
  multiline?: boolean;
  placeholder?: string;
  placeholderTextColor?: CodegenTypes.ColorValue;
  secureTextEntry?: boolean;
  selectionColor?: CodegenTypes.ColorValue;
  selectionHandleColor?: CodegenTypes.ColorValue;
  value?: string;
  defaultValue?: string;
  selectTextOnFocus?: boolean;
  blurOnSubmit?: boolean;
  caretHidden?: boolean;
  contextMenuHidden?: boolean;
  numberOfLines?: CodegenTypes.Int32;
  disableFullscreenUI?: boolean;
  textBreakStrategy?: CodegenTypes.WithDefault<
    'simple' | 'highQuality' | 'balanced',
    'simple'
  >;
  underlineColorAndroid?: CodegenTypes.ColorValue;
  showSoftInputOnFocus?: boolean;
  mostRecentEventCount: CodegenTypes.Int32;
  text?: string;

  // Text styling
  color?: CodegenTypes.Int32;
  fontSize?: CodegenTypes.Float;
  fontWeight?: string;
  fontFamily?: string;
  fontStyle?: string;
  letterSpacing?: CodegenTypes.Float;
  lineHeight?: CodegenTypes.Float;
  textAlign?: string;
  textAlignVertical?: string;
  textDecorationLine?: string;
  textTransform?: string;
  textShadowColor?: CodegenTypes.ColorValue;
  textShadowRadius?: CodegenTypes.Float;
  includeFontPadding?: boolean;
  cursorColor?: CodegenTypes.ColorValue;

  // TextInput events
  onBlur?: CodegenTypes.BubblingEventHandler<Readonly<{ target: CodegenTypes.Int32 }>>;
  onFocus?: CodegenTypes.BubblingEventHandler<Readonly<{ target: CodegenTypes.Int32 }>>;
  onChange?: CodegenTypes.BubblingEventHandler<
    Readonly<{
      target: CodegenTypes.Int32;
      eventCount: CodegenTypes.Int32;
      text: string;
    }>
  >;
  onChangeText?: CodegenTypes.BubblingEventHandler<
    Readonly<{
      target: CodegenTypes.Int32;
      eventCount: CodegenTypes.Int32;
      text: string;
    }>
  >;
  onEndEditing?: CodegenTypes.BubblingEventHandler<
    Readonly<{ target: CodegenTypes.Int32; text: string }>
  >;
  onSubmitEditing?: CodegenTypes.BubblingEventHandler<
    Readonly<{ target: CodegenTypes.Int32; text: string }>
  >;
  onKeyPress?: CodegenTypes.BubblingEventHandler<
    Readonly<{ target: CodegenTypes.Int32; key: string }>
  >;
  onSelectionChange?: CodegenTypes.DirectEventHandler<
    Readonly<{
      target: CodegenTypes.Int32;
      selection: Readonly<{
        start: CodegenTypes.Double;
        end: CodegenTypes.Double;
      }>;
    }>
  >;
  onContentSizeChange?: CodegenTypes.DirectEventHandler<
    Readonly<{
      target: CodegenTypes.Int32;
      contentSize: Readonly<{
        width: CodegenTypes.Double;
        height: CodegenTypes.Double;
      }>;
    }>
  >;
  onScroll?: CodegenTypes.DirectEventHandler<
    Readonly<{
      target: CodegenTypes.Int32;
      responderIgnoreScroll: boolean;
      contentInset: Readonly<{
        top: CodegenTypes.Double;
        left: CodegenTypes.Double;
        bottom: CodegenTypes.Double;
        right: CodegenTypes.Double;
      }>;
      contentOffset: Readonly<{
        x: CodegenTypes.Double;
        y: CodegenTypes.Double;
      }>;
      contentSize: Readonly<{
        width: CodegenTypes.Double;
        height: CodegenTypes.Double;
      }>;
      layoutMeasurement: Readonly<{
        width: CodegenTypes.Double;
        height: CodegenTypes.Double;
      }>;
      velocity: Readonly<{
        x: CodegenTypes.Double;
        y: CodegenTypes.Double;
      }>;
    }>
  >;
}

export default codegenNativeComponent<NativeProps>('SyncFormatEdittextView');
