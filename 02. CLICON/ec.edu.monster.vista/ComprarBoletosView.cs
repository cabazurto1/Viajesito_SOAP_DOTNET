using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Threading.Tasks;
using ServiceReference1;

namespace ec.edu.monster.vista
{
    public static class ComprarBoletosView
    {
        private static readonly AeroCondorControllerClient client = new AeroCondorControllerClient();
        private static readonly CultureInfo culture = CultureInfo.InvariantCulture;

        public static async Task ComprarAsync(Usuarios usuario)
        {
            var ciudades = await client.GetCiudadesAsync();
            if (ciudades == null || ciudades.Length == 0)
            {
                Console.WriteLine("No hay ciudades disponibles.");
                return;
            }

            int origenIdx = PedirIndiceCiudad("origen", ciudades, -1);
            int destinoIdx = PedirIndiceCiudad("destino", ciudades, origenIdx);

            DateTime fechaSalida = PedirFechaSalida();

            var ciudadOrigen = ciudades[origenIdx];
            var ciudadDestino = ciudades[destinoIdx];

            var vuelos = await client.BuscarVuelosAsync(
                ciudadOrigen.CodigoCiudad,
                ciudadDestino.CodigoCiudad,
                fechaSalida
            );

            if (vuelos == null || vuelos.Length == 0)
            {
                Console.WriteLine("No hay vuelos disponibles para esa fecha.");
                return;
            }

            int vueloIdx = PedirIndiceVuelo(vuelos);
            var vuelo = vuelos[vueloIdx];

            int cantidad = PedirCantidadBoletos(vuelo);

            decimal total = vuelo.Valor * cantidad;
            Console.WriteLine($"Total a pagar: ${total}");

            if (!Confirmar("¿Confirmar compra? (s/n): "))
            {
                Console.WriteLine("Compra cancelada.");
                return;
            }

            var request = new CompraBoletoRequest
            {
                IdVuelo = vuelo.IdVuelo,
                IdUsuario = usuario.IdUsuario,
                Cantidad = cantidad
            };

            var result = await client.ComprarAsync(request);
            Console.WriteLine(result ? "Compra realizada con éxito." : "Error en la compra.");
        }

        private static int PedirIndiceCiudad(string tipo, Ciudades[] ciudades, int excluir)
        {
            int idx = -1;
            while (idx < 0 || idx >= ciudades.Length || idx == excluir)
            {
                Console.WriteLine($"\n===== SELECCIONAR CIUDAD DE {tipo.ToUpper()} =====");
                for (int i = 0; i < ciudades.Length; i++)
                {
                    if (i == excluir) continue;
                    Console.WriteLine($"{i + 1}. {ciudades[i].CodigoCiudad} - {ciudades[i].NombreCiudad}");
                }
                Console.Write($"Elija ciudad de {tipo}: ");
                if (int.TryParse(Console.ReadLine(), out int opcion))
                {
                    opcion--;
                    if (opcion >= 0 && opcion < ciudades.Length && opcion != excluir)
                    {
                        idx = opcion;
                    }
                    else
                        Console.WriteLine("Opción inválida o repetida.");
                }
                else
                {
                    Console.WriteLine("Entrada inválida.");
                }
            }
            return idx;
        }

        private static DateTime PedirFechaSalida()
        {
            while (true)
            {
                Console.Write("Ingrese fecha de salida (yyyy-MM-dd): ");
                string input = Console.ReadLine();
                if (DateTime.TryParseExact(input, "yyyy-MM-dd", culture, DateTimeStyles.None, out DateTime fecha))
                {
                    if (fecha.Date >= DateTime.Today)
                        return fecha;
                    else
                        Console.WriteLine("La fecha no puede ser anterior a hoy.");
                }
                else
                {
                    Console.WriteLine("Formato inválido.");
                }
            }
        }

        private static int PedirIndiceVuelo(Vuelos[] vuelos)
        {
            int idx = -1;
            while (idx < 0 || idx >= vuelos.Length)
            {
                Console.WriteLine("\n===== VUELOS DISPONIBLES =====");
                for (int i = 0; i < vuelos.Length; i++)
                {
                    Console.WriteLine($"{i + 1}. Código: {vuelos[i].CodigoVuelo} | Hora: {vuelos[i].HoraSalida} | Precio: ${vuelos[i].Valor} | Disponibles: {vuelos[i].Disponibles}");
                }
                Console.Write("Seleccione el número de vuelo: ");
                if (int.TryParse(Console.ReadLine(), out int opcion))
                {
                    opcion--;
                    if (opcion >= 0 && opcion < vuelos.Length)
                        idx = opcion;
                    else
                        Console.WriteLine("Opción inválida.");
                }
                else
                {
                    Console.WriteLine("Entrada inválida.");
                }
            }
            return idx;
        }

        private static int PedirCantidadBoletos(Vuelos vuelo)
        {
            int cantidad = 0;
            while (cantidad <= 0 || cantidad > vuelo.Disponibles)
            {
                Console.Write($"¿Cuántos boletos desea comprar? (Disponibles: {vuelo.Disponibles}): ");
                if (int.TryParse(Console.ReadLine(), out cantidad))
                {
                    if (cantidad <= 0)
                        Console.WriteLine("Debe ser mayor que cero.");
                    else if (cantidad > vuelo.Disponibles)
                        Console.WriteLine("No hay suficientes boletos disponibles.");
                }
                else
                {
                    Console.WriteLine("Entrada inválida.");
                }
            }
            return cantidad;
        }

        private static bool Confirmar(string mensaje)
        {
            Console.Write(mensaje);
            string respuesta = Console.ReadLine();
            return respuesta.Equals("s", StringComparison.OrdinalIgnoreCase);
        }
    }
}
