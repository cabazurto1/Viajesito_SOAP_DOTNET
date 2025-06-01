using System;
using System.Collections.Generic;
using System.ServiceModel;
using ec.edu.monster.modelo;

namespace ec.edu.monster.controlador
{
    [ServiceContract]
    public interface IAeroCondorController
    {
        // ==== CIUDADES ====
        [OperationContract]
        List<Ciudades> GetCiudades();

        [OperationContract]
        Ciudades ObtenerCiudadPorId(int id);

        [OperationContract]
        bool CrearCiudad(Ciudades ciudad);

        [OperationContract]
        bool EditarCiudad(Ciudades ciudad);

        [OperationContract]
        bool EliminarCiudad(int id);

        [OperationContract]
        int ContarCiudades();

        [OperationContract]
        List<Ciudades> ListarCiudadesPorRango(int desde, int hasta);


        // ==== VUELOS ====
        [OperationContract]
        List<Vuelos> GetVuelos();

        [OperationContract]
        Vuelos ObtenerVueloPorId(int id);

        [OperationContract]
        bool CrearVuelo(Vuelos vuelo);

        [OperationContract]
        bool EditarVuelo(Vuelos vuelo);

        [OperationContract]
        bool EliminarVuelo(int id);

        [OperationContract]
        int ContarVuelos();

        [OperationContract]
        List<Vuelos> ListarVuelosPorRango(int desde, int hasta);

        [OperationContract]
        List<Vuelos> BuscarVuelos(string origen, string destino, DateTime fechaSalida);


        // ==== BOLETOS ====
        [OperationContract]
        List<Boletos> GetBoletos();

        [OperationContract]
        Boletos ObtenerBoletoPorId(int id);

        [OperationContract]
        bool CrearBoleto(Boletos boleto);

        [OperationContract]
        bool EditarBoleto(Boletos boleto);

        [OperationContract]
        bool EliminarBoleto(int id);

        [OperationContract]
        int ContarBoletos();

        [OperationContract]
        List<Boletos> ListarBoletosPorRango(int desde, int hasta);

        [OperationContract]
        bool Comprar(CompraBoletoRequest request);

        [OperationContract]
        List<Boletos> ObtenerBoletosPorUsuario(int idUsuario);


        // ==== USUARIOS ====
        [OperationContract]
        List<Usuarios> GetUsuarios();

        [OperationContract]
        Usuarios ObtenerUsuarioPorId(int id);

        [OperationContract]
        bool CrearUsuario(Usuarios usuario);

        [OperationContract]
        bool EditarUsuario(Usuarios usuario);

        [OperationContract]
        bool EliminarUsuario(int id);

        [OperationContract]
        int ContarUsuarios();

        [OperationContract]
        List<Usuarios> ListarUsuariosPorRango(int desde, int hasta);

        [OperationContract]
        Usuarios Login(string username, string password);
    }
}
