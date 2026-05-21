import type { CodegenTypes } from 'react-native';
import { codegenNativeComponent, type ViewProps } from 'react-native';

type KeyboardType =
  | 'default'
  | 'email-address'
  | 'numeric'
  | 'phone-pad'
  | 'number-pad'
  | 'decimal-pad'
  | 'url'
  | 'ascii-capable'
  | 'numbers-and-punctuation'
  | 'name-phone-pad'
  | 'twitter'
  | 'web-search'
  | 'visible-password';

type ReturnKeyType =
  | 'done'
  | 'go'
  | 'next'
  | 'search'
  | 'send'
  | 'none'
  | 'previous'
  | 'default'
  | 'emergency-call'
  | 'google'
  | 'join'
  | 'route'
  | 'yahoo';

type SubmitBehavior = 'submit' | 'blurAndSubmit' | 'newline';

interface NativeProps extends ViewProps {
  // Android-specific props
  autoComplete?: CodegenTypes.WithDefault<
    | 'birthdate-day'
    | 'birthdate-full'
    | 'birthdate-month'
    | 'birthdate-year'
    | 'cc-csc'
    | 'cc-exp'
    | 'cc-exp-day'
    | 'cc-exp-month'
    | 'cc-exp-year'
    | 'cc-number'
    | 'email'
    | 'gender'
    | 'name'
    | 'name-family'
    | 'name-given'
    | 'name-middle'
    | 'name-middle-initial'
    | 'name-prefix'
    | 'name-suffix'
    | 'password'
    | 'password-new'
    | 'postal-address'
    | 'postal-address-country'
    | 'postal-address-extended'
    | 'postal-address-extended-postal-code'
    | 'postal-address-locality'
    | 'postal-address-region'
    | 'postal-code'
    | 'street-address'
    | 'sms-otp'
    | 'tel'
    | 'tel-country-code'
    | 'tel-national'
    | 'tel-device'
    | 'username'
    | 'username-new'
    | 'off',
    'off'
  >;
  returnKeyLabel?: string;
  numberOfLines?: CodegenTypes.Int32;
  disableFullscreenUI?: boolean;
  textBreakStrategy?: CodegenTypes.WithDefault<
    'simple' | 'highQuality' | 'balanced',
    'simple'
  >;
  underlineColorAndroid?: CodegenTypes.Int32;
  inlineImageLeft?: string;
  inlineImagePadding?: CodegenTypes.Int32;
  importantForAutofill?: string;
  showSoftInputOnFocus?: boolean;

  // TextInput core props
  autoCapitalize?: CodegenTypes.WithDefault<
    'none' | 'sentences' | 'words' | 'characters',
    'none'
  >;
  autoCorrect?: boolean;
  autoFocus?: boolean;
  allowFontScaling?: boolean;
  maxFontSizeMultiplier?: CodegenTypes.Float;
  editable?: boolean;
  keyboardType?: CodegenTypes.WithDefault<KeyboardType, 'default'>;
  returnKeyType?: CodegenTypes.WithDefault<ReturnKeyType, 'done'>;
  maxLength?: CodegenTypes.Int32;
  multiline?: boolean;

  // Events
  onBlur?: CodegenTypes.BubblingEventHandler<{ target: CodegenTypes.Int32 }>;
  onFocus?: CodegenTypes.BubblingEventHandler<{ target: CodegenTypes.Int32 }>;
  onChange?: CodegenTypes.BubblingEventHandler<{
    target: CodegenTypes.Int32;
    eventCount: CodegenTypes.Int32;
    text: string;
  }>;
  onChangeText?: CodegenTypes.BubblingEventHandler<{
    target: CodegenTypes.Int32;
    eventCount: CodegenTypes.Int32;
    text: string;
  }>;
  onContentSizeChange?: CodegenTypes.DirectEventHandler<{
    target: CodegenTypes.Int32;
    contentSize: { width: CodegenTypes.Double; height: CodegenTypes.Double };
  }>;
  onEndEditing?: CodegenTypes.BubblingEventHandler<{
    target: CodegenTypes.Int32;
    text: string;
  }>;
  onSelectionChange?: CodegenTypes.DirectEventHandler<{
    target: CodegenTypes.Int32;
    selection: { start: CodegenTypes.Double; end: CodegenTypes.Double };
  }>;
  onSubmitEditing?: CodegenTypes.BubblingEventHandler<{
    target: CodegenTypes.Int32;
    text: string;
  }>;
  onKeyPress?: CodegenTypes.BubblingEventHandler<{
    target: CodegenTypes.Int32;
    key: string;
  }>;
  onScroll?: CodegenTypes.DirectEventHandler<{
    target: CodegenTypes.Int32;
    responderIgnoreScroll: boolean;
    contentInset: {
      top: CodegenTypes.Double;
      bottom: CodegenTypes.Double;
      left: CodegenTypes.Double;
      right: CodegenTypes.Double;
    };
    contentOffset: { x: CodegenTypes.Double; y: CodegenTypes.Double };
    contentSize: { width: CodegenTypes.Double; height: CodegenTypes.Double };
    layoutMeasurement: { width: CodegenTypes.Double; height: CodegenTypes.Double };
    velocity: { x: CodegenTypes.Double; y: CodegenTypes.Double };
  }>;

  // Value and display props
  placeholder?: string;
  placeholderTextColor?: CodegenTypes.Int32;
  secureTextEntry?: boolean;
  selectionColor?: CodegenTypes.Int32;
  selectionHandleColor?: CodegenTypes.Int32;
  selection?: { start: CodegenTypes.Int32; end?: CodegenTypes.Int32 };
  value?: string;
  defaultValue?: string;
  selectTextOnFocus?: boolean;
  blurOnSubmit?: boolean;
  submitBehavior?: CodegenTypes.WithDefault<SubmitBehavior, 'blurAndSubmit'>;
  caretHidden?: boolean;
  contextMenuHidden?: boolean;

  // Text styling props
  textShadowColor?: CodegenTypes.Int32;
  textShadowRadius?: CodegenTypes.Float;
  textDecorationLine?: string;
  fontStyle?: string;
  textShadowOffset?: { width?: CodegenTypes.Double; height?: CodegenTypes.Double };
  lineHeight?: CodegenTypes.Float;
  textTransform?: string;
  color?: CodegenTypes.Int32;
  letterSpacing?: CodegenTypes.Float;
  fontSize?: CodegenTypes.Float;
  textAlign?: string;
  includeFontPadding?: boolean;
  fontWeight?: string;
  fontFamily?: string;
  textAlignVertical?: string;
  cursorColor?: CodegenTypes.Int32;

  // Internal props (used by TextInput.js)
  mostRecentEventCount?: CodegenTypes.Int32;
  text?: string;

  // Custom: sync format event
  onSyncFormatChange?: CodegenTypes.DirectEventHandler<{
    text: string;
    cursorPos: CodegenTypes.Double;
  }>;
}

export default codegenNativeComponent<NativeProps>('SyncFormatEdittextView');
