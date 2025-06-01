using ServiceReference1;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace ec.edu.monster.controlador
{
    public class CiudadController
    {
        private readonly AeroCondorControllerClient _cliente;

        public CiudadController()
        {
            _cliente = new AeroCondorControllerClient();
        }

        public async Task<List<Ciudades>> ObtenerCiudadesAsync()
        {
            var ciudades = await _cliente.GetCiudadesAsync();
            return new List<Ciudades>(ciudades);
        }

        public async Task<Ciudades> ObtenerPorIdAsync(int id)
        {
            return await _cliente.ObtenerCiudadPorIdAsync(id);
        }

        public async Task<bool> CrearAsync(Ciudades ciudad)
        {
            return await _cliente.CrearCiudadAsync(ciudad);
        }

        public async Task<bool> EditarAsync(Ciudades ciudad)
        {
            return await _cliente.EditarCiudadAsync(ciudad);
        }

        public async Task<bool> EliminarAsync(int id)
        {
            return await _cliente.EliminarCiudadAsync(id);
        }
    }
}
