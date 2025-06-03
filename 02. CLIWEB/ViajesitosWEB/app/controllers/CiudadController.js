import { XMLParser } from 'fast-xml-parser';

const parser = new XMLParser({ ignoreAttributes: false });
const endpoint = 'http://192.168.18.158:8094/ec.edu.monster.controlador/AeroCondorController.svc';

export const obtenerCiudades = async () => {
  const body = `
    <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
      <s:Body>
        <GetCiudades xmlns="http://tempuri.org/" />
      </s:Body>
    </s:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/GetCiudades'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);

  const result = json['s:Envelope']?.['s:Body']?.['GetCiudadesResponse']?.['GetCiudadesResult'];

  if (!result || !result['a:Ciudades']) return [];

  const ciudadesRaw = result['a:Ciudades'];

  const lista = Array.isArray(ciudadesRaw) ? ciudadesRaw : [ciudadesRaw];

  const mapeadas = lista.map(c => ({
    id: c['a:IdCiudad'],
    codigo: c['a:CodigoCiudad'],
    nombre: c['a:NombreCiudad']
  }));

  return mapeadas;
};



export const obtenerCiudadPorId = async (idCiudad) => {
  const body = `
    <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
      <s:Body>
        <ObtenerCiudadPorId xmlns="http://tempuri.org/">
          <id>${idCiudad}</id>
        </ObtenerCiudadPorId>
      </s:Body>
    </s:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/ObtenerCiudadPorId'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);

  return json['s:Envelope']['s:Body']['ObtenerCiudadPorIdResponse']['ObtenerCiudadPorIdResult'] || null;
};
