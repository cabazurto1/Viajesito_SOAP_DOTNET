using System;
using System.Runtime.Serialization;

namespace ec.edu.monster.modelo
{
    [DataContract]
    public class Boletos
    {
        [DataMember(Order = 1)]
        public int IdBoleto { get; set; }

        [DataMember(Order = 2)]
        public string NumeroBoleto { get; set; }

        [DataMember(Order = 3)]
        public DateTime? FechaCompra { get; set; }

        [DataMember(Order = 4)]
        public decimal PrecioCompra { get; set; }

        [DataMember(Order = 5)]
        public int IdUsuario { get; set; }

        [DataMember(Order = 6)]
        public int IdVuelo { get; set; }
    }
}
