import { useState, useCallback } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { SyncFormatEdittextView } from 'react-native-sync-format-edittext';

function formatPhone(text: string, cursorPos: number) {
  const digits = text.replace(/\D/g, '').slice(0, 11);
  let formatted = '';
  let newCursorPos = cursorPos;

  if (digits.length <= 3) {
    formatted = digits;
  } else if (digits.length <= 7) {
    formatted = `${digits.slice(0, 3)}-${digits.slice(3)}`;
    if (cursorPos > 3) newCursorPos = cursorPos + 1;
  } else {
    formatted = `${digits.slice(0, 3)}-${digits.slice(3, 7)}-${digits.slice(7)}`;
    if (cursorPos > 3) newCursorPos = cursorPos + 1;
    if (cursorPos > 7) newCursorPos = cursorPos + 1;
  }

  return {
    text: formatted,
    cursorPos: Math.min(newCursorPos, formatted.length),
  };
}

export default function App() {
  const [phone, setPhone] = useState('');

  const handleChange = useCallback((text: string, _cursorPos: number) => {
    setPhone(text);
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>电话号码格式化</Text>
      <SyncFormatEdittextView
        value={phone}
        format={formatPhone}
        onChange={handleChange}
        placeholder="请输入电话号码"
        style={styles.input}
      />
      <Text style={styles.preview}>原始值: {phone}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    padding: 20,
  },
  title: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 16,
  },
  input: {
    width: '100%',
    height: 48,
    borderWidth: 1,
    borderColor: '#cccccc',
    borderRadius: 4,
    paddingHorizontal: 12,
    backgroundColor: '#ffffff',
  },
  preview: {
    marginTop: 16,
    fontSize: 14,
    color: '#666666',
  },
});
