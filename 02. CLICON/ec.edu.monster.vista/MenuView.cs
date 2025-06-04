using System;
using System.Globalization;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Linq;
using ServiceReference1;
using ec.edu.monster.controlador;
using _02._CLICON.ec.edu.monster.controlador;

namespace ec.edu.monster.vista
{
    public class MenuView
    {
        private static readonly VueloController vuelosController = new VueloController();
        private static readonly BoletoController boletosController = new BoletoController();
        private static readonly FacturaController facturaController = new FacturaController();




        public static async Task MostrarAsync(Usuarios usuario)
        {
            while (true)
            {
                Console.WriteLine("\n===== MENÚ PRINCIPAL =====");
                Console.WriteLine("1. Ver mis boletos");
                Console.WriteLine("2. Ver todos los vuelos");
                Console.WriteLine("3. Comprar boletos");
                Console.WriteLine("4. Ver mis facturas");
                Console.WriteLine("5. Cerrar sesión");                
                Console.Write("Seleccione una opción: ");

                var opcion = Console.ReadLine();

                switch (opcion)
                {
                    case "1":
                        await VerBoletosAsync(usuario);
                        break;
                    case "2":
                        await VerVuelosAsync();
                        break;
                    case "3":
                        await ComprarBoletosView.ComprarAsync(usuario);
                        break;
                    case "5":
                        Console.WriteLine("Sesión cerrada.");
                        return;
                    case "4":
                        await VerFacturasAsync(usuario);
                        break;
                    default:
                        Console.WriteLine("Opción inválida.\n");
                        break;
                }
            }
        }

        private static async Task MostrarDetalleFacturaAsync(Facturas factura, Usuarios usuario)
        {
            Console.WriteLine($"\nFactura: {factura.NumeroFactura}");
            Console.WriteLine("Vendedor: Viajecitos S.A.\t\tRUC:1710708973001");
            Console.WriteLine($"Fecha de emisión: {factura.FechaFactura}");

            Console.WriteLine("\nDatos del usuario");
            Console.WriteLine($"Nombre Cliente: {usuario.Nombre}");
            Console.WriteLine($"Cédula: {usuario.Cedula}");
            Console.WriteLine($"Teléfono: {usuario.Telefono}");
            Console.WriteLine($"Correo: {usuario.Correo}");

            Console.WriteLine("\nDetalle de la factura");
            Console.WriteLine("{0,-15} {1,-35} {2,-10} {3,-15}",
                "numBoleto", "Detalle", "cantidad", "precioUnitario");

            decimal subtotal = 0;

            foreach (var boleto in factura.BoletosRelacionados)
            {
                // Suponiendo que tienes acceso a los datos del vuelo:
                Vuelos vuelo = await vuelosController.ObtenerPorIdAsync(boleto.IdVuelo);

                string ciudadOrigen = ObtenerNombreCiudad(vuelo.IdCiudadOrigen);
                string ciudadDestino = ObtenerNombreCiudad(vuelo.IdCiudadDestino);
                string fecha = vuelo.HoraSalida.ToString("yyyy-MM-dd HH:mm");

                string detalle = $"{ciudadOrigen} - {ciudadDestino} - {fecha}";
                subtotal += boleto.PrecioCompra;

                Console.WriteLine("{0,-15} {1,-35} {2,-10} ${3,-13}",
                    boleto.NumeroBoleto, detalle, 1, boleto.PrecioCompra.ToString("F2"));
            }

            decimal iva = subtotal * 0.15m;
            decimal total = subtotal + iva;

            Console.WriteLine("\n{0,-20} ${1}", "subtotal", subtotal.ToString("F2"));
            Console.WriteLine("{0,-20} ${1}", "descuento", "0");
            Console.WriteLine("{0,-20} ${1}", "subtotal con IVA 15%", iva.ToString("F2"));
            Console.WriteLine("{0,-20} ${1}", "total", total.ToString("F2"));
        }

        private static string ObtenerNombreCiudad(int idCiudad)
        {
            return idCiudad switch
            {
                1 => "Quito",
                2 => "Guayaquil",
                3 => "Cuenca",
                4 => "Miami",
                5 => "Bogotá",
                6 => "Lima",
                _ => $"ID:{idCiudad}"
            };
        }


