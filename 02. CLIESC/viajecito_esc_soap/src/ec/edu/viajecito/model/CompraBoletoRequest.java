/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.viajecito.model;

import java.util.List;

/**
 *
 * @author Drouet
 */
public class CompraBoletoRequest {
    private Integer idUsuario;
    private List<VueloCompra> vuelos;

    public CompraBoletoRequest() {
    }

    public CompraBoletoRequest(Integer idUsuario, List<VueloCompra> vuelos) {
        this.idUsuario = idUsuario;
        this.vuelos = vuelos;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<VueloCompra> getVuelos() {
        return vuelos;
    }

    public void setVuelos(List<VueloCompra> vuelos) {
        this.vuelos = vuelos;
    }
}

