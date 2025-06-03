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
  StyleSheet,
  Dimensions
} from 'react-native';
import { useRouter } from 'expo-router';
import AsyncStorage from '@react-native-async-storage/async-storage';

import { obtenerCiudades } from '../controllers/CiudadController';
import { buscarVuelos as buscarVuelosAPI } from '../controllers/VueloController';
import { registrarBoleto } from '../controllers/BoletoController';

const { width: screenWidth, height: screenHeight } = Dimensions.get('window');
const isSmallScreen = screenWidth < 400;
const isMediumScreen = screenWidth >= 400 && screenWidth < 600;

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
      <Text style={[styles.ciudadText, selected === item.codigo && styles.ciudadTextSelected]}>
        {item.codigo} - {item.nombre}
      </Text>
    </TouchableOpacity>
  );

  const renderVueloItem = ({ item }) => (
    <View style={styles.card}>
      <View style={styles.cardHeader}>
        <Text style={styles.title}>✈️ {item.CodigoVuelo}</Text>
      </View>
      <View style={styles.cardBody}>
        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>🕐 Salida:</Text>
          <Text style={styles.infoValue}>{item.HoraSalida}</Text>
        </View>
        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>💰 Precio:</Text>
          <Text style={styles.infoValue}>${item.Valor}</Text>
        </View>
        <View style={styles.infoRow}>
          <Text style={styles.infoLabel}>🎫 Disponibles:</Text>
          <Text style={styles.infoValue}>{item.Disponibles}</Text>
        </View>
      </View>
      <TouchableOpacity style={styles.comprarBtn} onPress={() => handleComprar(item)}>
        <Text style={styles.btnText}>🛒 Comprar</Text>
      </TouchableOpacity>
    </View>
  );

  const headerComponent = (
    <View style={styles.innerWrapper}>
      <View style={styles.headerContainer}>
        <Text style={styles.header}>🎟️ Compra de Boletos</Text>
      </View>

      <View style={styles.sectionContainer}>
        <Text style={styles.subheader}>🛫 Ciudad de Origen</Text>
        <View style={styles.ciudadScrollContainer}>
          <FlatList
            horizontal
            showsHorizontalScrollIndicator={false}
            data={ciudades.filter(c => c.codigo !== destino)}
            keyExtractor={item => item.codigo}
            renderItem={({ item }) => renderCiudadItem(item, setOrigen, origen)}
            contentContainerStyle={styles.ciudadListContainer}
            ItemSeparatorComponent={() => <View style={styles.ciudadSeparator} />}
            decelerationRate="fast"
            snapToInterval={isSmallScreen ? 120 : 140}
            snapToAlignment="start"
          />
        </View>
      </View>

      <View style={styles.sectionContainer}>
        <Text style={styles.subheader}>🛬 Ciudad de Destino</Text>
        <View style={styles.ciudadScrollContainer}>
          <FlatList
            horizontal
            showsHorizontalScrollIndicator={false}
            data={ciudades.filter(c => c.codigo !== origen)}
            keyExtractor={item => item.codigo}
            renderItem={({ item }) => renderCiudadItem(item, setDestino, destino)}
            contentContainerStyle={styles.ciudadListContainer}
            ItemSeparatorComponent={() => <View style={styles.ciudadSeparator} />}
            decelerationRate="fast"
            snapToInterval={isSmallScreen ? 120 : 140}
            snapToAlignment="start"
          />
        </View>
      </View>

      <View style={styles.sectionContainer}>
        <Text style={styles.subheader}>📅 Fecha de Vuelo (YYYY-MM-DD)</Text>
        <TextInput
          style={styles.fechaInput}
          value={fecha}
          onChangeText={setFecha}
          placeholder="2025-06-15"
          placeholderTextColor="#999"
        />
      </View>

      <View style={styles.sectionContainer}>
        <Text style={styles.subheader}>🎫 Cantidad de Boletos</Text>
        <View style={styles.stepperContainer}>
          <TouchableOpacity
            style={styles.stepperButton}
            onPress={() => setCantidad(prev => Math.max(1, prev - 1))}
          >
            <Text style={styles.stepperText}>−</Text>
          </TouchableOpacity>
          <View style={styles.cantidadContainer}>
            <Text style={styles.cantidadText}>{cantidad}</Text>
          </View>
          <TouchableOpacity
            style={styles.stepperButton}
            onPress={() => setCantidad(prev => Math.min(10, prev + 1))}
          >
            <Text style={styles.stepperText}>+</Text>
          </TouchableOpacity>
        </View>
      </View>

      <View style={styles.buttonContainer}>
        <TouchableOpacity style={styles.buscarBtn} onPress={handleBuscarVuelos}>
          <Text style={styles.btnText}>🔍 Buscar Vuelos</Text>
        </TouchableOpacity>

        <TouchableOpacity style={styles.limpiarBtn} onPress={limpiarFormulario}>
          <Text style={styles.limpiarBtnText}>🗑️ Limpiar Formulario</Text>
        </TouchableOpacity>
      </View>

      {loading && (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color="#4e88a9" />
          <Text style={styles.loadingText}>Buscando vuelos...</Text>
        </View>
      )}
    </View>
  );

  const footerComponent = (
    <View style={styles.footerContainer}>
      <TouchableOpacity
        style={styles.volver}
        onPress={() => router.replace({ pathname: '/views/MenuView', params: { idUsuario: usuario } })}
      >
        <Text style={styles.volverText}>← Volver al Menú</Text>
      </TouchableOpacity>
    </View>
  );

  return (
    <KeyboardAvoidingView 
      behavior={Platform.OS === 'ios' ? 'padding' : 'height'} 
      style={styles.fullScreen}
    >
      <FlatList
        data={vuelos}
        ListHeaderComponent={headerComponent}
        ListFooterComponent={footerComponent}
        renderItem={renderVueloItem}
        keyExtractor={item => `${item.IdVuelo}`}
        contentContainerStyle={styles.container}
        showsVerticalScrollIndicator={false}
        ListEmptyComponent={
          !loading && vuelos.length === 0 && (
            <View style={styles.emptyContainer}>
              <Text style={styles.emptyText}>✈️ No hay vuelos para mostrar</Text>
              <Text style={styles.emptySubtext}>Busca vuelos para ver opciones disponibles</Text>
            </View>
          )
        }
      />

      <Modal transparent visible={modalVisible} animationType="slide">
        <View style={styles.modalContainer}>
          <View style={styles.modalContent}>
            <Text style={styles.modalText}>{mensaje}</Text>
            <TouchableOpacity style={styles.modalBtn} onPress={() => setModalVisible(false)}>
              <Text style={styles.btnText}>Aceptar</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  fullScreen: { 
    flex: 1, 
    backgroundColor: '#f8fafc' 
  },
  container: { 
    paddingVertical: isSmallScreen ? 15 : 25,
    alignItems: 'center',
    minHeight: screenHeight - 100
  },
  innerWrapper: { 
    width: '100%', 
    maxWidth: isSmallScreen ? screenWidth - 20 : isMediumScreen ? screenWidth - 40 : 600, 
    paddingHorizontal: isSmallScreen ? 12 : 20
  },
  headerContainer: {
    alignItems: 'center',
    marginBottom: isSmallScreen ? 20 : 25,
    paddingBottom: 15,
    borderBottomWidth: 2,
    borderBottomColor: '#e2e8f0'
  },
  header: { 
    fontSize: isSmallScreen ? 22 : isMediumScreen ? 26 : 28, 
    fontWeight: 'bold', 
    color: '#1e293b',
    textAlign: 'center'
  },
  sectionContainer: {
    marginBottom: isSmallScreen ? 18 : 22,
    backgroundColor: '#ffffff',
    borderRadius: 12,
    padding: isSmallScreen ? 12 : 16,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3
  },
  subheader: { 
    fontWeight: 'bold', 
    fontSize: isSmallScreen ? 14 : 16,
    marginBottom: 10,
    color: '#374151',
    textAlign: 'center'
  },
  ciudadScrollContainer: {
    paddingVertical: 5,
  },
  ciudadListContainer: {
    paddingHorizontal: 10,
    alignItems: 'center',
    minHeight: isSmallScreen ? 50 : 55
  },
  ciudadSeparator: {
    width: isSmallScreen ? 8 : 12
  },
  ciudadBtn: { 
    backgroundColor: '#e2e8f0', 
    paddingVertical: isSmallScreen ? 10 : 12,
    paddingHorizontal: isSmallScreen ? 12 : 16,
    borderRadius: 12,
    minWidth: isSmallScreen ? 100 : 120,
    maxWidth: isSmallScreen ? 140 : 160,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 1 },
    shadowOpacity: 0.1,
    shadowRadius: 2,
    elevation: 2
  },
  ciudadBtnSelected: { 
    backgroundColor: '#4e88a9',
    transform: [{ scale: 1.05 }]
  },
  ciudadText: { 
    color: '#1f2937',
    fontSize: isSmallScreen ? 11 : 13,
    fontWeight: '500',
    textAlign: 'center',
    lineHeight: isSmallScreen ? 14 : 16,
    numberOfLines: 2,
    flexWrap: 'wrap'
  },
  ciudadTextSelected: {
    color: '#ffffff',
    fontWeight: 'bold'
  },
  fechaInput: { 
    backgroundColor: '#f1f5f9', 
    padding: isSmallScreen ? 12 : 14, 
    borderRadius: 10, 
    fontSize: isSmallScreen ? 14 : 16, 
    width: '100%',
    borderWidth: 1,
    borderColor: '#cbd5e0',
    textAlign: 'center',
    color: '#1f2937'
  },
  stepperContainer: { 
    flexDirection: 'row', 
    alignItems: 'center', 
    justifyContent: 'center',
    gap: isSmallScreen ? 15 : 20
  },
  stepperButton: { 
    backgroundColor: '#4e88a9', 
    width: isSmallScreen ? 40 : 45,
    height: isSmallScreen ? 40 : 45,
    borderRadius: isSmallScreen ? 20 : 22.5,
    alignItems: 'center',
    justifyContent: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.2,
    shadowRadius: 4,
    elevation: 4
  },
  stepperText: { 
    fontSize: isSmallScreen ? 18 : 22, 
    color: '#fff', 
    fontWeight: 'bold' 
  },
  cantidadContainer: {
    backgroundColor: '#f8fafc',
    paddingHorizontal: isSmallScreen ? 16 : 20,
    paddingVertical: isSmallScreen ? 8 : 10,
    borderRadius: 10,
    borderWidth: 2,
    borderColor: '#4e88a9',
    minWidth: isSmallScreen ? 50 : 60,
    alignItems: 'center'
  },
  cantidadText: { 
    fontSize: isSmallScreen ? 16 : 20, 
    fontWeight: 'bold',
    color: '#1f2937',
    textAlign: 'center'
  },
  buttonContainer: {
    gap: isSmallScreen ? 10 : 12,
    marginTop: 10
  },
  buscarBtn: { 
    backgroundColor: '#059669', 
    padding: isSmallScreen ? 14 : 16, 
    borderRadius: 12, 
    alignItems: 'center', 
    width: '100%', 
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.2,
    shadowRadius: 5,
    elevation: 5
  },
  limpiarBtn: { 
    backgroundColor: '#6b7280', 
    padding: isSmallScreen ? 12 : 14, 
    borderRadius: 12, 
    alignItems: 'center', 
    width: '100%',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.15,
    shadowRadius: 3,
    elevation: 3
  },
  limpiarBtnText: { 
    color: '#fff', 
    fontWeight: 'bold',
    fontSize: isSmallScreen ? 14 : 16
  },
  loadingContainer: {
    alignItems: 'center',
    marginTop: 20,
    paddingVertical: 20
  },
  loadingText: {
    marginTop: 10,
    color: '#4e88a9',
    fontSize: isSmallScreen ? 14 : 16,
    fontWeight: '500'
  },
  card: { 
    backgroundColor: '#ffffff', 
    padding: isSmallScreen ? 14 : 18, 
    borderRadius: 15, 
    marginVertical: isSmallScreen ? 6 : 8, 
    borderWidth: 1, 
    borderColor: '#e2e8f0', 
    width: '100%',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 4 },
    shadowOpacity: 0.1,
    shadowRadius: 6,
    elevation: 4
  },
  cardHeader: {
    borderBottomWidth: 1,
    borderBottomColor: '#e2e8f0',
    paddingBottom: 10,
    marginBottom: 12,
    alignItems: 'center'
  },
  title: { 
    fontSize: isSmallScreen ? 16 : 18, 
    fontWeight: 'bold',
    color: '#1e293b',
    textAlign: 'center'
  },
  cardBody: {
    gap: 8
  },
  infoRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    paddingVertical: 4
  },
  infoLabel: {
    fontSize: isSmallScreen ? 13 : 14,
    color: '#64748b',
    fontWeight: '500'
  },
  infoValue: {
    fontSize: isSmallScreen ? 13 : 14,
    color: '#1f2937',
    fontWeight: 'bold'
  },
  comprarBtn: { 
    backgroundColor: '#3b82f6', 
    padding: isSmallScreen ? 12 : 14, 
    marginTop: 15, 
    borderRadius: 10, 
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 3 },
    shadowOpacity: 0.2,
    shadowRadius: 4,
    elevation: 4
  },
  btnText: { 
    color: '#fff', 
    fontWeight: 'bold',
    fontSize: isSmallScreen ? 14 : 16
  },
  footerContainer: {
    width: '100%',
    alignItems: 'center',
    paddingTop: 20
  },
  volver: { 
    backgroundColor: '#e5e7eb', 
    padding: isSmallScreen ? 12 : 14, 
    borderRadius: 12, 
    alignItems: 'center', 
    width: '100%',
    maxWidth: 300,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 3,
    elevation: 2
  },
  volverText: { 
    color: '#374151', 
    fontWeight: 'bold', 
    fontSize: isSmallScreen ? 14 : 16
  },
  emptyContainer: {
    alignItems: 'center',
    paddingVertical: 40,
    paddingHorizontal: 20
  },
  emptyText: {
    fontSize: isSmallScreen ? 16 : 18,
    color: '#6b7280',
    fontWeight: '600',
    textAlign: 'center',
    marginBottom: 5
  },
  emptySubtext: {
    fontSize: isSmallScreen ? 13 : 14,
    color: '#9ca3af',
    textAlign: 'center'
  },
  modalContainer: { 
    flex: 1, 
    justifyContent: 'center', 
    backgroundColor: 'rgba(0,0,0,0.5)', 
    alignItems: 'center',
    paddingHorizontal: 20
  },
  modalContent: { 
    backgroundColor: '#fff', 
    padding: isSmallScreen ? 20 : 28, 
    borderRadius: 16, 
    alignItems: 'center', 
    maxWidth: isSmallScreen ? screenWidth - 40 : 400,
    width: '100%',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.3,
    shadowRadius: 20,
    elevation: 10
  },
  modalText: {
    marginBottom: 20,
    textAlign: 'center',
    fontSize: isSmallScreen ? 14 : 16,
    color: '#1f2937',
    lineHeight: isSmallScreen ? 20 : 24
  },
  modalBtn: {
    backgroundColor: '#4e88a9',
    paddingHorizontal: isSmallScreen ? 24 : 32,
    paddingVertical: isSmallScreen ? 10 : 12,
    borderRadius: 10,
    minWidth: 100,
    alignItems: 'center'
  }
});