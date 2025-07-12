import React, { useState, useCallback } from 'react';
import {
  View,
  Text,
  FlatList,
  StyleSheet,
  ActivityIndicator,
  TouchableOpacity,
  useWindowDimensions,
  SafeAreaView,
  StatusBar,
  ScrollView
} from 'react-native';
import { useRouter, useLocalSearchParams, useFocusEffect } from 'expo-router';
import { obtenerBoletosPorUsuario } from '../controllers/BoletoController';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function MisBoletosView() {
  const router = useRouter();
  const { idUsuario: idParam } = useLocalSearchParams();
  const { width } = useWindowDimensions();
  const isMobile = width < 768;

  const [boletos, setBoletos] = useState([]);
  const [loading, setLoading] = useState(true);

  useFocusEffect(
    useCallback(() => {
      const cargarBoletos = async () => {
        setLoading(true);
        setBoletos([]);

        try {
          let idUsuarioActual = idParam;

          if (!idUsuarioActual) {
            idUsuarioActual = await AsyncStorage.getItem('idUsuario');
            if (!idUsuarioActual) throw new Error('Usuario no autenticado');
          }  else {
            const existente = await AsyncStorage.getItem('idUsuario');
            if (!existente || existente !== idUsuarioActual) {
              await AsyncStorage.setItem('idUsuario', idUsuarioActual);
            }
          }

          const datos = await obtenerBoletosPorUsuario(idUsuarioActual);

          if (Array.isArray(datos)) {
            const ordenados = datos.sort((a, b) =>
              new Date(b.fechaCompra) - new Date(a.fechaCompra)
            );
            setBoletos(ordenados);
          } else {
            setBoletos([]);
          }
        } catch (error) {
          console.error('❌ Error al cargar boletos:', error);
          setBoletos([]);
        } finally {
          setLoading(false);
        }
      };

      cargarBoletos();
    }, [idParam])
  );

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const renderItem = ({ item, index }) => {
      if (!item || !item.numeroBoleto) return null;
      return (
        <View style={[styles.card, { opacity: loading ? 0.5 : 1 }]}>
          <View style={styles.cardHeader}>
            <Text style={styles.cardTitle}>🎫 Boleto #{item.numeroBoleto}</Text>
            <View style={styles.badge}>
              <Text style={styles.badgeText}>#{index + 1}</Text>
            </View>
          </View>

          <View style={styles.cardContent}>
            <View style={styles.infoRow}>
              <Text style={styles.label}>✈️ ID Vuelo:</Text>
              <Text style={styles.value}>{item.idVuelo}</Text>
            </View>
            <View style={styles.infoRow}>
              <Text style={styles.label}>📅 Fecha de Compra:</Text>
              <Text style={styles.value}>{formatDate(item.fechaCompra)}</Text>
            </View>
            <View style={styles.infoRow}>
              <Text style={styles.label}>💰 Precio:</Text>
              <Text style={styles.priceValue}>${item.precio}</Text>
            </View>
          </View>
        </View>
      );
    };

  const renderTable = () => (
    <ScrollView
      horizontal
      contentContainerStyle={{ justifyContent: 'center', alignItems: 'center', width: '100%' }}
      style={{ flex: 1 }}
    >
      <ScrollView style={{ maxHeight: 500 }}>
        <View style={styles.tableWrapper}>
          <View style={styles.table}>
            <View style={[styles.tableRow, styles.tableHeader]}>
              <Text style={styles.tableHeaderCell}>#</Text>
              <Text style={styles.tableHeaderCell}>ID Vuelo</Text>
              <Text style={styles.tableHeaderCell}>Fecha de Compra</Text>
              <Text style={styles.tableHeaderCell}>Precio</Text>
            </View>
            {boletos.map((item, index) => (
              <View key={item.numeroBoleto || index} style={styles.tableRow}>
                <Text style={styles.tableCell}>{index + 1}</Text>
                <Text style={styles.tableCell}>{item.idVuelo}</Text>
                <Text style={styles.tableCell}>{formatDate(item.fechaCompra)}</Text>
                <Text style={styles.tableCell}>${item.precio}</Text>
              </View>
            ))}
          </View>
        </View>
      </ScrollView>
    </ScrollView>
  );


  const handleVolverMenu = async () => {
    let idUsuarioActual = idParam;
    if (!idUsuarioActual) {
      idUsuarioActual = await AsyncStorage.getItem('idUsuario');
    }

    if (idUsuarioActual) {
      router.replace({
        pathname: '/views/MenuView',
        params: { idUsuario: idUsuarioActual }
      });
    } else {
      router.replace('/');
    }
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
      <View style={styles.container}>
        <View style={styles.headerContainer}>
          <Text style={styles.header}>🎫 Mis Boletos</Text>
          <Text style={styles.subtitle}>
            {loading ? 'Cargando...' : `${boletos.length} boleto${boletos.length !== 1 ? 's' : ''} encontrado${boletos.length !== 1 ? 's' : ''}`}
          </Text>
        </View>

        <View style={styles.mainContent}>
          {loading ? (
            <View style={styles.loadingContainer}>
              <ActivityIndicator size="large" color="#4e88a9" />
              <Text style={styles.loadingText}>Cargando tus boletos...</Text>
            </View>
          ) : boletos.length === 0 ? (
            <View style={styles.emptyContainer}>
              <Text style={styles.emptyIcon}>✈️</Text>
              <Text style={styles.emptyTitle}>No tienes boletos</Text>
              <Text style={styles.emptyText}>
                Aún no has comprado ningún boleto.{'\n'}
                ¡Reserva tu próximo vuelo!
              </Text>
            </View>
          ) : (
            isMobile ? (
              <FlatList
                data={boletos}
                keyExtractor={(item, index) =>
                  item && item.numeroBoleto ? String(item.numeroBoleto) : `boleto-${index}`
                }
                renderItem={renderItem}
                showsVerticalScrollIndicator={true}
                contentContainerStyle={styles.listContainer}
                ItemSeparatorComponent={() => <View style={styles.separator} />}
              />
            ) : (
              renderTable()
            )
          )}
        </View>

        <View style={styles.buttonContainer}>
          <TouchableOpacity
            onPress={handleVolverMenu}
            style={styles.volverBtn}
            activeOpacity={0.8}
          >
            <Text style={styles.volverText}>← Volver al Menú</Text>
          </TouchableOpacity>
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  container: {
    flex: 1,
    backgroundColor: '#f8f9fa',
  },
  headerContainer: {
    backgroundColor: '#fff',
    paddingVertical: 24,
    paddingHorizontal: 20,
    borderBottomWidth: 1,
    borderBottomColor: '#e9ecef',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  header: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#35798e',
    textAlign: 'center',
    marginBottom: 4,
  },
  subtitle: {
    fontSize: 16,
    color: '#6c757d',
    textAlign: 'center',
    fontWeight: '500',
  },
  mainContent: {
    flex: 1,
    padding: 20,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingVertical: 60,
  },
  loadingText: {
    marginTop: 16,
    fontSize: 16,
    color: '#6c757d',
    textAlign: 'center',
  },
  emptyContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    paddingVertical: 60,
  },
  emptyIcon: {
    fontSize: 64,
    marginBottom: 16,
  },
  emptyTitle: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#35798e',
    marginBottom: 8,
    textAlign: 'center',
  },
  emptyText: {
    fontSize: 16,
    color: '#6c757d',
    textAlign: 'center',
    lineHeight: 22,
  },
  listContainer: {
    paddingBottom: 20,
  },
  separator: {
    height: 12,
  },
  card: {
    backgroundColor: '#fff',
    borderRadius: 16,
    marginBottom: 12,
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 8,
    elevation: 4,
    borderWidth: 1,
    borderColor: '#e9ecef',
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    backgroundColor: '#f8f9fa',
    paddingHorizontal: 20,
    paddingVertical: 16,
    borderTopLeftRadius: 16,
    borderTopRightRadius: 16,
    borderBottomWidth: 1,
    borderBottomColor: '#e9ecef',
  },
  cardTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    color: '#35798e',
  },
  badge: {
    backgroundColor: '#4e88a9',
    paddingHorizontal: 12,
    paddingVertical: 6,
    borderRadius: 20,
  },
  badgeText: {
    color: '#fff',
    fontSize: 12,
    fontWeight: 'bold',
  },
  cardContent: {
    padding: 20,
  },
  infoRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 12,
    paddingVertical: 8,
  },
  label: {
    fontSize: 16,
    color: '#495057',
    fontWeight: '500',
    flex: 1,
  },
  value: {
    fontSize: 16,
    color: '#212529',
    fontWeight: '600',
    flex: 1,
    textAlign: 'right',
  },
  priceValue: {
    fontSize: 18,
    color: '#27ae60',
    fontWeight: 'bold',
    flex: 1,
    textAlign: 'right',
  },
  tableWrapper: {
    backgroundColor: '#ffffff',
    borderRadius: 12,
    padding: 12,
    alignSelf: 'center',
    shadowColor: '#000',
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 2,
    marginVertical: 20,
    width: 700,
    marginLeft: 'auto',
    marginRight: 'auto', 
  },
  table: {
    minWidth: 700,
    borderWidth: 1,
    borderColor: '#dee2e6',
    borderRadius: 8,
    overflow: 'hidden',
  },
  tableRow: {
    flexDirection: 'row',
    paddingVertical: 10,
    paddingHorizontal: 6,
    borderBottomWidth: 1,
    borderBottomColor: '#dee2e6',
  },
  tableHeader: {
    backgroundColor: '#35798e',
  },
  tableHeaderCell: {
    flex: 1,
    fontWeight: 'bold',
    color: 'white',
    textAlign: 'center',
    padding: 10,
    fontSize: 16,
  },
  tableCell: {
    flex: 1,
    textAlign: 'center',
    color: '#212529',
    paddingVertical: 8,
    paddingHorizontal: 6,
    fontSize: 15,
  },
  volverBtn: {
    backgroundColor: '#4e88a9',
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 8,
    alignItems: 'center',
    alignSelf: 'center',
    maxWidth: 220,
  },
  buttonContainer: {
    backgroundColor: '#fff',
    padding: 16,
    borderTopWidth: 1,
    borderTopColor: '#e9ecef',
  },
  volverText: {
    color: '#fff',
    fontWeight: 'bold',
    fontSize: 18,
  },
});
