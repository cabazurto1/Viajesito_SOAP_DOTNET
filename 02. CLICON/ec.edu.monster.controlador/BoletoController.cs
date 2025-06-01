using ServiceReference1;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace ec.edu.monster.controlador
{
    public class BoletoController
    {
        private readonly AeroCondorControllerClient _cliente;

        public BoletoController()
        {
            _cliente = new AeroCondorControllerClient();
        }

        public async Task<List<Boletos>> ObtenerBoletosAsync()
        {
            var boletos = await _cliente.GetBoletosAsync();
            return new List<Boletos>(boletos);
        }

        public async Task<Boletos> ObtenerPorIdAsync(int id)
        {
            return await _cliente.ObtenerBoletoPorIdAsync(id);
        }

        public async Task<List<Boletos>> ObtenerPorUsuarioAsync(int idUsuario)
        {
            var boletos = await _cliente.ObtenerBoletosPorUsuarioAsync(idUsuario);
            return new List<Boletos>(boletos);
        }

        public async Task<bool> ComprarAsync(CompraBoletoRequest request)
        {
            return await _cliente.ComprarAsync(request);
        }
    }
}
