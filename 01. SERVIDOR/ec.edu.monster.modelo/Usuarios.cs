using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

using System.Runtime.Serialization;

namespace ec.edu.monster.modelo
{
    [DataContract]
    public class Usuarios
    {
        [DataMember(Order = 1)]
        public int IdUsuario { get; set; }

        [DataMember(Order = 2)]
        public string Nombre { get; set; }

        [DataMember(Order = 3)]
        public string Username { get; set; }

        [DataMember(Order = 4)]
        public string Password { get; set; }

        [DataMember(Order = 5)]
        public string Telefono { get; set; }
    }
}
