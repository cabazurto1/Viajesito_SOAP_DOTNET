package ec.edu.viajecito.controller;

import ec.edu.viajecito.client.CiudadesClient;
import ec.edu.viajecito.model.Ciudad;
import java.util.List;
import jakarta.ws.rs.core.GenericType;

public class CiudadesController {

    public List<Ciudad> obtenerTodasCiudades() {
        CiudadesClient client = new CiudadesClient();
        try {
            // Se espera que el cliente retorne una lista JSON de Ciudad que se mapea a List<Ciudad>
            return client.findAll(new GenericType<List<Ciudad>>() {});
        } finally {
            client.close();
        }
    }
}
