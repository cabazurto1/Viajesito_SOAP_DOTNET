using System;
using System.Globalization;
using System.Collections.Generic;
using System.Threading.Tasks;
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

            Console.WriteLine("{0,-18} {1,-10} {2,-22} {3,-10}", "Número Boleto", "ID Vuelo", "Fecha Compra", "Precio");
            Console.WriteLine(new string('-', 70));

            foreach (var boleto in boletos)
            {
                string fecha = boleto.FechaCompra?.ToString("yyyy-MM-dd HH:mm") ?? "N/A";
                Console.WriteLine("{0,-18} {1,-10} {2,-22} ${3,-8}", boleto.NumeroBoleto, boleto.IdVuelo, fecha, boleto.PrecioCompra);
            }
        }

        private static async Task VerVuelosAsync()
        {
            List<Vuelos> vuelos = await vuelosController.ObtenerTodosAsync();

            Console.WriteLine("\n===== TODOS LOS VUELOS =====");
            Console.WriteLine("{0,-5} {1,-10} {2,-10} {3,-10} {4,-20} {5,-10} {6,-10}",
                "ID", "Código", "Origen", "Destino", "Hora Salida", "Precio", "Disponibles");
            Console.WriteLine(new string('-', 90));

            foreach (var vuelo in vuelos)
            {
                string hora = vuelo.HoraSalida.ToString("yyyy-MM-dd HH:mm", CultureInfo.InvariantCulture);
                Console.WriteLine("{0,-5} {1,-10} {2,-10} {3,-10} {4,-20} ${5,-8} {6,-10}",
                    vuelo.IdVuelo,
                    vuelo.CodigoVuelo,
                    vuelo.IdCiudadOrigen,
                    vuelo.IdCiudadDestino,
                    hora,
                    vuelo.Valor,
                    vuelo.Disponibles);
            }
        }
    }
}
