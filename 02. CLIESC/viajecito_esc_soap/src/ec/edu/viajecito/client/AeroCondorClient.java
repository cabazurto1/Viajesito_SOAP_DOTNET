/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.viajecito.client;

/**
 *
 * @author Drouet
 */
public class AeroCondorClient {
    
    

    public static ArrayOfBoletos obtenerBoletosPorUsuario(java.lang.Integer idUsuario) {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.obtenerBoletosPorUsuario(idUsuario);
    }

    public static Boolean comprar(ec.edu.viajecito.client.CompraBoletoRequest request) {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.comprar(request);
    }

    public static ArrayOfCiudades getCiudades() {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.getCiudades();
    }

    public static Boolean crearUsuario(ec.edu.viajecito.client.Usuarios usuario) {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.crearUsuario(usuario);
    }

    public static Usuarios login(java.lang.String username, java.lang.String password) {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.login(username, password);
    }

    public static ArrayOfVuelos getVuelos() {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.getVuelos();
    }

    public static ArrayOfVuelos buscarVuelos(java.lang.String origen, java.lang.String destino, javax.xml.datatype.XMLGregorianCalendar fechaSalida) {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.buscarVuelos(origen, destino, fechaSalida);
    }

    public static Ciudades obtenerCiudadPorId(java.lang.Integer id) {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.obtenerCiudadPorId(id);
    }

    public static Usuarios obtenerUsuarioPorId(java.lang.Integer id) {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.obtenerUsuarioPorId(id);
    }

    public static Vuelos obtenerVueloPorId(java.lang.Integer id) {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.obtenerVueloPorId(id);
    }

    public static Facturas obtenerFacturaPorId(java.lang.Integer id) {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.obtenerFacturaPorId(id);
    }

    public static ArrayOfFacturas getFacturasPorUsuario(java.lang.Integer idUsuario) {
        ec.edu.viajecito.client.AeroCondorController service = new ec.edu.viajecito.client.AeroCondorController();
        ec.edu.viajecito.client.IAeroCondorController port = service.getBasicHttpBindingIAeroCondorController();
        return port.getFacturasPorUsuario(idUsuario);
    }        
    
}
