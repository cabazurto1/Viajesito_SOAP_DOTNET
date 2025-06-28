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

            var listaVuelos = new List<ServiceReference1.VueloCompra>();
            decimal total = 0;

            do
            {
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
                    continue;
                }

                int vueloIdx = PedirIndiceVuelo(vuelos);
                var vuelo = vuelos[vueloIdx];

                int cantidad = PedirCantidadBoletos(vuelo);

                total += vuelo.Valor * cantidad;

                var vueloCompra = new ServiceReference1.VueloCompra
                {
                    IdVuelo = vuelo.IdVuelo,
                    Cantidad = cantidad
                };

                listaVuelos.Add(vueloCompra);

                Console.WriteLine($"Subtotal actual (sin IVA): ${total}");

            } while (Confirmar("¿Desea agregar otro vuelo a la compra? (s/n): "));

            total = new Decimal(((double)total) * 1.15);

            Console.WriteLine($"Total actual (con IVA): ${total}");

            // Elegir tipo de pago
            bool esCredito = Confirmar("¿Desea pagar a crédito? (s/n): ");
            int cuotas = 1;
            double tasaInteres = 16.5;

            if (esCredito)
            {
                cuotas = PedirEntero("Ingrese el número de cuotas mensuales (2 o más): ", 2);
                var resumen = CalcularAmortizacion((double)total, tasaInteres, cuotas);
                MostrarResumenCredito((double)total, resumen, cuotas);
                total = new Decimal(cuotas * resumen);
                if (!Confirmar("¿Desea continuar con esta forma de pago? (s/n): "))
                {
                    Console.WriteLine("Compra cancelada.");
                    return;
                }
            }

            Console.WriteLine($"Total a pagar: ${total}");

            if (!Confirmar("¿Confirmar compra total? (s/n): "))
            {
                Console.WriteLine("Compra cancelada.");
                return;
            }

            var request = new CompraBoletoRequest
            {
                IdUsuario = usuario.IdUsuario,
                Vuelos = listaVuelos.ToArray(),
                EsCredito = esCredito,
                NumeroCuotas = cuotas,
                TasaInteresAnual = tasaInteres,
            };

            var result = await client.ComprarAsync(request);
            Console.WriteLine(result ? "Compra realizada con éxito." : "Error en la compra.");
        }

        private static int PedirEntero(string mensaje, int minimo)
        {
            int valor;
            do
            {
                Console.Write(mensaje);
            } while (!int.TryParse(Console.ReadLine(), out valor) || valor < minimo);
            return valor;
        }

        private static double CalcularAmortizacion(double monto, double tasaAnual, int cuotas)
        {
            double tasaMensual = tasaAnual / 12 / 100;
            double cuotaMensual = monto * tasaMensual / (1 - Math.Pow(1 + tasaMensual, -cuotas));
            return cuotaMensual;
        }

        private static List<Amortizacion> CalcularTablaAmortizacion(double monto, double tasaAnual, int cuotas)
        {
            decimal saldoPendiente = (decimal)monto;
            decimal tasaMensual = (decimal)(tasaAnual / 12.0 / 100.0);

            // Fórmula de cuota fija (método francés)
            decimal cuota = saldoPendiente * tasaMensual /
                (1 - (decimal)Math.Pow(1 + (double)tasaMensual, -cuotas));

            var tabla = new List<Amortizacion>();

            for (int i = 1; i <= cuotas; i++)
            {
                decimal interes = saldoPendiente * tasaMensual;
                decimal capital = cuota - interes;
                saldoPendiente -= capital;

                tabla.Add(new Amortizacion
                {
                    IdAmortizacion = 0, // Se asignará luego al guardar
                    IdFactura = 0,      // Se asignará luego al guardar
                    NumeroCuota = i,
                    ValorCuota = Math.Round(cuota, 2),
                    InteresPagado = Math.Round(interes, 2),
                    CapitalPagado = Math.Round(capital, 2),
                    Saldo = Math.Round(saldoPendiente < 0 ? 0 : saldoPendiente, 2)
                });
            }

            return tabla;
        }


        private static void MostrarResumenCredito(double monto, double cuota, int cuotas)
        {
            double totalPagar = cuota * cuotas;
            double interesTotal = totalPagar - monto;

            Console.WriteLine($"\n===== RESUMEN DE CRÉDITO =====");
            Console.WriteLine($"Monto original: ${monto:F2}");
            Console.WriteLine($"Cuotas: {cuotas}");
            Console.WriteLine($"Cuota mensual: ${cuota:F2}");
            Console.WriteLine($"Total a pagar: ${totalPagar:F2}");
            Console.WriteLine($"Interés total: ${interesTotal:F2}");
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
