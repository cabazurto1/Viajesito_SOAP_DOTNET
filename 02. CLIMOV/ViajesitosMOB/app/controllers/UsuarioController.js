import { XMLParser } from 'fast-xml-parser';

const parser = new XMLParser({ ignoreAttributes: false });
const endpoint = 'http://192.168.18.158:8094/ec.edu.monster.controlador/AeroCondorController.svc';

// LOGIN
export const login = async (username, password) => {
  const body = `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
    <soapenv:Header/>
    <soapenv:Body>
      <tem:Login>
        <tem:username>${username}</tem:username>
        <tem:password>${password}</tem:password>
      </tem:Login>
    </soapenv:Body>
  </soapenv:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/Login'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);
  const result = json['s:Envelope']?.['s:Body']?.['LoginResponse']?.['LoginResult'];

  if (result && result['a:IdUsuario']) {
    return {
      IdUsuario: result['a:IdUsuario'],
      Nombre: result['a:Nombre'],
      Username: result['a:Username'],
      Password: result['a:Password'],
      Telefono: result['a:Telefono']
    };
  }

  return null;
};

// CREAR USUARIO
export const crearUsuario = async (usuario) => {
  const body = `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/" xmlns:ec="http://schemas.datacontract.org/2004/07/ec.edu.monster.modelo">
    <soapenv:Header/>
    <soapenv:Body>
      <tem:CrearUsuario>
        <tem:usuario>
          <ec:IdUsuario>${usuario.IdUsuario || 0}</ec:IdUsuario>
          <ec:Nombre>${usuario.Nombre}</ec:Nombre>
          <ec:Username>${usuario.Username}</ec:Username>
          <ec:Password>${usuario.Password}</ec:Password>
          <ec:Telefono>${usuario.Telefono}</ec:Telefono>
        </tem:usuario>
      </tem:CrearUsuario>
    </soapenv:Body>
  </soapenv:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/CrearUsuario'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);
  const result = json['s:Envelope']?.['s:Body']?.['CrearUsuarioResponse']?.['CrearUsuarioResult'];

  return result === 'true' || result === true;
};

// EDITAR USUARIO
export const editarUsuario = async (usuario) => {
  const body = `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/" xmlns:ec="http://schemas.datacontract.org/2004/07/ec.edu.monster.modelo">
    <soapenv:Header/>
    <soapenv:Body>
      <tem:EditarUsuario>
        <tem:usuario>
          <ec:IdUsuario>${usuario.IdUsuario}</ec:IdUsuario>
          <ec:Nombre>${usuario.Nombre}</ec:Nombre>
          <ec:Username>${usuario.Username}</ec:Username>
          <ec:Password>${usuario.Password}</ec:Password>
          <ec:Telefono>${usuario.Telefono}</ec:Telefono>
        </tem:usuario>
      </tem:EditarUsuario>
    </soapenv:Body>
  </soapenv:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/EditarUsuario'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);
  const result = json['s:Envelope']?.['s:Body']?.['EditarUsuarioResponse']?.['EditarUsuarioResult'];

  return result === 'true' || result === true;
};

// ELIMINAR USUARIO
export const eliminarUsuario = async (id) => {
  const body = `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
    <soapenv:Header/>
    <soapenv:Body>
      <tem:EliminarUsuario>
        <tem:id>${id}</tem:id>
      </tem:EliminarUsuario>
    </soapenv:Body>
  </soapenv:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/EliminarUsuario'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);
  const result = json['s:Envelope']?.['s:Body']?.['EliminarUsuarioResponse']?.['EliminarUsuarioResult'];

  return result === 'true' || result === true;
};

// OBTENER TODOS LOS USUARIOS
export const getUsuarios = async () => {
  const body = `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
    <soapenv:Header/>
    <soapenv:Body>
      <tem:GetUsuarios/>
    </soapenv:Body>
  </soapenv:Envelope>`;

  const response = await fetch(endpoint, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/xml;charset=UTF-8',
      SOAPAction: 'http://tempuri.org/IAeroCondorController/GetUsuarios'
    },
    body
  });

  const xml = await response.text();
  const json = parser.parse(xml);
  const usuarios = json['s:Envelope']?.['s:Body']?.['GetUsuariosResponse']?.['GetUsuariosResult']?.['a:Usuario'];

  if (!usuarios) return [];

  return Array.isArray(usuarios) ? usuarios : [usuarios];
};

// OBTENER BOLETOS POR USUARIO
export const obtenerBoletosPorUsuario = async (idUsuario) => {
  const body = `
  <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tem="http://tempuri.org/">
    <soapenv:Header/>
    <soapenv:Body>
      <tem:ObtenerBoletosPorUsuario>
        <tem:idUsuario>${idUsuario}</tem:idUsuario>
      </tem:ObtenerBoletosPorUsuario>
    </soapenv:Body>
  </soapenv:Envelope>`;

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
  const boletos = json['s:Envelope']?.['s:Body']?.['ObtenerBoletosPorUsuarioResponse']?.['ObtenerBoletosPorUsuarioResult']?.['a:Boleto'];

  if (!boletos) return [];

  return Array.isArray(boletos) ? boletos : [boletos];
};
