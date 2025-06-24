/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.viajecito.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Drouet
 */
public class Amortizacion {
    private int idAmortizacion;
    private int idFactura;
    private int numeroCuota;
    private double valorCuota;
    private double interesPagado;
    private double capitalPagado;
    private double saldo;

    public Amortizacion() {
    }

    public Amortizacion(int idAmortizacion, int idFactura, int numeroCuota, double valorCuota, double interesPagado, double capitalPagado, double saldo) {
        this.idAmortizacion = idAmortizacion;
        this.idFactura = idFactura;
        this.numeroCuota = numeroCuota;
        this.valorCuota = valorCuota;
        this.interesPagado = interesPagado;
        this.capitalPagado = capitalPagado;
        this.saldo = saldo;
    }

    // Getters y Setters
    public int getIdAmortizacion() {
        return idAmortizacion;
    }

    public void setIdAmortizacion(int idAmortizacion) {
        this.idAmortizacion = idAmortizacion;
    }

    public int getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(int idFactura) {
        this.idFactura = idFactura;
    }

    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public double getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(double valorCuota) {
        this.valorCuota = redondear(valorCuota);
    }

    public double getInteresPagado() {
        return interesPagado;
    }

    public void setInteresPagado(double interesPagado) {
        this.interesPagado = redondear(interesPagado);
    }

    public double getCapitalPagado() {
        return capitalPagado;
    }

    public void setCapitalPagado(double capitalPagado) {
        this.capitalPagado = redondear(capitalPagado);
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = redondear(Math.max(saldo, 0));
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
    
    public static List<Amortizacion> calcularAmortizacion(double monto, int cuotas, double tasaAnual) {
        List<Amortizacion> tabla = new ArrayList<>();

        double tasaMensual = tasaAnual / 12.0 / 100.0;
        double cuotaFija = monto * (tasaMensual / (1 - Math.pow(1 + tasaMensual, -cuotas)));

        double saldo = monto;
        double interesTotal = 0;

        for (int i = 1; i <= cuotas; i++) {
            double interes = saldo * tasaMensual;
            double capital = cuotaFija - interes;
            saldo -= capital;
            interesTotal += interes;

            Amortizacion fila = new Amortizacion();
            fila.setIdAmortizacion(0); // Asigna 0 por defecto o un valor autogenerado si corresponde
            fila.setNumeroCuota(i);
            fila.setValorCuota(cuotaFija);
            fila.setInteresPagado(interes);
            fila.setCapitalPagado(capital);
            fila.setSaldo(saldo);

            tabla.add(fila);
        }

        return tabla;
    } 
}
