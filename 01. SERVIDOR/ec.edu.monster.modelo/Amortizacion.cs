using System;
using System.Runtime.Serialization;

namespace ec.edu.monster.modelo
{
    [DataContract]
    public class Amortizacion
    {
        [DataMember(Order = 1)]
        public int IdAmortizacion { get; set; }

        [DataMember(Order = 2)]
        public int IdFactura { get; set; }

        [DataMember(Order = 3)]
        public int NumeroCuota { get; set; }

        [DataMember(Order = 4)]
        public decimal ValorCuota { get; set; }

        [DataMember(Order = 5)]
        public decimal InteresPagado { get; set; }

        [DataMember(Order = 6)]
        public decimal CapitalPagado { get; set; }

        [DataMember(Order = 7)]
        public decimal Saldo { get; set; }
    }
}
