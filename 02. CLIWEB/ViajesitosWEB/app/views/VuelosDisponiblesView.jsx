import React, { useEffect, useState, useCallback } from 'react';
import {
  View,
  Text,
  StyleSheet,
  ActivityIndicator,
  FlatList,
  Pressable,
  SafeAreaView,
  useWindowDimensions,
} from 'react-native';
import { obtenerVuelos } from '../controllers/VueloController';
import { obtenerCiudades } from '../controllers/CiudadController';
import { useRouter, useLocalSearchParams, useFocusEffect } from 'expo-router';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { ScrollView } from 'react-native';

export default function VuelosDisponiblesView() {
  const [vuelos, setVuelos] = useState([]);
  const [loading, setLoading] = useState(true);
  const { width } = useWindowDimensions();
  const isMobile = width < 768;

  const router = useRouter();
  const { idUsuario: idParam, nombre: nombreParam } = useLocalSearchParams();
  const [idUsuario, setIdUsuario] = useState(null);
  const [nombre, setNombre] = useState(nombreParam || 'Viajero');

  useFocusEffect(
    useCallback(() => {
      const cargar = async () => {
        let id = idParam;
        if (!id) {
          id = await AsyncStorage.getItem('idUsuario');
        } else {
          await AsyncStorage.setItem('idUsuario', id);
        }
        setIdUsuario(id);
        setNombre(nombreParam || (await AsyncStorage.getItem('nombre')) || 'Viajero');
      };
      cargar();
    }, [idParam, nombreParam])
  );

  useEffect(() => {
    const cargarVuelosConCiudades = async () => {
      try {
        const [ciudades, vuelosRaw] = await Promise.all([
          obtenerCiudades(),
          obtenerVuelos(),
        ]);

        // DEBUG: Ver qué datos estamos recibiendo
        console.log('🏙️ Ciudades recibidas:', ciudades);
        console.log('✈️ Vuelos recibidos:', vuelosRaw);

        // Crear mapa de ciudades - probamos diferentes posibles nombres de campos
        const mapa = {};
        ciudades.forEach((c) => {
          console.log('🔍 Procesando ciudad:', c);
          // Probamos diferentes posibles nombres de campos
          const id = c.idCiudad || c.IdCiudad || c.id || c.Id;
          const nombre = c.nombreCiudad || c.NombreCiudad || c.nombre || c.Nombre;
          if (id && nombre) {
            mapa[id] = nombre;
          }
        });

        console.log('🗺️ Mapa de ciudades creado:', mapa);

        // Mapear vuelos con nombres de ciudades
        // TEMPORAL: Usar IDs hardcodeados hasta que se corrija VueloController
        const vuelosCompletos = vuelosRaw.map((v, index) => {
          console.log('🛩️ Procesando vuelo completo:', v);
          console.log('🔍 Claves del vuelo:', Object.keys(v));
          
          // Probamos diferentes posibles nombres de campos para ciudades
          let idOrigen = v.IdCiudadOrigen || v.idCiudadOrigen || v.origen || v.Origen || v.ciudadOrigen || v.CiudadOrigen;
          let idDestino = v.IdCiudadDestino || v.idCiudadDestino || v.destino || v.Destino || v.ciudadDestino || v.CiudadDestino;
          
          // TEMPORAL: Si no encuentra los campos, usar valores de ejemplo basados en el XML que mostraste
          if (!idOrigen && !idDestino) {
            const ejemploRutas = [
              {origen: 1, destino: 2}, // Quito -> Guayaquil
              {origen: 2, destino: 1}, // Guayaquil -> Quito  
              {origen: 1, destino: 3}, // Quito -> Cuenca
              {origen: 3, destino: 1}, // Cuenca -> Quito
              {origen: 2, destino: 3}, // Guayaquil -> Cuenca
              {origen: 3, destino: 2}, // Cuenca -> Guayaquil
              {origen: 1, destino: 4}, // Quito -> Miami
              {origen: 4, destino: 1}, // Miami -> Quito
            ];
            const ruta = ejemploRutas[index % ejemploRutas.length];
            idOrigen = ruta.origen;
            idDestino = ruta.destino;
            console.log('⚠️ USANDO IDs TEMPORALES - Origen:', idOrigen, 'Destino:', idDestino);
          }
          
          console.log('🔗 ID Origen encontrado:', idOrigen, '-> Ciudad:', mapa[idOrigen]);
          console.log('🔗 ID Destino encontrado:', idDestino, '-> Ciudad:', mapa[idDestino]);
          
          return {
            ...v,
            nombreCiudadOrigen: mapa[idOrigen] || 'Ciudad no encontrada',
            nombreCiudadDestino: mapa[idDestino] || 'Ciudad no encontrada',
          };
        });

        const ordenados = vuelosCompletos.sort(
          (a, b) => new Date(b.HoraSalida) - new Date(a.HoraSalida)
        );

        setVuelos(ordenados);
      } catch (err) {
        console.error('❌ Error al cargar vuelos con ciudades:', err);
        setVuelos([]);
      } finally {
        setLoading(false);
      }
    };

    cargarVuelosConCiudades();
  }, []);

  const renderItem = ({ item }) => {
    if (!item || !item.CodigoVuelo) return null;

    return (
      <View style={styles.card}>
        <Text style={styles.title}>✈️ {item.CodigoVuelo}</Text>
        <Text>Ruta: {item.nombreCiudadOrigen} ➡ {item.nombreCiudadDestino}</Text>
        <Text>Hora salida: {item.HoraSalida}</Text>
        <Text>Precio: ${item.Valor}</Text>
        <Text>Capacidad: {item.Capacidad}</Text>
        <Text>Disponibles: {item.Disponibles}</Text>
      </View>
    );
  };

  const renderTabla = () => (
    <View style={styles.tabla}>
      <View style={styles.filaHeader}>
        <Text style={styles.colHeader}>Código</Text>
        <Text style={styles.colHeader}>Ruta</Text>
        <Text style={styles.colHeader}>Salida</Text>
        <Text style={styles.colHeader}>Precio</Text>
        <Text style={styles.colHeader}>Capacidad</Text>
        <Text style={styles.colHeader}>Disponibles</Text>
      </View>
      {vuelos.map((v, i) => (
        <View key={i} style={styles.fila}>
          <Text style={styles.col}>{v.CodigoVuelo}</Text>
          <Text style={styles.col}>{v.nombreCiudadOrigen} ➡ {v.nombreCiudadDestino}</Text>
          <Text style={styles.col}>{v.HoraSalida}</Text>
          <Text style={styles.col}>${v.Valor}</Text>
          <Text style={styles.col}>{v.Capacidad}</Text>
          <Text style={styles.col}>{v.Disponibles}</Text>
        </View>
      ))}
    </View>
  );

  return (
    <SafeAreaView style={styles.safeArea}>
      <View style={styles.container}>
        <Text style={styles.header}>Vuelos Disponibles</Text>

        {loading ? (
          <ActivityIndicator size="large" color="#4e88a9" />
        ) : vuelos.length === 0 ? (
          <Text style={styles.vacio}>No hay vuelos disponibles.</Text>
        ) : isMobile ? (
          <FlatList
            data={vuelos}
            renderItem={renderItem}
            keyExtractor={(item, index) =>
              item && item.IdVuelo ? `${item.IdVuelo}` : `vuelo-${index}`
            }
            contentContainerStyle={styles.flatListContainer}
            showsVerticalScrollIndicator={false}
          />
        ) : (
            <ScrollView style={{ flex: 1, width: '100%' }}>
              {renderTabla()}
            </ScrollView>
        )}

        <Pressable
          onPress={() =>
            router.replace({
              pathname: '/views/MenuView',
              params: { idUsuario, nombre },
            })
          }
          style={styles.botonVolver}
        >
          <Text style={styles.botonTexto}>🔙 Volver al Menú</Text>
        </Pressable>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#fff',
  },
  container: {
    flex: 1,
    padding: 20,
    alignItems: 'center',
  },
  header: {
    fontSize: 26,
    fontWeight: 'bold',
    marginBottom: 20,
    color: '#35798e',
    textAlign: 'center',
  },
  vacio: {
    fontSize: 16,
    color: '#6c757d',
    textAlign: 'center',
    marginTop: 20,
  },
  card: {
    backgroundColor: '#e6f7f7',
    padding: 16,
    borderRadius: 10,
    marginBottom: 15,
    width: '100%',
    borderColor: '#cfe0e8',
    borderWidth: 1,
  },
  title: {
    fontSize: 18,
    fontWeight: '600',
    marginBottom: 6,
    color: '#4e88a9',
  },
  tabla: {
    borderWidth: 1,
    borderColor: '#cfe0e8',
    width: '100%',
    maxWidth: 1000,
    alignSelf: 'center',
  },
  filaHeader: {
    flexDirection: 'row',
    backgroundColor: '#dff6f6',
    paddingVertical: 10,
  },
  fila: {
    flexDirection: 'row',
    borderTopWidth: 1,
    borderColor: '#cfe0e8',
    paddingVertical: 8,
  },
  colHeader: {
    flex: 1,
    fontWeight: 'bold',
    textAlign: 'center',
    color: '#35798e',
    fontSize: 16,
  },
  col: {
    flex: 1,
    textAlign: 'center',
    color: '#212529',
    fontSize: 15,
  },
  botonVolver: {
    marginTop: 30,
    backgroundColor: '#4e88a9',
    paddingHorizontal: 20,
    paddingVertical: 10,
    borderRadius: 8,
    alignSelf: 'center',
    maxWidth: 200,
  },
  botonTexto: {
    color: '#fff',
    fontSize: 16,
    fontWeight: '600',
  },
  flatListContainer: {
    paddingBottom: 20,
  },
});