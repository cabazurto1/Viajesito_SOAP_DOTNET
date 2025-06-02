import { XMLParser } from 'fast-xml-parser';

const parser = new XMLParser({ ignoreAttributes: false });
const endpoint = 'http://10.40.31.126:8094/ec.edu.monster.controlador/AeroCondorController.svc';

export const obtenerVuelos = async () => {
  const body = `
    <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
      <s:Body>
        <GetVuelos xmlns="http://tempuri.org/" />
      </s:Body>
    </s:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/GetVuelos'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);
  const raw = json?.['s:Envelope']?.['s:Body']?.['GetVuelosResponse']?.['GetVuelosResult'];

  const vuelos = raw?.['a:Vuelos'];
  const lista = Array.isArray(vuelos) ? vuelos : vuelos ? [vuelos] : [];

  // Mapeo limpio de campos
  return lista.map(v => ({
    IdVuelo: v['a:IdVuelo'],
    CodigoVuelo: v['a:CodigoVuelo'],
    HoraSalida: v['a:HoraSalida'],
    Valor: v['a:Valor'],
    Capacidad: v['a:Capacidad'],
    Disponibles: v['a:Disponibles']
  }));
};

export const buscarVuelos = async (origen, destino, fechaSalida) => {
  const body = `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
    <soapenv:Header/>
    <soapenv:Body>
      <tem:BuscarVuelos>
        <tem:origen>${origen}</tem:origen>
        <tem:destino>${destino}</tem:destino>
        <tem:fechaSalida>${fechaSalida}</tem:fechaSalida>
      </tem:BuscarVuelos>
    </soapenv:Body>
  </soapenv:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/BuscarVuelos'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);

  const vuelosRaw = json?.['s:Envelope']?.['s:Body']?.['BuscarVuelosResponse']?.['BuscarVuelosResult']?.['a:Vuelos'];

  const lista = Array.isArray(vuelosRaw) ? vuelosRaw : vuelosRaw ? [vuelosRaw] : [];

  return lista.map(v => ({
    IdVuelo: v['a:IdVuelo'],
    CodigoVuelo: v['a:CodigoVuelo'],
    HoraSalida: v['a:HoraSalida'],
    Valor: v['a:Valor'],
    Capacidad: v['a:Capacidad'],
    Disponibles: v['a:Disponibles']
  }));
};
