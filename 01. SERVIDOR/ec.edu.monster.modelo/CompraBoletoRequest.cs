using System;
using System.Collections.Generic;
using System.Runtime.Serialization;

namespace ec.edu.monster.modelo
{
    [DataContract]
    public class VueloCompra
    {
        [DataMember(Order = 1)]
        public int IdVuelo { get; set; }

        [DataMember(Order = 2)]
        public int Cantidad { get; set; }
    }

    [DataContract]
    public class CompraBoletoRequest
    {
        [DataMember(Order = 1)]
        public int IdUsuario { get; set; }

        [DataMember(Order = 2)]
        public List<VueloCompra> Vuelos { get; set; }
    }
}
