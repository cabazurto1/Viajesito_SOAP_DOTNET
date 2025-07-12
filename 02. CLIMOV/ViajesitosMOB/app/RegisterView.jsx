import React, { useState } from 'react';
import {
  View,
  TextInput,
  Button,
  Text,
  StyleSheet,
  Platform,
  KeyboardAvoidingView,
  Image,
  TouchableOpacity,
  Modal
} from 'react-native';
import { useRouter } from 'expo-router';
import { crearUsuario } from './controllers/UsuarioController';

function validarCorreo(correo) {
  const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return regex.test(correo);
}

export default function RegisterView() {
  const router = useRouter();
  const [nombre, setNombre] = useState('');
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [telefono, setTelefono] = useState('');
  const [cedula, setCedula] = useState('');
  const [correo, setCorreo] = useState('');
  const [secure, setSecure] = useState(true);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [mensaje, setMensaje] = useState('');
  const [esExito, setEsExito] = useState(false);

  const handleRegister = async () => {
    if (!nombre.trim() || !username.trim() || !password.trim() || !telefono.trim() || !cedula.trim() || !correo.trim()) {
      setMensaje('⚠️ Todos los campos son obligatorios');
      setEsExito(false);
      setModalVisible(true);
      return;
    }

    if (!/^[0-9]{10}$/.test(telefono.trim())) {
      setMensaje('⚠️ El teléfono debe tener 10 dígitos');
      setEsExito(false);
      setModalVisible(true);
      return;
    }

    if (!validarCorreo(correo.trim())) {
      setMensaje('⚠️ Formato de correo no válido');
      setEsExito(false);
      setModalVisible(true);
      return;
    }

    setLoading(true);
    try {
      const success = await crearUsuario({
        IdUsuario: 0,
        Nombre: nombre.trim(),
        Username: username.trim(),
        Password: password.trim(),
        Telefono: telefono.trim(),
        Cedula: cedula.trim(),
        Correo: correo.trim()
      });

      if (success === true) {
        setMensaje('✅ Registro exitoso. Ya puedes iniciar sesión.');
        setEsExito(true);
      } else {
        setMensaje('❌ El usuario ya está registrado o ocurrió un error.');
        setEsExito(false);
      }
    } catch (error) {
      setMensaje('❌ Error de conexión o del servidor.');
      setEsExito(false);
    } finally {
      setLoading(false);
      setModalVisible(true);
    }
  };

  const volverAlLogin = () => {
    setModalVisible(false);
    if (esExito) router.replace('/LoginView');
  };

  return (
    <KeyboardAvoidingView
      style={styles.container}
      behavior={Platform.OS === 'ios' ? 'padding' : undefined}
    >
      <Image source={require('../assets/images/logo_monster.png')} style={styles.logo} />
      <Text style={styles.title}>Crear cuenta</Text>

      <View style={styles.inputWrapper}>
        <TextInput
          style={styles.input}
          placeholder="Nombre completo"
          value={nombre}
          onChangeText={setNombre}
          placeholderTextColor="#888"
        />
      </View>

      <View style={styles.inputWrapper}>
        <TextInput
          style={styles.input}
          placeholder="Usuario"
          value={username}
          onChangeText={setUsername}
          placeholderTextColor="#888"
          autoCapitalize="none"
        />
      </View>

      <View style={styles.inputWrapper}>
        <TextInput
          style={styles.input}
          placeholder="Contraseña"
          secureTextEntry={secure}
          value={password}
          onChangeText={setPassword}
          placeholderTextColor="#888"
          autoCapitalize="none"
        />
        <TouchableOpacity onPress={() => setSecure(!secure)} style={styles.toggle}>
          <Text style={styles.toggleText}>{secure ? 'Mostrar' : 'Ocultar'}</Text>
        </TouchableOpacity>
      </View>

      <View style={styles.inputWrapper}>
        <TextInput
          style={styles.input}
          placeholder="Teléfono (10 dígitos)"
          keyboardType="phone-pad"
          value={telefono}
          onChangeText={(text) => {
            if (text.length <= 10 && /^[0-9]*$/.test(text)) setTelefono(text);
          }}
          placeholderTextColor="#888"
        />
      </View>

      <View style={styles.inputWrapper}>
        <TextInput
          style={styles.input}
          placeholder="Cédula"
          keyboardType="number-pad"
          value={cedula}
          onChangeText={(text) => {
            if (text.length <= 10 && /^[0-9]*$/.test(text)) setCedula(text);
          }}
          placeholderTextColor="#888"
        />
      </View>

      <View style={styles.inputWrapper}>
        <TextInput
          style={styles.input}
          placeholder="Correo electrónico"
          keyboardType="email-address"
          value={correo}
          onChangeText={setCorreo}
          placeholderTextColor="#888"
          autoCapitalize="none"
        />
      </View>

      <View style={styles.buttonContainer}>
        <Button
          title={loading ? 'Registrando...' : 'REGISTRAR'}
          color="#5fb4a2"
          onPress={handleRegister}
          disabled={loading}
        />
      </View>

      <TouchableOpacity onPress={() => router.replace('/LoginView')} style={{ marginTop: 16 }}>
        <Text style={{ color: '#4e88a9', fontWeight: '500' }}>
          ¿Ya tienes cuenta? Inicia sesión
        </Text>
      </TouchableOpacity>

      <Modal
        visible={modalVisible}
        transparent
        animationType="fade"
        onRequestClose={() => setModalVisible(false)}
      >
        <View style={styles.modalOverlay}>
          <View style={styles.modalContainer}>
            <Text style={[styles.modalMessage, { color: esExito ? 'green' : 'red' }]}> {mensaje} </Text>
            <TouchableOpacity onPress={volverAlLogin} style={styles.modalButton}>
              <Text style={{ color: '#fff', fontWeight: 'bold' }}>Aceptar</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  container: {
    padding: 24,
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#fff'
  },
  logo: {
    width: 150,
    height: 60,
    resizeMode: 'contain',
    marginBottom: 10
  },
  title: {
    fontSize: 26,
    fontWeight: '600',
    color: '#4e88a9',
    textAlign: 'center',
    marginBottom: 24
  },
  inputWrapper: {
    width: '90%',
    maxWidth: 400,
    marginBottom: 16,
    position: 'relative'
  },
  input: {
    backgroundColor: '#f0f4f8',
    padding: 14,
    borderRadius: 12,
    borderColor: '#cfe0e8',
    borderWidth: 1,
    color: '#333',
    paddingRight: 80
  },
  toggle: {
    position: 'absolute',
    right: 14,
    top: '25%'
  },
  toggleText: {
    color: '#4e88a9',
    fontWeight: '500'
  },
  buttonContainer: {
    width: '90%',
    maxWidth: 400,
    marginTop: 8,
    borderRadius: 10,
    overflow: 'hidden'
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: 'rgba(0,0,0,0.4)',
    justifyContent: 'center',
    alignItems: 'center'
  },
  modalContainer: {
    backgroundColor: '#fff',
    padding: 24,
    borderRadius: 12,
    width: '80%',
    alignItems: 'center'
  },
  modalMessage: {
    fontSize: 16,
    fontWeight: '500',
    marginBottom: 16,
    textAlign: 'center'
  },
  modalButton: {
    backgroundColor: '#4e88a9',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8
  }
});