        private static async Task VerFacturasAsync(Usuarios usuario)
        {
            var facturas = await facturaController.GetFacturaporUsuario(usuario.IdUsuario);

            Console.WriteLine("\n===== TUS FACTURAS =====");

            if (facturas == null || facturas.Length == 0)
            {
                Console.WriteLine("No tienes facturas registradas.");
                return;
            }

            Console.WriteLine("{0,-5} {1,-15} {2,-20} {3,-15} {4,-15}",
                "ID", "Número", "Fecha", "Sin IVA", "Con IVA");
            Console.WriteLine(new string('-', 75));

            foreach (var factura in facturas)
            {
                Console.WriteLine("{0,-5} {1,-15} {2,-20} ${3,-13} ${4,-13}",
                    factura.IdFactura,
                    factura.NumeroFactura,
                    factura.FechaFactura.ToString("yyyy-MM-dd HH:mm"),
                    factura.PrecioSinIVA.ToString("F2"),
                    factura.PrecioConIVA.ToString("F2"));
            }

            Console.Write("\nIngrese el ID de la factura para ver detalles (0 para salir): ");
            if (!int.TryParse(Console.ReadLine(), out int idSeleccionado) || idSeleccionado < 0)
            {
                Console.WriteLine("ID inválido.");
                return;
            }

            if (idSeleccionado == 0)
                return;

            var detalle = await facturaController.GetFactura(idSeleccionado);
            if (detalle == null || detalle.BoletosRelacionados == null)
            {
                Console.WriteLine("Factura no encontrada o sin boletos asociados.");
                return;
            }

            await MostrarDetalleFacturaAsync(detalle, usuario);
        }


        private static async Task VerBoletosAsync(Usuarios usuario)
        {
            List<Boletos> boletos = await boletosController.ObtenerPorUsuarioAsync(usuario.IdUsuario);

            Console.WriteLine("\n===== TUS BOLETOS =====");

            if (boletos == null || boletos.Count == 0)
            {
                Console.WriteLine("No tienes boletos registrados.");
                return;
            }

            Console.WriteLine("{0,-18} {1,-10} {2,-22} {3,-10}",
                "Número Boleto", "ID Vuelo", "Fecha Compra", "Precio");
            Console.WriteLine(new string('-', 70));

            foreach (var boleto in boletos)
            {
                string fecha = boleto.FechaCompra?.ToString("yyyy-MM-dd HH:mm") ?? "N/A";
                Console.WriteLine("{0,-18} {1,-10} {2,-22} ${3,-8}",
                    boleto.NumeroBoleto, boleto.IdVuelo, fecha, boleto.PrecioCompra);
            }
        }

        private static async Task VerVuelosAsync()
        {
            List<Vuelos> vuelos = await vuelosController.ObtenerTodosAsync();

            Console.WriteLine("\n===== TODOS LOS VUELOS =====");

            if (vuelos == null || vuelos.Count == 0)
            {
                Console.WriteLine("No hay vuelos disponibles.");
                return;
            }

            // Mapeo directo de IDs a nombres de ciudades
            var mapaCiudades = new Dictionary<int, string>
            {
                { 1, "Quito" },
                { 2, "Guayaquil" },
                { 3, "Cuenca" },
                { 4, "Miami" },
                { 5, "Bogotá" },
                { 6, "Lima" }
            };

            Console.WriteLine("{0,-5} {1,-10} {2,-15} {3,-15} {4,-20} {5,-10} {6,-10}",
                "ID", "Código", "Origen", "Destino", "Hora Salida", "Precio", "Disponibles");
            Console.WriteLine(new string('-', 100));

            foreach (var vuelo in vuelos)
            {
                string hora = vuelo.HoraSalida.ToString("yyyy-MM-dd HH:mm", CultureInfo.InvariantCulture);

                // Obtener nombres de ciudades o mostrar ID si no se encuentra
                string nombreOrigen = mapaCiudades.ContainsKey(vuelo.IdCiudadOrigen)
                    ? mapaCiudades[vuelo.IdCiudadOrigen]
                    : $"ID:{vuelo.IdCiudadOrigen}";

                string nombreDestino = mapaCiudades.ContainsKey(vuelo.IdCiudadDestino)
                    ? mapaCiudades[vuelo.IdCiudadDestino]
                    : $"ID:{vuelo.IdCiudadDestino}";

                Console.WriteLine("{0,-5} {1,-10} {2,-15} {3,-15} {4,-20} ${5,-8} {6,-10}",
                    vuelo.IdVuelo,
                    vuelo.CodigoVuelo,
                    nombreOrigen,
                    nombreDestino,
                    hora,
                    vuelo.Valor,
                    vuelo.Disponibles);
            }
        }
    }
}