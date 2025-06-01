/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.viajecito.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Drouet
 */
public class Vuelo {
    private Integer idVuelo;
    private String codigoVuelo;
    private BigDecimal valor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date horaSalida;
    private int capacidad;
    private int disponibles;
    @JsonAlias("idCiudadOrigen")
    private Ciudad ciudadOrigen;
    @JsonAlias("idCiudadDestino")
    private Ciudad ciudadDestino;

    // Constructor por defecto
    public Vuelo() {
    }

    // Constructor con solo id
    public Vuelo(Integer idVuelo) {
        this.idVuelo = idVuelo;
    }

    // Constructor con todos los campos excepto relaciones
    public Vuelo(Integer idVuelo, String codigoVuelo, BigDecimal valor, Date horaSalida, int capacidad, int disponibles) {
        this.idVuelo = idVuelo;
        this.codigoVuelo = codigoVuelo;
        this.valor = valor;
        this.horaSalida = horaSalida;
        this.capacidad = capacidad;
        this.disponibles = disponibles;
    }

    // Getters y Setters
    public Integer getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(Integer idVuelo) {
        this.idVuelo = idVuelo;
    }

    public String getCodigoVuelo() {
        return codigoVuelo;
    }

    public void setCodigoVuelo(String codigoVuelo) {
        this.codigoVuelo = codigoVuelo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Date horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public Ciudad getCiudadOrigen() {
        return ciudadOrigen;
    }

    public void setCiudadOrigen(Ciudad ciudadOrigen) {
        this.ciudadOrigen = ciudadOrigen;
    }

    public Ciudad getCiudadDestino() {
        return ciudadDestino;
    }

    public void setCiudadDestino(Ciudad ciudadDestino) {
        this.ciudadDestino = ciudadDestino;
    }
}
