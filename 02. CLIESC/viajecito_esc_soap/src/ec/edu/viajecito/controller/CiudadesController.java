package ec.edu.viajecito.controller;

import ec.edu.viajecito.client.AeroCondorClient;
import ec.edu.viajecito.client.ArrayOfCiudades;
import ec.edu.viajecito.client.Ciudades;
import ec.edu.viajecito.model.Ciudad;
import java.util.ArrayList;
import java.util.List;

public class CiudadesController {

    public List<Ciudad> obtenerTodasCiudades() {
        ArrayOfCiudades arrayOfCiudades = AeroCondorClient.getCiudades();
        List<Ciudad> ciudades = new ArrayList<>();

        if (arrayOfCiudades != null && arrayOfCiudades.getCiudades() != null) {
            for (Ciudades c : arrayOfCiudades.getCiudades()) {
                Ciudad ciudad = new Ciudad();
                ciudad.setIdCiudad(c.getIdCiudad());

                if (c.getCodigoCiudad() != null) {
                    ciudad.setCodigoCiudad(c.getCodigoCiudad().getValue());
                }

                if (c.getNombreCiudad() != null) {
                    ciudad.setNombreCiudad(c.getNombreCiudad().getValue());
                }

                ciudades.add(ciudad);
            }
        }

        return ciudades;
    }
}
