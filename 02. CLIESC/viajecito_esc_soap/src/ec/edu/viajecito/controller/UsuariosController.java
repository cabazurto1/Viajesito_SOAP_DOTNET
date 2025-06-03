package ec.edu.viajecito.controller;

import ec.edu.viajecito.client.AeroCondorClient;
import ec.edu.viajecito.model.Usuario;
import ec.edu.viajecito.client.Usuarios;

import javax.xml.namespace.QName;
import javax.xml.bind.JAXBElement;

public class UsuariosController {

    private static final String NAMESPACE = "http://schemas.datacontract.org/2004/07/ec.edu.monster.modelo";

    public boolean crearUsuario(Usuario usuario) {
        Usuarios soapUsuario = new Usuarios();

        soapUsuario.setNombre(new JAXBElement<>(
            new QName(NAMESPACE, "Nombre"), String.class, usuario.getNombre()));
        soapUsuario.setUsername(new JAXBElement<>(
            new QName(NAMESPACE, "Username"), String.class, usuario.getUsername()));
        soapUsuario.setPassword(new JAXBElement<>(
            new QName(NAMESPACE, "Password"), String.class, usuario.getPassword()));
        soapUsuario.setTelefono(new JAXBElement<>(
            new QName(NAMESPACE, "Telefono"), String.class, usuario.getTelefono()));
        soapUsuario.setCedula(new JAXBElement<>(
            new QName(NAMESPACE, "Cedula"), String.class, usuario.getCedula()));
        soapUsuario.setCorreo(new JAXBElement<>(
            new QName(NAMESPACE, "Correo"), String.class, usuario.getCorreo()));

        return AeroCondorClient.crearUsuario(soapUsuario);
    }


    public Usuario login(String username, String password) {
        Usuarios soapUsuario = AeroCondorClient.login(username, password);
        if (soapUsuario == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(soapUsuario.getIdUsuario());

        if (soapUsuario.getNombre() != null) {
            usuario.setNombre(soapUsuario.getNombre().getValue());
        }
        if (soapUsuario.getUsername() != null) {
            usuario.setUsername(soapUsuario.getUsername().getValue());
        }
        if (soapUsuario.getPassword() != null) {
            usuario.setPassword(soapUsuario.getPassword().getValue());
        }
        if (soapUsuario.getTelefono() != null) {
            usuario.setTelefono(soapUsuario.getTelefono().getValue());
        }
        if (soapUsuario.getCorreo()!= null) {
            usuario.setCorreo(soapUsuario.getCorreo().getValue());
        }
        if (soapUsuario.getCedula()!= null) {
            usuario.setCedula(soapUsuario.getCedula().getValue());
        }

        return usuario;
    }
}
