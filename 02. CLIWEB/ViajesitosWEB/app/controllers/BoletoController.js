import { XMLParser } from 'fast-xml-parser';

const parser = new XMLParser({ ignoreAttributes: false });
const endpoint = 'http://192.168.18.158:8094/ec.edu.monster.controlador/AeroCondorController.svc';

// Obtener boletos de un usuario específico
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
    const result =
      json['s:Envelope']['s:Body']['ObtenerBoletosPorUsuarioResponse']['ObtenerBoletosPorUsuarioResult'];

    if (!result) return [];

    let boletos = result['a:Boletos'];

    // Si es un solo boleto, lo convertimos a array
    if (!Array.isArray(boletos)) {
      boletos = [boletos];
    }

    // Mapear para dejar solo los campos necesarios
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

// Registrar compra de boletos

export const registrarBoletos = async ({ idUsuario, vuelos }) => {
  // vuelos debe ser un array de objetos: [{ idVuelo: 1, cantidad: 2 }, { idVuelo: 2, cantidad: 3 }]
  
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
            <ec:Vuelos>
              ${vuelosXML}
            </ec:Vuelos>
          </tem:request>
        </tem:Comprar>
      </soapenv:Body>
    </soapenv:Envelope>
  `;

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


