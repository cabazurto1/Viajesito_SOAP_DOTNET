import { XMLParser } from 'fast-xml-parser';

const parser = new XMLParser({ ignoreAttributes: false });
const endpoint = 'http://10.40.31.126:8094/ec.edu.monster.controlador/AeroCondorController.svc';

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
export const registrarBoleto = async ({ idVuelo, idUsuario, cantidad = 1 }) => {
  const body = `
    <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
      <s:Body>
        <Comprar xmlns="http://tempuri.org/">
          <request xmlns:a="http://schemas.datacontract.org/2004/07/ec.edu.monster.modelo">
            <a:IdVuelo>${idVuelo}</a:IdVuelo>
            <a:IdUsuario>${idUsuario}</a:IdUsuario>
            <a:Cantidad>${cantidad}</a:Cantidad>
          </request>
        </Comprar>
      </s:Body>
    </s:Envelope>`;

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
    console.error('❌ Error al procesar compra:', error);
    return false;
  }
};


