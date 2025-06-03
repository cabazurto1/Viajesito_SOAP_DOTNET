using System;
using System.Collections.Generic;
using System.Runtime.Serialization;

namespace ec.edu.monster.modelo
{
    [DataContract]
    public class Facturas
    {
        [DataMember(Order = 1)]
        public int IdFactura { get; set; }

        [DataMember(Order = 2)]
        public string NumeroFactura { get; set; }

        [DataMember(Order = 3)]
        public int IdUsuario { get; set; }

        [DataMember(Order = 4)]
        public decimal PrecioSinIVA { get; set; }

        [DataMember(Order = 5)]
        public decimal PrecioConIVA { get; set; }

        [DataMember(Order = 6)]
        public DateTime FechaFactura { get; set; }

        // Opcional: agregar una lista de boletos relacionados a esta factura
        [DataMember(Order = 7)]
        public List<Boletos> BoletosRelacionados { get; set; }
    }
}
