import React, { useState, useCallback } from 'react';
import {
  View,
  Text,
  FlatList,
  StyleSheet,
  ActivityIndicator,
  TouchableOpacity,
  SafeAreaView,
  StatusBar,
  Modal,
  ScrollView,
  useWindowDimensions
} from 'react-native';
import { useRouter, useLocalSearchParams, useFocusEffect } from 'expo-router';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { getFacturasPorUsuario, obtenerFacturaPorId } from '../controllers/FacturaController';
import { obtenerUsuarioPorId } from '../controllers/UsuarioController';
import { obtenerCiudades } from '../controllers/CiudadController';

export default function FacturasView() {
  const router = useRouter();
  const { idUsuario: idParam } = useLocalSearchParams();
  const { width, height } = useWindowDimensions();
  const isLargeScreen = width >= 768;
  const isTablet = width >= 768 && width < 1024;
  const isDesktop = width >= 1024;
  const [ciudades, setCiudades] = useState([]);
  const [facturas, setFacturas] = useState([]);
  const [loading, setLoading] = useState(true);
  const [facturaSeleccionada, setFacturaSeleccionada] = useState(null);
  const [usuarioSeleccionado, setUsuarioSeleccionado] = useState(null);
  const [modalVisible, setModalVisible] = useState(false);

  // Determinar número de columnas basado en el tamaño de pantalla
  const getNumColumns = () => {
    if (isDesktop) return 3;
    if (isTablet) return 2;
    return 1;
  };

  // Función corregida para obtener el nombre de la ciudad
  const getNombreCiudad = (codigo) => {
    if (!codigo || !ciudades.length) return 'N/A';
    const ciudad = ciudades.find(c => c.codigo === codigo);
    return ciudad ? ciudad.nombre : 'N/A';
  };

  // Función helper para extraer códigos de ciudad de los boletos
  const obtenerCodigoCiudad = (ciudadData) => {
  if (!ciudadData) return null;
  
  // Si ya es string, lo retornamos
  if (typeof ciudadData === 'string') return ciudadData;

  // Posibles estructuras
  return (
    ciudadData['a:CodigoCiudad'] ||
    ciudadData['CodigoCiudad'] ||
    ciudadData['codigo'] ||
    ciudadData['a:codigo'] ||
    Object.values(ciudadData).find(v => typeof v === 'string' && v.length === 3) || // Ej: 'UIO', 'GYE'
    null
  );
};


  // Key para forzar re-render cuando cambia el número de columnas
  const flatListKey = `${getNumColumns()}-${width}`;

  useFocusEffect(
    useCallback(() => {
      const cargarFacturas = async () => {
        setLoading(true);
        try {
          let idUsuarioActual = idParam;
          if (!idUsuarioActual) {
            idUsuarioActual = await AsyncStorage.getItem('idUsuario');
          }

          const [datos, listaCiudades] = await Promise.all([
            getFacturasPorUsuario(idUsuarioActual),
            obtenerCiudades()
          ]);

          console.log('Ciudades cargadas:', listaCiudades);

          const ordenadas = Array.isArray(datos)
            ? datos.sort((a, b) => new Date(b['a:FechaFactura']) - new Date(a['a:FechaFactura']))
            : [];

          setFacturas(ordenadas);
          setCiudades(listaCiudades);
        } catch (e) {
          console.error('Error al cargar facturas:', e);
        } finally {
          setLoading(false);
        }
      };
      cargarFacturas();
    }, [idParam])
  );

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  const formatDateShort = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    });
  };

  const handleFacturaPress = async (idFactura, idUsuario) => {
    try {
      const [detalle, usuario] = await Promise.all([
        obtenerFacturaPorId(Number(idFactura)),
        obtenerUsuarioPorId(Number(idUsuario))
      ]);
      if (detalle) {
        console.log('Detalle de factura:', detalle);
        setFacturaSeleccionada(detalle);
        setUsuarioSeleccionado(usuario);
        setModalVisible(true);
      } else {
        alert('No se pudo obtener el detalle de la factura.');
      }
    } catch (error) {
      console.error('Error al obtener detalle:', error);
    }
  };

  const getCardStyle = () => {
    const numColumns = getNumColumns();
    const padding = 16;
    const gap = 12;
    
    if (numColumns === 1) {
      return { width: '100%' };
    }
    
    const availableWidth = width - (padding * 2) - (gap * (numColumns - 1));
    const cardWidth = availableWidth / numColumns;
    
    return { width: cardWidth };
  };

  // Función para calcular totales correctamente
  const calcularTotales = (factura) => {
    const boletos = factura['a:BoletosRelacionados']?.['a:Boletos'] || [];
    const subtotal = boletos.reduce((sum, boleto) => {
      return sum + parseFloat(boleto['a:PrecioCompra'] || 0);
    }, 0);
    
    const descuento = 0; // Si hay descuentos, agregar lógica aquí
    const subtotalConDescuento = subtotal - descuento;
    const iva = subtotalConDescuento * 0.15;
    const total = subtotalConDescuento + iva;
    
    return {
      subtotal: subtotal.toFixed(2),
      descuento: descuento.toFixed(2),
      subtotalConDescuento: subtotalConDescuento.toFixed(2),
      iva: iva.toFixed(2),
      total: total.toFixed(2)
    };
  };

  const renderItem = ({ item, index }) => (
    <TouchableOpacity
      style={[
        styles.card,
        getCardStyle(),
        isLargeScreen && styles.cardLarge
      ]}
      onPress={() => handleFacturaPress(item['a:IdFactura'], item['a:IdUsuario'])}
    >
      <View style={styles.cardHeader}>
        <Text style={[
          styles.cardTitle,
          isLargeScreen && styles.cardTitleLarge
        ]}>
          🧾 Factura {item['a:NumeroFactura']}
        </Text>
        <View style={styles.badge}>
          <Text style={styles.badgeText}>#{index + 1}</Text>
        </View>
      </View>
      <View style={styles.cardContent}>
        <View style={styles.infoRow}>
          <Text style={[styles.label, isLargeScreen && styles.labelLarge]}>📅 Fecha:</Text>
          <Text style={[styles.value, isLargeScreen && styles.valueLarge]}>
            {formatDate(item['a:FechaFactura'])}
          </Text>
        </View>
        <View style={styles.infoRow}>
          <Text style={[styles.label, isLargeScreen && styles.labelLarge]}>💵 Total:</Text>
          <Text style={[styles.priceValue, isLargeScreen && styles.priceValueLarge]}>
            ${item['a:PrecioConIVA']}
          </Text>
        </View>
      </View>
    </TouchableOpacity>
  );

  const getModalWidth = () => {
    if (isDesktop) return '70%';
    if (isTablet) return '85%';
    return '95%';
  };

  const getModalMaxHeight = () => {
    return height * 0.9;
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar barStyle="dark-content" backgroundColor="#f8f9fa" />
      <View style={styles.container}>
        <Text style={[
          styles.header,
          isLargeScreen && styles.headerLarge
        ]}>
          🧾 Mis Facturas
        </Text>

        {loading ? (
          <View style={styles.loadingContainer}>
            <ActivityIndicator size="large" color="#4e88a9" />
            <Text style={styles.loadingText}>Cargando facturas...</Text>
          </View>
        ) : (
          <>
            {isLargeScreen ? (
              <View style={styles.tableMainContainer}>
                <ScrollView horizontal showsHorizontalScrollIndicator={true} style={styles.horizontalScroll}>
                  <View style={[styles.table, { minWidth: Math.max(900, width * 0.9) }]}>
                    <View style={[styles.tableHeader, { backgroundColor: '#35798e' }]}>
                      <Text style={[styles.tableColHeader, styles.tableColNum]}>#</Text>
                      <Text style={[styles.tableColHeader, styles.tableColDetail]}>Factura</Text>
                      <Text style={[styles.tableColHeader, styles.tableColCant]}>Fecha</Text>
                      <Text style={[styles.tableColHeader, styles.tableColPrice]}>Total</Text>
                    </View>
                    {facturas.map((item, index) => (
                      <TouchableOpacity
                        key={item['a:IdFactura'] || index}
                        onPress={() => handleFacturaPress(item['a:IdFactura'], item['a:IdUsuario'])}
                      >
                        <View style={[
                          styles.tableRow,
                          index % 2 === 0 && styles.tableRowEven
                        ]}>
                          <Text style={[styles.tableCol, styles.tableColNum]}>
                            {index + 1}
                          </Text>
                          <Text style={[styles.tableCol, styles.tableColDetail]}>
                            {item['a:NumeroFactura']}
                          </Text>
                          <Text style={[styles.tableCol, styles.tableColCant]}>
                            {formatDate(item['a:FechaFactura'])}
                          </Text>
                          <Text style={[styles.tableCol, styles.tableColPrice]}>
                            ${item['a:PrecioConIVA']}
                          </Text>
                        </View>
                      </TouchableOpacity>
                    ))}
                  </View>
                </ScrollView>
              </View>
            ) : (
              <FlatList
                key={flatListKey}
                data={facturas}
                numColumns={getNumColumns()}
                keyExtractor={(item, index) => item['a:IdFactura'] || index.toString()}
                renderItem={renderItem}
                columnWrapperStyle={getNumColumns() > 1 ? styles.row : null}
                contentContainerStyle={[
                  styles.listContainer,
                  isLargeScreen && styles.listContainerLarge
                ]}
                showsVerticalScrollIndicator={false}
              />
            )}
          </>
        )}

        <Modal 
          visible={modalVisible} 
          transparent 
          animationType="slide"
          onRequestClose={() => setModalVisible(false)}
        >
          <View style={styles.modalOverlay}>
            <View style={[
              styles.modalContent,
              { 
                width: getModalWidth(),
                maxHeight: getModalMaxHeight()
              }
            ]}>
              <ScrollView 
                contentContainerStyle={styles.modalScrollContent}
                showsVerticalScrollIndicator={true}
                bounces={false}
              >
                {facturaSeleccionada ? (
                  <>
                    <View style={styles.modalHeader}>
                      <Text style={[
                        styles.modalTitle,
                        isLargeScreen && styles.modalTitleLarge
                      ]}>
                        🧾 Factura {facturaSeleccionada['a:NumeroFactura']}
                      </Text>
                      <Text style={[
                        styles.modalSubtitle,
                        isLargeScreen && styles.modalSubtitleLarge
                      ]}>
                        Viajecitos S.A. | RUC: 1710708973001
                      </Text>
                      <Text style={[
                        styles.modalDate,
                        isLargeScreen && styles.modalDateLarge
                      ]}>
                        📅 Fecha de Emisión: {formatDate(facturaSeleccionada['a:FechaFactura'])}
                      </Text>
                    </View>

                    <View style={styles.section}>
                      <Text style={[
                        styles.sectionTitle,
                        isLargeScreen && styles.sectionTitleLarge
                      ]}>
                        👤 Datos del Cliente
                      </Text>
                      <View style={[
                        styles.clientInfo,
                        isLargeScreen && styles.clientInfoLarge
                      ]}>
                        <Text style={[styles.sectionItem, isLargeScreen && styles.sectionItemLarge]}>
                          <Text style={styles.bold}>Nombre:</Text> {usuarioSeleccionado?.Nombre || 'Desconocido'}
                        </Text>
                        <Text style={[styles.sectionItem, isLargeScreen && styles.sectionItemLarge]}>
                          <Text style={styles.bold}>Cédula:</Text> {usuarioSeleccionado?.Cedula || '-'}
                        </Text>
                        <Text style={[styles.sectionItem, isLargeScreen && styles.sectionItemLarge]}>
                          <Text style={styles.bold}>Teléfono:</Text> {usuarioSeleccionado?.Telefono || '-'}
                        </Text>
                        <Text style={[styles.sectionItem, isLargeScreen && styles.sectionItemLarge]}>
                          <Text style={styles.bold}>Correo:</Text> {usuarioSeleccionado?.Correo || '-'}
                        </Text>
                      </View>
                    </View>

                    <View style={styles.section}>
                      <Text style={[
                        styles.sectionTitle,
                        isLargeScreen && styles.sectionTitleLarge
                      ]}>
                        📄 Detalle de Boletos
                      </Text>
                      <View style={styles.centeredTableContainer}>
                        <ScrollView 
                          horizontal 
                          showsHorizontalScrollIndicator={true}
                          contentContainerStyle={styles.horizontalScrollContent}
                        >
                          <View style={styles.table}>
                            <View style={[
                              styles.tableHeader,
                              isLargeScreen && styles.tableHeaderLarge
                            ]}>
                              <Text style={[styles.tableColHeader, styles.tableColNumBoleto]}>Num. Boleto</Text>
                              <Text style={[styles.tableColHeader, styles.tableColDetalleBoleto]}>Detalle</Text>
                              <Text style={[styles.tableColHeader, styles.tableColCant]}>Cantidad</Text>
                              <Text style={[styles.tableColHeader, styles.tableColPrecioUnit]}>Precio Unitario</Text>
                            </View>
{facturaSeleccionada['a:BoletosRelacionados']?.['a:Boletos']?.map((b, idx) => {
  const mapaCiudades = {
    1: 'Quito',
    2: 'Guayaquil',
    3: 'Cuenca',
    4: 'Miami',
  };

  // Usamos los IDs directamente del vuelo si existen, o quemamos uno por defecto
  const idOrigen = b['a:IdVuelo']?.['a:IdCiudadOrigen'] || 1;
  const idDestino = b['a:IdVuelo']?.['a:IdCiudadDestino'] || 2;

  const ciudadOrigen = mapaCiudades[idOrigen] || 'Ciudad desconocida';
  const ciudadDestino = mapaCiudades[idDestino] || 'Ciudad desconocida';

  return (
    <View key={idx} style={[styles.tableRow, idx % 2 === 0 && styles.tableRowEven]}>
      <Text style={[styles.tableCol, styles.tableColNumBoleto]}>
        {b['a:NumeroBoleto']}
      </Text>
      <Text style={[styles.tableCol, styles.tableColDetalleBoleto]}>
        {`${ciudadOrigen} ➝ ${ciudadDestino} - ${formatDateShort(b['a:FechaCompra'])}`}
      </Text>
      <Text style={[styles.tableCol, styles.tableColCant]}>1</Text>
      <Text style={[styles.tableCol, styles.tableColPrecioUnit]}>
        ${parseFloat(b['a:PrecioCompra']).toFixed(2)}
      </Text>
    </View>
  );
})}




                          </View>
                        </ScrollView>
                      </View>
                    </View>

                    <View style={[styles.section, styles.totalSection]}>
                      {(() => {
                        const totales = calcularTotales(facturaSeleccionada);
                        return (
                          <>
                            <View style={styles.totalRow}>
                              <Text style={[styles.totalLabel, isLargeScreen && styles.totalLabelLarge]}>
                                🔢 Subtotal:
                              </Text>
                              <Text style={[styles.totalValue, isLargeScreen && styles.totalValueLarge]}>
                                ${totales.subtotal}
                              </Text>
                            </View>
                            <View style={styles.totalRow}>
                              <Text style={[styles.totalLabel, isLargeScreen && styles.totalLabelLarge]}>
                                🎯 Descuento:
                              </Text>
                              <Text style={[styles.totalValue, isLargeScreen && styles.totalValueLarge]}>
                                ${totales.descuento}
                              </Text>
                            </View>
                            <View style={styles.totalRow}>
                              <Text style={[styles.totalLabel, isLargeScreen && styles.totalLabelLarge]}>
                                💰 Subtotal con IVA (15%):
                              </Text>
                              <Text style={[styles.totalValue, isLargeScreen && styles.totalValueLarge]}>
                                ${totales.iva}
                              </Text>
                            </View>
                            <View style={[styles.totalRow, styles.totalRowFinal]}>
                              <Text style={[styles.totalLabel, styles.totalLabelFinal, isLargeScreen && styles.totalLabelFinalLarge]}>
                                💵 Total:
                              </Text>
                              <Text style={[styles.totalValue, styles.totalValueFinal, isLargeScreen && styles.totalValueFinalLarge]}>
                                ${totales.total}
                              </Text>
                            </View>
                          </>
                        );
                      })()}
                    </View>
                  </>
                ) : (
                  <View style={styles.loadingModal}>
                    <ActivityIndicator size="large" color="#4e88a9" />
                    <Text style={styles.loadingModalText}>Cargando detalles...</Text>
                  </View>
                )}
              </ScrollView>
              
              <TouchableOpacity 
                onPress={() => setModalVisible(false)} 
                style={[
                  styles.closeBtn,
                  isLargeScreen && styles.closeBtnLarge
                ]}
              >
                <Text style={[
                  styles.closeText,
                  isLargeScreen && styles.closeTextLarge
                ]}>
                  Cerrar
                </Text>
              </TouchableOpacity>
            </View>
          </View>
        </Modal>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: { 
    flex: 1, 
    backgroundColor: '#f8f9fa' 
  },
  container: { 
    flex: 1 
  },
  header: {
    fontSize: 28,
    fontWeight: 'bold',
    color: '#35798e',
    textAlign: 'center',
    marginVertical: 20,
    paddingHorizontal: 16
  },
  headerLarge: {
    fontSize: 36,
    marginVertical: 30
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    gap: 16
  },
  loadingText: {
    fontSize: 16,
    color: '#6c757d',
    textAlign: 'center'
  },
  listContainer: {
    padding: 16,
    gap: 12
  },
  listContainerLarge: {
    padding: 24,
    gap: 16
  },
  row: {
    justifyContent: 'space-between',
    gap: 12
  },
  tableMainContainer: {
    flex: 1,
    paddingHorizontal: 16,
    alignItems: 'center'
  },
  horizontalScroll: {
    width: '100%'
  },
  card: {
    backgroundColor: '#fff',
    borderRadius: 12,
    padding: 16,
    borderWidth: 1,
    borderColor: '#e0e0e0',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.1,
    shadowRadius: 3.84,
    elevation: 5,
  },
  cardLarge: {
    padding: 20,
    borderRadius: 16
  },
  cardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 12
  },
  cardTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#35798e',
    flex: 1
  },
  cardTitleLarge: {
    fontSize: 18
  },
  badge: {
    backgroundColor: '#4e88a9',
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 10,
    minWidth: 30,
    alignItems: 'center'
  },
  badgeText: {
    color: '#fff',
    fontWeight: 'bold',
    fontSize: 12
  },
  cardContent: {
    gap: 8
  },
  infoRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center'
  },
  label: { 
    fontSize: 14, 
    color: '#555',
    flex: 1
  },
  labelLarge: {
    fontSize: 16
  },
  value: { 
    fontSize: 14, 
    fontWeight: '600', 
    color: '#000',
    textAlign: 'right',
    flex: 1
  },
  valueLarge: {
    fontSize: 16
  },
  priceValue: { 
    fontSize: 16, 
    fontWeight: 'bold', 
    color: '#27ae60',
    textAlign: 'right',
    flex: 1
  },
  priceValueLarge: {
    fontSize: 18
  },
  modalOverlay: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'rgba(0,0,0,0.5)'
  },
  modalContent: {
    backgroundColor: '#fff',
    borderRadius: 16,
    maxWidth: 1000,
    margin: 20
  },
  modalScrollContent: {
    padding: 20,
    paddingBottom: 0
  },
  modalHeader: {
    alignItems: 'center',
    marginBottom: 20,
    paddingBottom: 15,
    borderBottomWidth: 1,
    borderBottomColor: '#e9ecef'
  },
  modalTitle: { 
    fontSize: 22, 
    fontWeight: 'bold', 
    color: '#35798e',
    textAlign: 'center'
  },
  modalTitleLarge: {
    fontSize: 28
  },
  modalSubtitle: {
    fontSize: 14,
    color: '#6c757d',
    marginTop: 5,
    textAlign: 'center'
  },
  modalSubtitleLarge: {
    fontSize: 16
  },
  modalDate: {
    fontSize: 16,
    color: '#495057',
    marginTop: 8,
    textAlign: 'center',
    fontWeight: '600'
  },
  modalDateLarge: {
    fontSize: 18
  },
  section: {
    marginBottom: 24
  },
  sectionTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 12,
    color: '#35798e'
  },
  sectionTitleLarge: {
    fontSize: 22,
    marginBottom: 16
  },
  clientInfo: {
    backgroundColor: '#f8f9fa',
    padding: 16,
    borderRadius: 8,
    gap: 8
  },
  clientInfoLarge: {
    padding: 20,
    borderRadius: 10,
    gap: 10
  },
  sectionItem: {
    fontSize: 14,
    color: '#333',
    lineHeight: 20
  },
  sectionItemLarge: {
    fontSize: 16,
    lineHeight: 24
  },
  bold: {
    fontWeight: 'bold',
    color: '#495057'
  },
  centeredTableContainer: {
    alignItems: 'center',
    backgroundColor: '#f8f9fa',
    borderRadius: 8,
    padding: 8
  },
  horizontalScrollContent: {
    alignItems: 'center'
  },
  table: {
    minWidth: 600
  },
  tableHeader: {
    flexDirection: 'row',
    backgroundColor: '#35798e',
    borderRadius: 6,
    padding: 12,
    marginBottom: 2
  },
  tableHeaderLarge: {
    padding: 16
  },
  tableColHeader: {
    color: '#fff',
    fontWeight: 'bold',
    fontSize: 14,
    textAlign: 'center'
  },
  tableRow: {
    flexDirection: 'row',
    padding: 12,
    borderRadius: 4,
    marginBottom: 1
  },
  tableRowEven: {
    backgroundColor: '#fff'
  },
  tableCol: {
    fontSize: 13,
    color: '#212529',
    textAlign: 'center'
  },
  tableColNum: {
    width: 60
  },
  tableColDetail: {
    width: 200,
    textAlign: 'left',
    paddingLeft: 8
  },
  tableColCant: {
    width: 60
  },
  tableColPrice: {
    width: 80,
    fontWeight: '600',
    color: '#27ae60'
  },
  tableColNumBoleto: {
    width: 120,
    textAlign: 'center'
  },
  tableColDetalleBoleto: {
    width: 250,
    textAlign: 'left',
    paddingLeft: 8
  },
  tableColPrecioUnit: {
    width: 100,
    fontWeight: '600',
    color: '#27ae60',
    textAlign: 'center'
  },
  totalSection: {
    backgroundColor: '#f8f9fa',
    padding: 16,
    borderRadius: 8,
    marginTop: 8
  },
  totalRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8
  },
  totalRowFinal: {
    marginTop: 8,
    paddingTop: 12,
    borderTopWidth: 2,
    borderTopColor: '#35798e'
  },
  totalLabel: {
    fontSize: 16,
    color: '#495057'
  },
  totalLabelLarge: {
    fontSize: 18
  },
  totalLabelFinal: {
    fontWeight: 'bold',
    color: '#35798e'
  },
  totalLabelFinalLarge: {
    fontSize: 20
  },
  totalValue: {
    fontSize: 16,
    fontWeight: '600',
    color: '#27ae60'
  },
  totalValueLarge: {
    fontSize: 18
  },
  totalValueFinal: {
    fontSize: 18,
    fontWeight: 'bold'
  },
  totalValueFinalLarge: {
    fontSize: 22
  },
  loadingModal: {
    alignItems: 'center',
    padding: 40,
    gap: 16
  },
  loadingModalText: {
    fontSize: 16,
    color: '#6c757d'
  },
  closeBtn: {
    backgroundColor: '#4e88a9',
    paddingVertical: 12,
    paddingHorizontal: 24,
    borderRadius: 8,
    margin: 20,
    alignItems: 'center'
  },
  closeBtnLarge: {
    paddingVertical: 16,
    paddingHorizontal: 32,
    borderRadius: 10
  },
  closeText: { 
    color: '#fff', 
    fontWeight: 'bold', 
    fontSize: 16 
  },
  closeTextLarge: {
    fontSize: 18
  }
});