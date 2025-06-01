using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System;
using System.Runtime.Serialization;

namespace ec.edu.monster.modelo
{
    [DataContract]
    public class Vuelos
    {
        [DataMember(Order = 1)]
        public int IdVuelo { get; set; }

        [DataMember(Order = 2)]
        public string CodigoVuelo { get; set; }

        [DataMember(Order = 3)]
        public decimal Valor { get; set; }

        [DataMember(Order = 4)]
        public DateTime HoraSalida { get; set; }

        [DataMember(Order = 5)]
        public int Capacidad { get; set; }

        [DataMember(Order = 6)]
        public int Disponibles { get; set; }

        [DataMember(Order = 7)]
        public int IdCiudadOrigen { get; set; }

        [DataMember(Order = 8)]
        public int IdCiudadDestino { get; set; }
    }
}
