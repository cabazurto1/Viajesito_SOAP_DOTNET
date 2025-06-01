/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.viajecito.controller;

import ec.edu.viajecito.client.BoletosClient;
import ec.edu.viajecito.model.Boleto;
import ec.edu.viajecito.model.CompraBoletoRequest;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author Drouet
 */
public class BoletosController {
    public List<Boleto> obtenerBoletosPorUsuario(String idUsuario) {
        BoletosClient client = new BoletosClient();
        try {
            // Asumiendo que el servicio devuelve List<Boleto>
            List<Boleto> boletos = client.obtenerBoletosPorUsuario(new GenericType<List<Boleto>>() {}, idUsuario);
            return boletos;
        } finally {
            client.close();
        }
    }

    public boolean comprarBoletos(CompraBoletoRequest compraBoletoRequest) {
        BoletosClient client = new BoletosClient();
        try {
            Response response = client.comprarBoletos(compraBoletoRequest);
            int status = response.getStatus();
            // Retorna true si el status está en el rango 200-299 (éxito HTTP)
            return status >= 200 && status < 300;
        } finally {
            client.close();
        }
    }
}
