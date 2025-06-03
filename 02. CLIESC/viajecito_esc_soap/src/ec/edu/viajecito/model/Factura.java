/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.viajecito.model;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Desarrollo
 */
public class Factura {
    private Integer idFactura;
    private String numeroFactura;
    private int IdUsuario;
    private double PrecioSinIVA;
    private double PrecioConIVA;
    private Date FechaFactura;
    private List<Boleto> boletos;

    public Factura() {
    }

    public Factura(Integer idFactura, String numeroFactura, int IdUsuario, double PrecioSinIVA, double PrecioConIVA, Date FechaFactura, List<Boleto> boletos) {
        this.idFactura = idFactura;
        this.numeroFactura = numeroFactura;
        this.IdUsuario = IdUsuario;
        this.PrecioSinIVA = PrecioSinIVA;
        this.PrecioConIVA = PrecioConIVA;
        this.FechaFactura = FechaFactura;
        this.boletos = boletos;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public double getPrecioSinIVA() {
        return PrecioSinIVA;
    }

    public void setPrecioSinIVA(double PrecioSinIVA) {
        this.PrecioSinIVA = PrecioSinIVA;
    }

    public double getPrecioConIVA() {
        return PrecioConIVA;
    }

    public void setPrecioConIVA(double PrecioConIVA) {
        this.PrecioConIVA = PrecioConIVA;
    }

    public Date getFechaFactura() {
        return FechaFactura;
    }

    public void setFechaFactura(Date FechaFactura) {
        this.FechaFactura = FechaFactura;
    }

    public List<Boleto> getBoletos() {
        return boletos;
    }

    public void setBoletos(List<Boleto> boletos) {
        this.boletos = boletos;
    }
    
    
    
}
