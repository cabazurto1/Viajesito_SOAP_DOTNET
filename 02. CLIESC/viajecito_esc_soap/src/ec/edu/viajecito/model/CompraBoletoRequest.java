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
    private Boolean esCredito;
    private Integer numeroCuotas;
    private Double tasaInteresAnual;

    public CompraBoletoRequest() {
    }

    public CompraBoletoRequest(Integer idUsuario, List<VueloCompra> vuelos, Boolean esCredito, Integer numeroCuotas, Double tasaInteresAnual) {
        this.idUsuario = idUsuario;
        this.vuelos = vuelos;
        this.esCredito = esCredito;
        this.numeroCuotas = numeroCuotas;
        this.tasaInteresAnual = tasaInteresAnual;
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

    public Boolean getEsCredito() {
        return esCredito;
    }

    public void setEsCredito(Boolean esCredito) {
        this.esCredito = esCredito;
    }

    public Integer getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(Integer numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }

    public Double getTasaInteresAnual() {
        return tasaInteresAnual;
    }

    public void setTasaInteresAnual(Double tasaInteresAnual) {
        this.tasaInteresAnual = tasaInteresAnual;
    }
}

