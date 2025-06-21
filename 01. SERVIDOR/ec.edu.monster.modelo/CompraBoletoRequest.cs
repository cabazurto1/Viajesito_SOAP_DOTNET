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

        [DataMember(Order = 3)]
        public bool EsCredito { get; set; } // true = pago a crédito, false = pago directo (débito)

        [DataMember(Order = 4)]
        public int NumeroCuotas { get; set; } // Solo aplica si EsCredito == true

        [DataMember(Order = 5)]
        public double TasaInteresAnual { get; set; } // Ej: 16.5

        // Este campo se puede usar para retornar la tabla desde el servidor si lo deseas
        [DataMember(Order = 6)]
        public List<Amortizacion> TablaAmortizacion { get; set; }
    }
}
