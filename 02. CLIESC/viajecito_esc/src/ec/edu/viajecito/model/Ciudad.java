/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.viajecito.model;

/**
 *
 * @author Drouet
 */
public class Ciudad {
    private Integer idCiudad;
    private String codigoCiudad;
    private String nombreCiudad;

    // Constructor por defecto
    public Ciudad() {
    }

    // Constructor con solo id
    public Ciudad(Integer idCiudad) {
        this.idCiudad = idCiudad;
    }

    // Constructor con todos los campos
    public Ciudad(Integer idCiudad, String codigoCiudad, String nombreCiudad) {
        this.idCiudad = idCiudad;
        this.codigoCiudad = codigoCiudad;
        this.nombreCiudad = nombreCiudad;
    }

    // Getters y Setters
    public Integer getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Integer idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getCodigoCiudad() {
        return codigoCiudad;
    }

    public void setCodigoCiudad(String codigoCiudad) {
        this.codigoCiudad = codigoCiudad;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }
}
