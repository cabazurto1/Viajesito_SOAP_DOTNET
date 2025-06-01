import React, { useEffect, useState } from 'react';
import {
  View,
  Text,
  FlatList,
  TouchableOpacity,
  Modal,
  ActivityIndicator,
  Platform,
  TextInput,
  Alert,
  KeyboardAvoidingView,
  StyleSheet
} from 'react-native';
import { useRouter } from 'expo-router';
import AsyncStorage from '@react-native-async-storage/async-storage';

import { obtenerCiudades } from '../controllers/CiudadController';
import { buscarVuelos as buscarVuelosAPI } from '../controllers/VueloController';
import { registrarBoleto } from '../controllers/BoletoController';

export default function ComprarBoletoView() {
  const router = useRouter();
  const [usuario, setUsuario] = useState(null);
  const [ciudades, setCiudades] = useState([]);
  const [origen, setOrigen] = useState('');
  const [destino, setDestino] = useState('');
  const [fecha, setFecha] = useState('');
  const [vuelos, setVuelos] = useState([]);
  const [loading, setLoading] = useState(false);
  const [mensaje, setMensaje] = useState('');
  const [modalVisible, setModalVisible] = useState(false);
  const [cantidad, setCantidad] = useState(1);

  useEffect(() => {
    const cargar = async () => {
      try {
        const data = await obtenerCiudades();
        setCiudades(Array.isArray(data) ? data : [data]);
      } catch (e) {
        console.error('Error cargando ciudades:', e);
        setCiudades([]);
      }

      const storedId = await AsyncStorage.getItem('idUsuario');
      if (storedId) setUsuario(parseInt(storedId));
      else router.replace('/');
    };
    cargar();
  }, []);

  // Función para limpiar todos los datos del formulario
  const limpiarFormulario = () => {
    setOrigen('');
    setDestino('');
    setFecha('');
    setVuelos([]);
    setCantidad(1);
  };

  const handleBuscarVuelos = async () => {
    if (!origen || !destino || origen === destino || !/^[0-9]{4}-[0-9]{2}-[0-9]{2}$/.test(fecha)) {
      Alert.alert('Error', 'Seleccione ciudades válidas y una fecha con formato correcto (YYYY-MM-DD).');
      return;
    }
    setLoading(true);
    try {
      const resultados = await buscarVuelosAPI(origen, destino, fecha);
      const lista = Array.isArray(resultados) ? resultados : resultados ? [resultados] : [];
      setVuelos(lista);
      if (lista.length === 0) Alert.alert('Sin vuelos disponibles');
    } catch (error) {
      console.error('Error al buscar vuelos:', error);
      Alert.alert('Error', 'No se pudo buscar vuelos.');
    } finally {
      setLoading(false);
    }
  };

  const handleComprar = async (vuelo) => {
    if (!usuario || isNaN(usuario)) {
      setMensaje('❌ ID de usuario no disponible. Intente ingresar nuevamente.');
      setModalVisible(true);
      return;
    }

    if (!cantidad || isNaN(cantidad) || cantidad <= 0 || cantidad > vuelo.Disponibles) {
      setMensaje(`❌ Cantidad inválida. Disponible: ${vuelo.Disponibles}`);
      setModalVisible(true);
      return;
    }

    const total = (vuelo.Valor * cantidad).toFixed(2);
    try {
      const resultado = await registrarBoleto({
        idVuelo: vuelo.IdVuelo,
        idUsuario: usuario,
        cantidad
      });

      if (resultado) {
        setMensaje(`✅ Compra realizada\n\n✈ Vuelo: ${vuelo.CodigoVuelo}\n🎟 Cantidad: ${cantidad}\n💵 Total: $${total}`);
        setModalVisible(true);
        
        // Limpiar formulario después de compra exitosa
        limpiarFormulario();
        
        setTimeout(() => {
          setModalVisible(false);
          router.replace({ pathname: '/views/MenuView', params: { idUsuario: usuario } });
        }, 3000);
      } else {
        setMensaje('❌ No se pudo completar la compra');
        setModalVisible(true);
      }
    } catch (e) {
      console.error('Error al comprar:', e);
      setMensaje('❌ Error inesperado.');
      setModalVisible(true);
    }
  };

  const renderCiudadItem = (item, onSelect, selected) => (
    <TouchableOpacity
      style={[styles.ciudadBtn, selected === item.codigo && styles.ciudadBtnSelected]}
      onPress={() => onSelect(item.codigo)}
    >
      <Text style={styles.ciudadText}>{item.codigo} - {item.nombre}</Text>
    </TouchableOpacity>
  );

  const renderVueloItem = ({ item }) => (
    <View style={styles.card}>
      <Text style={styles.title}>✈️ {item.CodigoVuelo}</Text>
      <Text>Salida: {item.HoraSalida}</Text>
      <Text>Precio: ${item.Valor}</Text>
      <Text>Disponibles: {item.Disponibles}</Text>
      <TouchableOpacity style={styles.btn} onPress={() => handleComprar(item)}>
        <Text style={styles.btnText}>Comprar</Text>
      </TouchableOpacity>
    </View>
  );

  const headerComponent = (
    <View style={styles.innerWrapper}>
      <Text style={styles.header}>Compra de Boletos</Text>

      <Text style={styles.subheader}>Ciudad de Origen</Text>
      <FlatList
        horizontal
        data={ciudades.filter(c => c.codigo !== destino)}
        keyExtractor={item => item.codigo}
        renderItem={({ item }) => renderCiudadItem(item, setOrigen, origen)}
      />

      <Text style={styles.subheader}>Ciudad de Destino</Text>
      <FlatList
        horizontal
        data={ciudades.filter(c => c.codigo !== origen)}
        keyExtractor={item => item.codigo}
        renderItem={({ item }) => renderCiudadItem(item, setDestino, destino)}
      />

      <Text style={styles.subheader}>Fecha de Vuelo (YYYY-MM-DD)</Text>
      <TextInput
        style={styles.fechaInput}
        value={fecha}
        onChangeText={setFecha}
        placeholder="2025-06-15"
      />

      <Text style={styles.subheader}>Cantidad de Boletos</Text>
      <View style={styles.stepperContainer}>
        <TouchableOpacity
          style={styles.stepperButton}
          onPress={() => setCantidad(prev => Math.max(1, prev - 1))}
        >
          <Text style={styles.stepperText}>−</Text>
        </TouchableOpacity>
        <Text style={styles.cantidadText}>{cantidad}</Text>
        <TouchableOpacity
          style={styles.stepperButton}
          onPress={() => setCantidad(prev => Math.min(10, prev + 1))}
        >
          <Text style={styles.stepperText}>+</Text>
        </TouchableOpacity>
      </View>

      <TouchableOpacity style={styles.buscarBtn} onPress={handleBuscarVuelos}>
        <Text style={styles.btnText}>🔍 Buscar Vuelos</Text>
      </TouchableOpacity>

      {/* Botón para limpiar formulario manualmente */}
      <TouchableOpacity style={styles.limpiarBtn} onPress={limpiarFormulario}>
        <Text style={styles.limpiarBtnText}>🗑️ Limpiar Formulario</Text>
      </TouchableOpacity>

      {loading && <ActivityIndicator size="large" color="#4e88a9" style={{ marginTop: 20 }} />}
    </View>
  );

  const footerComponent = (
    <TouchableOpacity
      style={styles.volver}
      onPress={() => router.replace({ pathname: '/views/MenuView', params: { idUsuario: usuario } })}
    >
      <Text style={styles.volverText}>← Volver al Menú</Text>
    </TouchableOpacity>
  );

  return (
    <KeyboardAvoidingView behavior="padding" style={styles.fullScreen}>
      <FlatList
        data={vuelos}
        ListHeaderComponent={headerComponent}
        ListFooterComponent={footerComponent}
        renderItem={renderVueloItem}
        keyExtractor={item => `${item.IdVuelo}`}
        contentContainerStyle={styles.container}
        ListEmptyComponent={!loading && <Text style={{ textAlign: 'center', marginTop: 20 }}>No hay vuelos para mostrar.</Text>}
      />

      <Modal transparent visible={modalVisible} animationType="slide">
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={{ marginBottom: 10, textAlign: 'center' }}>{mensaje}</Text>
            <TouchableOpacity style={styles.btn} onPress={() => setModalVisible(false)}>
              <Text style={styles.btnText}>Aceptar</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  fullScreen: { flex: 1, backgroundColor: '#f9fbfd' },
  container: { paddingVertical: 30, alignItems: 'center' },
  innerWrapper: { width: '100%', maxWidth: 600, paddingHorizontal: 16 },
  header: { fontSize: 24, fontWeight: 'bold', marginBottom: 10, textAlign: 'center' },
  subheader: { fontWeight: 'bold', marginTop: 10, marginBottom: 5 },
  ciudadBtn: { backgroundColor: '#ddd', padding: 10, margin: 4, borderRadius: 8 },
  ciudadBtnSelected: { backgroundColor: '#4e88a9' },
  ciudadText: { color: '#000' },
  fechaInput: { backgroundColor: '#eaeaea', padding: 10, borderRadius: 6, marginTop: 5, fontSize: 16, width: '100%' },
  buscarBtn: { backgroundColor: '#28a745', padding: 12, borderRadius: 10, marginTop: 15, alignItems: 'center', width: '100%', borderWidth: 1, borderColor: '#0e5b28' },
  limpiarBtn: { backgroundColor: '#6c757d', padding: 12, borderRadius: 10, marginTop: 10, alignItems: 'center', width: '100%', borderWidth: 1, borderColor: '#495057' },
  limpiarBtnText: { color: '#fff', fontWeight: 'bold' },
  card: { backgroundColor: '#f1fbfc', padding: 16, borderRadius: 12, marginVertical: 8, borderWidth: 1, borderColor: '#cfe0e8', width: '100%' },
  title: { fontSize: 18, fontWeight: 'bold' },
  btn: { backgroundColor: '#4e88a9', padding: 10, marginTop: 10, borderRadius: 8, alignItems: 'center' },
  btnText: { color: '#fff', fontWeight: 'bold' },
  volver: { backgroundColor: '#ddd', marginTop: 20, padding: 12, borderRadius: 10, alignItems: 'center', width: '100%' },
  volverText: { color: '#444', fontWeight: 'bold', fontSize: 16 },
  modalContainer: { flex: 1, justifyContent: 'center', backgroundColor: 'rgba(0,0,0,0.4)', alignItems: 'center' },
  modalContent: { backgroundColor: '#fff', padding: 24, borderRadius: 12, alignItems: 'center', maxWidth: 400 },
  stepperContainer: { flexDirection: 'row', alignItems: 'center', justifyContent: 'center', marginVertical: 10, gap: 20 },
  stepperButton: { backgroundColor: '#4e88a9', paddingHorizontal: 16, paddingVertical: 8, borderRadius: 8 },
  stepperText: { fontSize: 20, color: '#fff', fontWeight: 'bold' },
  cantidadText: { fontSize: 18, fontWeight: 'bold', minWidth: 30, textAlign: 'center' }
});