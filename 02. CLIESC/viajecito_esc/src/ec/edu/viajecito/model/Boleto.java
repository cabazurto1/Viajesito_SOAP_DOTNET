/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.viajecito.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 *
 * @author Drouet
 */
public class Boleto {

    private Integer idBoleto;
    private String numeroBoleto;
    private String fechaCompra;
    private BigDecimal precioCompra;
    @JsonAlias("idUsuario")
    private Usuario usuario;
    @JsonAlias("idVuelo")
    private Vuelo vuelo;

    // Constructor por defecto
    public Boleto() {
    }

    // Constructor con todos los campos
    public Boleto(Integer idBoleto, String numeroBoleto, String fechaCompra, BigDecimal precioCompra, Usuario usuario, Vuelo vuelo) {
        this.idBoleto = idBoleto;
        this.numeroBoleto = numeroBoleto;
        this.fechaCompra = fechaCompra;
        this.precioCompra = precioCompra;
        this.usuario = usuario;
        this.vuelo = vuelo;
    }

    // Getters y Setters
    public Integer getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(Integer idBoleto) {
        this.idBoleto = idBoleto;
    }

    public String getNumeroBoleto() {
        return numeroBoleto;
    }

    public void setNumeroBoleto(String numeroBoleto) {
        this.numeroBoleto = numeroBoleto;
    }

    public String getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(String fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public BigDecimal getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }
}
