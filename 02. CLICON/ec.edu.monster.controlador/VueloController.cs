using ServiceReference1;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace ec.edu.monster.controlador
{
    public class VueloController
    {
        private readonly AeroCondorControllerClient _cliente;

        public VueloController()
        {
            _cliente = new AeroCondorControllerClient();
        }

        public async Task<List<Vuelos>> ObtenerTodosAsync()
        {
            var vuelos = await _cliente.GetVuelosAsync();
            return new List<Vuelos>(vuelos);
        }

        public async Task<Vuelos> ObtenerPorIdAsync(int id)
        {
            return await _cliente.ObtenerVueloPorIdAsync(id);
        }

        public async Task<bool> CrearAsync(Vuelos vuelo)
        {
            return await _cliente.CrearVueloAsync(vuelo);
        }

        public async Task<bool> EditarAsync(Vuelos vuelo)
        {
            return await _cliente.EditarVueloAsync(vuelo);
        }

        public async Task<bool> EliminarAsync(int id)
        {
            return await _cliente.EliminarVueloAsync(id);
        }

        public async Task<List<Vuelos>> BuscarAsync(string origen, string destino, DateTime fecha)
        {
            var result = await _cliente.BuscarVuelosAsync(origen, destino, fecha);
            return new List<Vuelos>(result);
        }
    }
}
