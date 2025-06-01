using ServiceReference1;
using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace ec.edu.monster.controlador
{
    public class UsuarioController
    {
        private readonly AeroCondorControllerClient _cliente;

        public UsuarioController()
        {
            _cliente = new AeroCondorControllerClient();
        }

        public async Task<List<Usuarios>> ObtenerUsuariosAsync()
        {
            var usuarios = await _cliente.GetUsuariosAsync();
            return new List<Usuarios>(usuarios);
        }

        public async Task<bool> CrearUsuarioAsync(Usuarios u)
        {
            return await _cliente.CrearUsuarioAsync(u);
        }

        public async Task<Usuarios> LoginAsync(string user, string pass)
        {
            return await _cliente.LoginAsync(user, pass);
        }

        public async Task<Usuarios> ObtenerPorIdAsync(int id)
        {
            return await _cliente.ObtenerUsuarioPorIdAsync(id);
        }

        public async Task<bool> EditarAsync(Usuarios u)
        {
            return await _cliente.EditarUsuarioAsync(u);
        }

        public async Task<bool> EliminarAsync(int id)
        {
            return await _cliente.EliminarUsuarioAsync(id);
        }
    }
}
