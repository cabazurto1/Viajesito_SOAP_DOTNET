package ec.edu.viajecito.controller;

import ec.edu.viajecito.client.AeroCondorClient;
import ec.edu.viajecito.client.ArrayOfBoletos;
import ec.edu.viajecito.client.Boletos;
import ec.edu.viajecito.client.Facturas;
import ec.edu.viajecito.client.ArrayOfFacturas;
import ec.edu.viajecito.model.Boleto;
import ec.edu.viajecito.model.Factura;
import ec.edu.viajecito.model.Vuelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FacturasController {

    public List<Factura> obtenerFacturasPorUsuario(int idUsuario) {
        ArrayOfFacturas arraySoap = AeroCondorClient.getFacturasPorUsuario(idUsuario);
        List<Factura> facturas = new ArrayList<>();

        if (arraySoap != null && arraySoap.getFacturas() != null) {
            for (Facturas facturaSoap : arraySoap.getFacturas()) {
                facturas.add(convertirFactura(facturaSoap));
            }
        }

        return facturas;
    }

    public Factura obtenerFacturaPorId(int idFactura) {
        Facturas facturaSoap = AeroCondorClient.obtenerFacturaPorId(idFactura);
        if (facturaSoap == null) return null;

            return convertirFactura(facturaSoap);
        }

        private Factura convertirFactura(Facturas facturaSoap) {
        Factura factura = new Factura();

        factura.setIdFactura(facturaSoap.getIdFactura());
        factura.setNumeroFactura(facturaSoap.getNumeroFactura() != null ? facturaSoap.getNumeroFactura().getValue() : "");
        factura.setIdUsuario(facturaSoap.getIdUsuario() != null ? facturaSoap.getIdUsuario() : 0);

        BigDecimal sinIVA = facturaSoap.getPrecioSinIVA();
        BigDecimal conIVA = facturaSoap.getPrecioConIVA();

        factura.setPrecioSinIVA(sinIVA != null ? sinIVA.doubleValue() : 0);
        factura.setPrecioConIVA(conIVA != null ? conIVA.doubleValue() : 0);

        if (facturaSoap.getFechaFactura() != null) {
            factura.setFechaFactura(facturaSoap.getFechaFactura().toGregorianCalendar().getTime());
        }

        // Convertir boletos relacionados
        List<Boleto> boletos = new ArrayList<>();

        if (facturaSoap.getBoletosRelacionados() != null &&
            facturaSoap.getBoletosRelacionados().getValue() != null &&
            facturaSoap.getBoletosRelacionados().getValue().getBoletos() != null) {

            List<Boletos> boletosSoap = facturaSoap.getBoletosRelacionados().getValue().getBoletos();

            for (Boletos boletoSoap : boletosSoap) {
                Boleto boleto = convertirBoleto(boletoSoap);
                // Aquí corregimos el problema: buscamos el vuelo de cada boleto individualmente
                Vuelo vuelo = buscarVueloPorId(boletoSoap.getIdVuelo());
                boleto.setVuelo(vuelo);
                boletos.add(boleto);
            }
        }

        factura.setBoletos(boletos);
        return factura;
    }


    private Boleto convertirBoleto(Boletos boletoSoap) {
        Boleto boleto = new Boleto();

        boleto.setIdBoleto(boletoSoap.getIdBoleto());
        boleto.setNumeroBoleto(boletoSoap.getNumeroBoleto() != null ? boletoSoap.getNumeroBoleto().getValue() : "");
        boleto.setPrecioCompra(boletoSoap.getPrecioCompra() != null ? boletoSoap.getPrecioCompra() : new BigDecimal(0));
        
        if (boletoSoap.getFechaCompra() != null) {
            boleto.setFechaCompra(boletoSoap.getFechaCompra().toString());
        }

        return boleto;
    }

    private Vuelo buscarVueloPorId(Integer idVuelo) {
        if (idVuelo == null) return null;

        VuelosController vuelosController = new VuelosController();
        List<Vuelo> vuelos = vuelosController.obtenerTodosVuelos();

        for (Vuelo v : vuelos) {
            if (v.getIdVuelo() == idVuelo) {
                return v;
            }
        }

        return null;
    }
}
