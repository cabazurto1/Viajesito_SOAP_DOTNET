import { XMLParser } from 'fast-xml-parser';

const parser = new XMLParser({ ignoreAttributes: false });
const endpoint = 'http://10.40.31.126:8094/ec.edu.monster.controlador/AeroCondorController.svc';

export const getFacturasPorUsuario = async (idUsuario) => {
  const body = `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
    <soapenv:Header/>
    <soapenv:Body>
      <tem:GetFacturasPorUsuario>
        <tem:idUsuario>${idUsuario}</tem:idUsuario>
      </tem:GetFacturasPorUsuario>
    </soapenv:Body>
  </soapenv:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/GetFacturasPorUsuario'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);

  const result = json['s:Envelope']?.['s:Body']?.['GetFacturasPorUsuarioResponse']?.['GetFacturasPorUsuarioResult'];
  const facturas = result?.['a:Facturas'];

  if (!facturas) return [];

  return Array.isArray(facturas) ? facturas : [facturas];
};



// OBTENER FACTURA POR ID
export const obtenerFacturaPorId = async (idFactura) => {
  const body = `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
    <soapenv:Header/>
    <soapenv:Body>
      <tem:ObtenerFacturaPorId>
        <tem:id>${idFactura}</tem:id>
      </tem:ObtenerFacturaPorId>
    </soapenv:Body>
  </soapenv:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/ObtenerFacturaPorId'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);
  const result = json['s:Envelope']?.['s:Body']?.['ObtenerFacturaPorIdResponse']?.['ObtenerFacturaPorIdResult'];

  return result && result['a:IdFactura'] ? result : null;
};

