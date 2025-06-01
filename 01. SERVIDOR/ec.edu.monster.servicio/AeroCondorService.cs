using System;
using System.Collections.Generic;
using System.ServiceModel;
using ec.edu.monster.modelo;

namespace ec.edu.monster.servicio
{
    [ServiceContract]
    public interface IAeroCondorService
    {
        [OperationContract]
        List<Ciudades> ListarCiudades();

        [OperationContract]
        List<Vuelos> ListarVuelos();

        [OperationContract]
        List<Usuarios> ListarUsuarios();

        [OperationContract]
        List<Boletos> ListarBoletos();

        [OperationContract]
        bool ComprarBoletos(CompraBoletoRequest request);

        [OperationContract]
        Usuarios Login(string username, string password);

        [OperationContract]
        List<Vuelos> BuscarVuelos(string origen, string destino, DateTime fechaSalida);
    }
}
