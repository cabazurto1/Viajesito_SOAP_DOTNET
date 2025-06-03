package ec.edu.viajecito.controller;

import ec.edu.viajecito.client.AeroCondorClient;
import ec.edu.viajecito.client.ArrayOfVuelos;
import ec.edu.viajecito.client.Ciudades;
import ec.edu.viajecito.client.Vuelos;
import ec.edu.viajecito.model.Ciudad;
import ec.edu.viajecito.model.Vuelo;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConstants;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class VuelosController {

    public List<Vuelo> obtenerTodosVuelos() {
        ArrayOfVuelos arraySoap = AeroCondorClient.getVuelos();
        List<Vuelo> vuelos = new ArrayList<>();

        if (arraySoap != null && arraySoap.getVuelos() != null) {
            for (Vuelos vueloSoap : arraySoap.getVuelos()) {
                vuelos.add(convertirVuelo(vueloSoap));
            }
        }

        return vuelos;
    }

    // Convierte String "yyyy-MM-dd" a XMLGregorianCalendar solo con fecha
    public XMLGregorianCalendar toXMLGregorianCalendarDateTime(String fecha) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(fecha);

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);

        XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(
            cal.get(GregorianCalendar.YEAR),
            cal.get(GregorianCalendar.MONTH) + 1,
            cal.get(GregorianCalendar.DAY_OF_MONTH),
            0, 0, 0, 0, DatatypeConstants.FIELD_UNDEFINED
        );

        return xmlCal;
    }

    public List<Vuelo> obtenerVuelosPorCiudad(String origen, String destino, String fechaSalidaString) {
        try {
            if (fechaSalidaString == null || fechaSalidaString.trim().isEmpty()) {
                throw new IllegalArgumentException("La fecha de salida no puede estar vacía");
            }

            XMLGregorianCalendar fechaSalida = toXMLGregorianCalendarDateTime(fechaSalidaString);

            System.out.println("Fecha XML: " + fechaSalida.toXMLFormat());


            ArrayOfVuelos arraySoap = AeroCondorClient.buscarVuelos(origen, destino, fechaSalida);
            List<Vuelo> vuelos = new ArrayList<>();

            if (arraySoap != null && arraySoap.getVuelos() != null) {
                for (Vuelos vueloSoap : arraySoap.getVuelos()) {
                    vuelos.add(convertirVuelo(vueloSoap));
                }
            }

            return vuelos;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    // Método auxiliar para convertir Vuelos (SOAP) → Vuelo (local)
    private Vuelo convertirVuelo(Vuelos vueloSoap) {
        Vuelo vuelo = new Vuelo();

        vuelo.setIdVuelo(vueloSoap.getIdVuelo());
        vuelo.setCodigoVuelo(vueloSoap.getCodigoVuelo().getValue());
        vuelo.setValor(vueloSoap.getValor());
        vuelo.setCapacidad(vueloSoap.getCapacidad());
        vuelo.setDisponibles(vueloSoap.getDisponibles());

        // Convertir hora de salida
        if (vueloSoap.getHoraSalida() != null) {
            vuelo.setHoraSalida(vueloSoap.getHoraSalida().toGregorianCalendar().getTime());
        }

        // Convertir ciudad origen
        if (vueloSoap.getIdCiudadOrigen() != null) {
            vuelo.setCiudadOrigen(convertirCiudad(vueloSoap.getIdCiudadOrigen()));
        }

        // Convertir ciudad destino
        if (vueloSoap.getIdCiudadDestino() != null) {
            vuelo.setCiudadDestino(convertirCiudad(vueloSoap.getIdCiudadDestino()));
        }

        return vuelo;
    }

    // Método auxiliar para convertir Ciudades (SOAP) → Ciudad (local)
    private Ciudad convertirCiudad(Integer idCiudad) {
        Ciudad ciudad = new Ciudad();

        if (idCiudad != null) {
            // Llamar al servicio para obtener la ciudad completa por ID
            Ciudades ciudadCompletaSoap = AeroCondorClient.obtenerCiudadPorId(idCiudad);

            if (ciudadCompletaSoap != null) {
                ciudad.setIdCiudad(ciudadCompletaSoap.getIdCiudad());
                ciudad.setCodigoCiudad(ciudadCompletaSoap.getCodigoCiudad().getValue());
                ciudad.setNombreCiudad(ciudadCompletaSoap.getNombreCiudad().getValue());
            } else {
                // En caso de que no exista la ciudad en el servicio, poner solo el ID
                ciudad.setIdCiudad(idCiudad);
            }
        }

        return ciudad;
    }

}
