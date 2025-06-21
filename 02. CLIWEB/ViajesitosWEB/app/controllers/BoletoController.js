import { XMLParser } from 'fast-xml-parser';

const parser = new XMLParser({ ignoreAttributes: false });
const endpoint = 'http://localhost:55325/ec.edu.monster.controlador/AeroCondorController.svc';

// 🔹 1. Obtener boletos por usuario
export const obtenerBoletosPorUsuario = async (idUsuario) => {
  const body = `
    <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
      <s:Body>
        <ObtenerBoletosPorUsuario xmlns="http://tempuri.org/">
          <idUsuario>${idUsuario}</idUsuario>
        </ObtenerBoletosPorUsuario>
      </s:Body>
    </s:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/ObtenerBoletosPorUsuario'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);

  try {
    const result = json['s:Envelope']['s:Body']['ObtenerBoletosPorUsuarioResponse']['ObtenerBoletosPorUsuarioResult'];

    if (!result) return [];

    let boletos = result['a:Boletos'];

    if (!Array.isArray(boletos)) {
      boletos = [boletos];
    }

    return boletos.map((b) => ({
      idBoleto: b['a:IdBoleto'],
      numeroBoleto: b['a:NumeroBoleto'],
      fechaCompra: b['a:FechaCompra'],
      precio: b['a:PrecioCompra'],
      idVuelo: b['a:IdVuelo'],
      idUsuario: b['a:IdUsuario']
    }));
  } catch (error) {
    console.error('❌ Error al procesar respuesta SOAP:', error);
    return [];
  }
};

// 🔹 2. Registrar compra de boletos (con o sin crédito)
export const registrarBoletos = async ({
  idUsuario,
  vuelos,
  esCredito = false,
  numeroCuotas = 0,
  tasaInteresAnual = 0
}) => {
  const vuelosXML = vuelos.map(
    ({ idVuelo, cantidad }) => `
      <ec:VueloCompra>
        <ec:IdVuelo>${idVuelo}</ec:IdVuelo>
        <ec:Cantidad>${cantidad}</ec:Cantidad>
      </ec:VueloCompra>
    `
  ).join('');

  const body = `
    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:tem="http://tempuri.org/"
                      xmlns:ec="http://schemas.datacontract.org/2004/07/ec.edu.monster.modelo">
      <soapenv:Header/>
      <soapenv:Body>
        <tem:Comprar>
          <tem:request>
            <ec:IdUsuario>${idUsuario}</ec:IdUsuario>
            <ec:Vuelos>${vuelosXML}</ec:Vuelos>
            <ec:EsCredito>${esCredito}</ec:EsCredito>
            <ec:NumeroCuotas>${numeroCuotas}</ec:NumeroCuotas>
            <ec:TasaInteresAnual>${tasaInteresAnual}</ec:TasaInteresAnual>
          </tem:request>
        </tem:Comprar>
      </soapenv:Body>
    </soapenv:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      'SOAPAction': 'http://tempuri.org/IAeroCondorController/Comprar'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);

  try {
    const result = json['s:Envelope']['s:Body']['ComprarResponse']['ComprarResult'];
    return typeof result === 'string' ? result.toLowerCase() === 'true' : !!result;
  } catch (error) {
    console.error('❌ Error al procesar compra múltiple:', error);
    return false;
  }
};

// 🔹 3. Obtener tabla de amortización por factura
export const obtenerAmortizacionPorFactura = async (idFactura) => {
  const body = `
    <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
      <s:Body>
        <ObtenerAmortizacionPorFactura xmlns="http://tempuri.org/">
          <idFactura>${idFactura}</idFactura>
        </ObtenerAmortizacionPorFactura>
      </s:Body>
    </s:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/ObtenerAmortizacionPorFactura'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);

  try {
    const result = json['s:Envelope']['s:Body']['ObtenerAmortizacionPorFacturaResponse']['ObtenerAmortizacionPorFacturaResult'];

    if (!result) return [];

    let amortizaciones = result['a:Amortizacion'];

    if (!Array.isArray(amortizaciones)) {
      amortizaciones = [amortizaciones];
    }

    return amortizaciones.map((a) => ({
      idAmortizacion: a['a:IdAmortizacion'],
      idFactura: a['a:IdFactura'],
      numeroCuota: a['a:NumeroCuota'],
      valorCuota: parseFloat(a['a:ValorCuota']),
      interesPagado: parseFloat(a['a:InteresPagado']),
      capitalPagado: parseFloat(a['a:CapitalPagado']),
      saldo: parseFloat(a['a:Saldo']),
    }));
  } catch (error) {
    console.error('❌ Error al procesar amortización:', error);
    return [];
  }
};
