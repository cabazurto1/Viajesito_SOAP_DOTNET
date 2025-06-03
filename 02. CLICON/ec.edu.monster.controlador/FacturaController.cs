using ServiceReference1;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace _02._CLICON.ec.edu.monster.controlador
{
    public class FacturaController
    {
        private readonly AeroCondorControllerClient _cliente;

        public FacturaController()
        {
            _cliente = new AeroCondorControllerClient();
        }

        public async Task<Facturas[]> GetFacturaporUsuario(int idUsuario)
        {
            return await _cliente.GetFacturasPorUsuarioAsync(idUsuario);
        }

        public async Task<Facturas> GetFactura(int idFactura)
        {
            return await _cliente.ObtenerFacturaPorIdAsync(idFactura);
        }
    }
}
