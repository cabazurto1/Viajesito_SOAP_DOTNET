package ec.edu.viajecito.controller;

import ec.edu.viajecito.client.UsuariosClient;
import ec.edu.viajecito.model.Usuario;
import jakarta.ws.rs.ClientErrorException;

public class UsuariosController {
    public boolean crearUsuario(Usuario usuario) {
        UsuariosClient client = new UsuariosClient();
        try {
            client.create(usuario);
            return true;
        } catch (ClientErrorException e) {
            return false;
        } finally {
            client.close();
        }
    }

    public boolean actualizarUsuario(String id, Usuario usuario) {
        UsuariosClient client = new UsuariosClient();
        try {
            client.edit(usuario, id);
            return true;
        } catch (ClientErrorException e) {
            return false;
        } finally {
            client.close();
        }
    }

    
    public Usuario login(String username, String password) {
        UsuariosClient client = new UsuariosClient();
        try {
            Usuario usuario = client.login(Usuario.class, password, username);
            return usuario;
        } finally {
            client.close();
        }
    }
}
