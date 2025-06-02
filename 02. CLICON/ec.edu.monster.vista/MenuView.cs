using System;
using System.Globalization;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Linq;
using ServiceReference1;
using ec.edu.monster.controlador;

namespace ec.edu.monster.vista
{
    public class MenuView
    {
        private static readonly VueloController vuelosController = new VueloController();
        private static readonly BoletoController boletosController = new BoletoController();


        public static async Task MostrarAsync(Usuarios usuario)
        {
            while (true)
            {
                Console.WriteLine("\n===== MENÚ PRINCIPAL =====");
                Console.WriteLine("1. Ver mis boletos");
                Console.WriteLine("2. Ver todos los vuelos");
                Console.WriteLine("3. Comprar boletos");
                Console.WriteLine("4. Cerrar sesión");
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
                    case "4":
                        Console.WriteLine("Sesión cerrada.");
                        return;
                    default:
                        Console.WriteLine("Opción inválida.\n");
                        break;
                }
            }
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