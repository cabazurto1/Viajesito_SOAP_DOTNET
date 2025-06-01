import React, { useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  ScrollView,
  StyleSheet,
  KeyboardAvoidingView,
  Platform
} from 'react-native';
import { MaterialIcons } from '@expo/vector-icons';
import { useRouter, useLocalSearchParams } from 'expo-router';
import AsyncStorage from '@react-native-async-storage/async-storage';
import BackgroundWrapper from '../../screens/BackgroundWrapper';

export default function MenuView() {
  const router = useRouter();
  const { idUsuario, nombre } = useLocalSearchParams();

  useEffect(() => {
    if (!idUsuario) {
      router.replace('/');
    }
  }, [idUsuario]);

  const handleNavigation = (route) => {
    router.push({
      pathname: `/views/${route}`,
      params: { idUsuario, nombre }
    });
  };

  const cerrarSesion = async () => {
    await AsyncStorage.clear(); // Elimina todo, incluyendo idUsuario
    router.replace('/');
  };

  return (
    <BackgroundWrapper>
      <KeyboardAvoidingView
        style={{ flex: 1 }}
        behavior={Platform.OS === 'ios' ? 'padding' : undefined}
      >
        <View style={styles.overlay}>
          <Text style={styles.title}>✈️ Hola, {nombre || 'Viajero'}</Text>
          <Text style={styles.subtitle}>Tu aerolínea de confianza.</Text>

          <ScrollView
            contentContainerStyle={styles.buttonContainer}
            showsVerticalScrollIndicator={false}
          >
            <TouchableOpacity
              style={styles.option}
              onPress={() => handleNavigation('MisBoletosView')}
            >
              <MaterialIcons name="confirmation-number" size={28} color="#35798e" />
              <Text style={styles.label}>Mis Boletos</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.option}
              onPress={() => handleNavigation('VuelosDisponiblesView')}
            >
              <MaterialIcons name="flight-takeoff" size={28} color="#35798e" />
              <Text style={styles.label}>Vuelos Disponibles</Text>
            </TouchableOpacity>

            <TouchableOpacity
              style={styles.option}
              onPress={() => handleNavigation('ComprarBoletoView')}
            >
              <MaterialIcons name="shopping-cart" size={28} color="#35798e" />
              <Text style={styles.label}>Comprar Boletos</Text>
            </TouchableOpacity>
          </ScrollView>

          <TouchableOpacity
            style={[styles.option, styles.logoutBtn]}
            onPress={cerrarSesion}
          >
            <MaterialIcons name="logout" size={28} color="#d04848" />
            <Text style={[styles.label, styles.logoutText]}>Cerrar sesión</Text>
          </TouchableOpacity>
        </View>
      </KeyboardAvoidingView>
    </BackgroundWrapper>
  );
}

const styles = StyleSheet.create({
  overlay: {
    backgroundColor: 'rgba(255,255,255,0.95)',
    margin: 20,
    padding: 20,
    borderRadius: 20,
    alignItems: 'center',
    flex: 1
  },
  title: {
    fontSize: 26,
    fontWeight: 'bold',
    color: '#4e88a9',
    marginBottom: 6
  },
  subtitle: {
    fontSize: 16,
    color: '#5fb4a2',
    marginBottom: 22
  },
  buttonContainer: {
    flexGrow: 1,
    justifyContent: 'center',
    gap: 20
  },
  option: {
    backgroundColor: '#e6f7f7',
    padding: 16,
    borderRadius: 16,
    width: 260,
    alignItems: 'center',
    flexDirection: 'row',
    gap: 12,
    borderWidth: 1,
    borderColor: '#cfe0e8'
  },
  label: {
    fontSize: 16,
    fontWeight: '600',
    color: '#35798e'
  },
  logoutBtn: {
    backgroundColor: '#ffecec',
    marginTop: 30,
    borderColor: '#f5bcbc'
  },
  logoutText: {
    color: '#d04848'
  }
});
