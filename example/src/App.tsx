import { useState, useCallback } from 'react';
import { View, Text, StyleSheet } from 'react-native';
import { SyncFormatEdittextView } from 'react-native-sync-format-edittext';

function formatPhone(text: string, cursorPos: number) {
  const digits = text.replace(/\D/g, '').slice(0, 11);
  let formatted = '';
  let newCursorPos = cursorPos;
  const connectSignal = '-';
  if (digits.length <= 3) {
    formatted = digits;
  } else if (digits.length <= 7) {
    formatted = `${digits.slice(0, 3)}${connectSignal}${digits.slice(3)}`;
    if (cursorPos > 3) newCursorPos = cursorPos + 1;
  } else {
    formatted = `${digits.slice(0, 3)}${connectSignal}${digits.slice(3, 7)}${connectSignal}${digits.slice(7)}`;
    if (cursorPos > 3) newCursorPos = cursorPos + 1;
    if (cursorPos > 7) newCursorPos = cursorPos + 1;
  }

  return {
    text: formatted,
    cursorPos: Math.min(newCursorPos, formatted.length),
  };
}

function formatLettersOnly(text: string, cursorPos: number) {
  const letters = text.replace(/[^a-zA-Z]/g, '').slice(0, 10);
  let lettersBefore = 0;
  for (let i = 0; i < Math.min(cursorPos, text.length); i++) {
    if (/[a-zA-Z]/.test(text[i]!)) lettersBefore++;
  }
  return {
    text: letters,
    cursorPos: Math.min(lettersBefore, letters.length),
  };
}

export default function App() {
  const [phone, setPhone] = useState('');
  const [letters, setLetters] = useState('');
  const [password, setPassword] = useState('');

  const handlePhoneChange = useCallback((text: string) => {
    setPhone(text);
  }, []);

  const handleLettersChange = useCallback((text: string) => {
    setLetters(text);
  }, []);

  const handlePasswordChange = useCallback((text: string) => {
    setPassword(text);
  }, []);

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Phone (keyboardType=phone-pad)</Text>
      <SyncFormatEdittextView
        value={phone}
        format={formatPhone}
        onChangeText={handlePhoneChange}
        placeholder="Enter phone number"
        keyboardType="phone-pad"
        style={styles.input}
      />
      <Text style={styles.preview}>Value: {phone}</Text>

      <Text style={[styles.title, { marginTop: 24 }]}>
        Letters (maxLength=10)
      </Text>
      <SyncFormatEdittextView
        value={letters}
        format={formatLettersOnly}
        onChangeText={handleLettersChange}
        placeholder="Enter letters only"
        maxLength={10}
        autoCapitalize="characters"
        style={styles.input}
      />
      <Text style={styles.preview}>Value: {letters}</Text>

      <Text style={[styles.title, { marginTop: 24 }]}>
        Password (secureTextEntry)
      </Text>
      <SyncFormatEdittextView
        value={password}
        onChangeText={handlePasswordChange}
        placeholder="Enter password"
        secureTextEntry={true}
        style={styles.input}
      />
      <Text style={styles.preview}>Value: {password}</Text>
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
