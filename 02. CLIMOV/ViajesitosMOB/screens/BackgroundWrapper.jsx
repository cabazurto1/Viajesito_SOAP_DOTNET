// app/components/BackgroundWrapper.jsx
import React from 'react';
import { ImageBackground, StyleSheet } from 'react-native';

export default function BackgroundWrapper({ children }) {
  return (
    <ImageBackground
      source={require('../assets/images/fondo.jpg')}
      style={styles.background}
      resizeMode="cover"
    >
      {children}
    </ImageBackground>
  );
}

const styles = StyleSheet.create({
  background: {
    flex: 1,
    width: '100%',
    height: '100%'
  }
});
