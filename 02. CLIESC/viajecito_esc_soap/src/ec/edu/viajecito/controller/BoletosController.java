/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.viajecito.controller;

import ec.edu.viajecito.client.AeroCondorClient;
import ec.edu.viajecito.client.ArrayOfBoletos;
import ec.edu.viajecito.client.Boletos;
import ec.edu.viajecito.client.Ciudades;
import ec.edu.viajecito.client.Usuarios;
import ec.edu.viajecito.client.Vuelos;
import ec.edu.viajecito.model.Boleto;
import ec.edu.viajecito.model.Ciudad;
import ec.edu.viajecito.model.CompraBoletoRequest;
import ec.edu.viajecito.model.Usuario;
import ec.edu.viajecito.model.Vuelo;
import ec.edu.viajecito.model.VueloCompra;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 *
 * @author Drouet
 */
public class BoletosController {
    public List<Boleto> obtenerBoletosPorUsuario(String idUsuario) {
        ArrayOfBoletos arrayOfBoletos = AeroCondorClient.obtenerBoletosPorUsuario(Integer.valueOf(idUsuario));
        List<Boleto> boletos = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        if (arrayOfBoletos != null && arrayOfBoletos.getBoletos() != null) {
            for (Boletos b : arrayOfBoletos.getBoletos()) {
                Boleto boleto = new Boleto();
                boleto.setIdBoleto(b.getIdBoleto());
                boleto.setNumeroBoleto(b.getNumeroBoleto().getValue());

                if (b.getFechaCompra() != null) {
                    boleto.setFechaCompra(sdf.format(b.getFechaCompra().getValue().toGregorianCalendar().getTime()));
                } else {
                    boleto.setFechaCompra(null);
                }

                boleto.setPrecioCompra(b.getPrecioCompra());

                // Usuario
                Usuarios u = AeroCondorClient.obtenerUsuarioPorId(b.getIdUsuario());
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(u.getIdUsuario());
                usuario.setNombre(u.getNombre().getValue());
                usuario.setUsername(u.getUsername().getValue());
                usuario.setPassword(u.getPassword().getValue());
                usuario.setTelefono(u.getTelefono().getValue());
                boleto.setUsuario(usuario);

                // Vuelo
                Vuelos v = AeroCondorClient.obtenerVueloPorId(b.getIdVuelo());
                Vuelo vuelo = new Vuelo();
                vuelo.setIdVuelo(v.getIdVuelo());
                vuelo.setCodigoVuelo(v.getCodigoVuelo().getValue());
                vuelo.setValor(v.getValor());
                vuelo.setHoraSalida(v.getHoraSalida().toGregorianCalendar().getTime());
                vuelo.setCapacidad(v.getCapacidad());
                vuelo.setDisponibles(v.getDisponibles());

                // Ciudad Origen
                Ciudades origen = AeroCondorClient.obtenerCiudadPorId(v.getIdCiudadOrigen());
                Ciudad ciudadOrigen = new Ciudad();
                ciudadOrigen.setIdCiudad(origen.getIdCiudad());
                ciudadOrigen.setCodigoCiudad(origen.getCodigoCiudad().getValue());
                ciudadOrigen.setNombreCiudad(origen.getNombreCiudad().getValue());
                vuelo.setCiudadOrigen(ciudadOrigen);

                // Ciudad Destino
                Ciudades destino = AeroCondorClient.obtenerCiudadPorId(v.getIdCiudadDestino());
                Ciudad ciudadDestino = new Ciudad();
                ciudadDestino.setIdCiudad(destino.getIdCiudad());
                ciudadDestino.setCodigoCiudad(destino.getCodigoCiudad().getValue());
                ciudadDestino.setNombreCiudad(destino.getNombreCiudad().getValue());
                vuelo.setCiudadDestino(ciudadDestino);

                boleto.setVuelo(vuelo);
                boletos.add(boleto);
            }
        }

        return boletos;
    }

    public boolean comprarBoletos(CompraBoletoRequest compraBoletoRequest) {
        ec.edu.viajecito.client.CompraBoletoRequest compraBoletoRequest1 = new ec.edu.viajecito.client.CompraBoletoRequest();

        // Asignar el ID del usuario
        compraBoletoRequest1.setIdUsuario(compraBoletoRequest.getIdUsuario());

        // Crear lista de VueloCompra SOAP
        List<ec.edu.viajecito.client.VueloCompra> vuelosSoap = new ArrayList<>();
        for (VueloCompra vuelo : compraBoletoRequest.getVuelos()) {
            ec.edu.viajecito.client.VueloCompra vueloSoap = new ec.edu.viajecito.client.VueloCompra();
            vueloSoap.setIdVuelo(vuelo.getIdVuelo());
            vueloSoap.setCantidad(vuelo.getCantidad());
            vuelosSoap.add(vueloSoap);
        }

        // Crear el ArrayOfVueloCompra SOAP y añadir los vuelos
        ec.edu.viajecito.client.ArrayOfVueloCompra arrayOfVueloCompra = new ec.edu.viajecito.client.ArrayOfVueloCompra();
        arrayOfVueloCompra.getVueloCompra().addAll(vuelosSoap);

        // Envolver en JAXBElement
        JAXBElement<ec.edu.viajecito.client.ArrayOfVueloCompra> vuelosElement =
            new JAXBElement<>(
                new QName("http://schemas.datacontract.org/2004/07/ec.edu.monster.modelo", "Vuelos"),
                ec.edu.viajecito.client.ArrayOfVueloCompra.class,
                arrayOfVueloCompra
            );

        compraBoletoRequest1.setVuelos(vuelosElement);

        // Enviar al cliente SOAP
        return AeroCondorClient.comprar(compraBoletoRequest1);
    }

}
