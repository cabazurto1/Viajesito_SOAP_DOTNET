using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

using System.Runtime.Serialization;

namespace ec.edu.monster.modelo
{
    [DataContract]
    public class Ciudades
    {
        [DataMember(Order = 1)]
        public int IdCiudad { get; set; }

        [DataMember(Order = 2)]
        public string CodigoCiudad { get; set; }

        [DataMember(Order = 3)]
        public string NombreCiudad { get; set; }
    }
}
