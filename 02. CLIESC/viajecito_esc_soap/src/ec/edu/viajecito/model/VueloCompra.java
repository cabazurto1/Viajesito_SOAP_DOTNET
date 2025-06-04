/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.viajecito.model;

/**
 *
 * @author Drouet
 */
public class VueloCompra {
    private int idVuelo;
    private int cantidad;

    public VueloCompra() {
    }

    public VueloCompra(int idVuelo, int cantidad) {
        this.idVuelo = idVuelo;
        this.cantidad = cantidad;
    }

    public int getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(int idVuelo) {
        this.idVuelo = idVuelo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
}
