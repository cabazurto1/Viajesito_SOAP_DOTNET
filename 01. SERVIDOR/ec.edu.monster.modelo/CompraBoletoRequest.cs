using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

namespace ec.edu.monster.modelo
{
    [DataContract]
    public class CompraBoletoRequest
    {
        [DataMember(Order = 1)]
        public int IdVuelo { get; set; }

        [DataMember(Order = 2)]
        public int IdUsuario { get; set; }

        [DataMember(Order = 3)]
        public int Cantidad { get; set; }
    }
}
