﻿using System;
using System.Linq;
using System.Threading.Tasks;
using ec.edu.monster.controlador;
using ServiceReference1;

namespace ec.edu.monster.vista
{
    public class LoginView
    {
        private static readonly UsuarioController controller = new UsuarioController();

        public static async Task MostrarAsync()
        {
            while (true)
            {
                Console.WriteLine("===== BIENVENIDO MONSTER VIAJECITOS =====");
                Console.WriteLine("1. Iniciar Sesión");
                Console.WriteLine("2. Registrarse");
                Console.WriteLine("3. Salir");
                Console.Write("Seleccione una opción: ");
                string opcion = Console.ReadLine();

                switch (opcion)
                {
                    case "1":
                        await IniciarSesionAsync();
                        break;
                    case "2":
                        await RegistrarseAsync();
                        break;
                    case "3":
                        Console.WriteLine("¡Hasta luego!");
                        return;
                    default:
                        Console.WriteLine("Opción inválida\n");
                        break;
                }
            }
        }

        private static async Task IniciarSesionAsync()
        {
            Console.Write("Usuario: ");
            string username = Console.ReadLine()?.Trim();
            Console.Write("Contraseña: ");
            string password = Console.ReadLine()?.Trim();

            if (string.IsNullOrWhiteSpace(username) || string.IsNullOrWhiteSpace(password))
            {
                Console.WriteLine("Usuario y contraseña son obligatorios.\n");
                return;
            }

            var usuario = await controller.LoginAsync(username, password);

            if (usuario != null && usuario.IdUsuario > 0)
            {
                Console.WriteLine($"\nInicio de sesión exitoso. Bienvenido {usuario.Nombre}!\n");
                await MenuView.MostrarAsync(usuario);
            }
            else
            {
                Console.WriteLine("Credenciales inválidas.\n");
            }
        }

        private static async Task RegistrarseAsync()
        {
            var nuevo = new Usuarios();

            Console.Write("Nombre: ");
            nuevo.Nombre = Console.ReadLine()?.Trim();
            Console.Write("Username: ");
            nuevo.Username = Console.ReadLine()?.Trim();
            Console.Write("Contraseña: ");
            nuevo.Password = Console.ReadLine()?.Trim();
            Console.Write("Teléfono: ");
            nuevo.Telefono = Console.ReadLine()?.Trim();
            Console.Write("Cédula: ");
            nuevo.Cedula = Console.ReadLine()?.Trim();
            Console.Write("Correo: ");
            nuevo.Correo = Console.ReadLine()?.Trim();

            // Validaciones obligatorias
            if (string.IsNullOrWhiteSpace(nuevo.Nombre) ||
                string.IsNullOrWhiteSpace(nuevo.Username) ||
                string.IsNullOrWhiteSpace(nuevo.Password) ||
                string.IsNullOrWhiteSpace(nuevo.Cedula) ||
                string.IsNullOrWhiteSpace(nuevo.Correo))
            {
                Console.WriteLine("Nombre, username, contraseña, cédula y correo son obligatorios.\n");
                return;
            }

            // Validación de teléfono
            if (!string.IsNullOrWhiteSpace(nuevo.Telefono))
            {
                if (!nuevo.Telefono.All(char.IsDigit))
                {
                    Console.WriteLine("El teléfono debe contener solo números.\n");
                    return;
                }

                if (nuevo.Telefono.Length != 10)
                {
                    Console.WriteLine("El número de teléfono debe tener exactamente 10 dígitos.\n");
                    return;
                }
            }

            // Validación de cédula (solo números, longitud típica entre 8 y 10)
            if (!nuevo.Cedula.All(char.IsDigit) || nuevo.Cedula.Length < 8 || nuevo.Cedula.Length > 10)
            {
                Console.WriteLine("La cédula debe contener solo números y tener entre 8 y 10 dígitos.\n");
                return;
            }

            // Validación de correo electrónico básica
            if (!System.Text.RegularExpressions.Regex.IsMatch(nuevo.Correo!, @"^[^@\s]+@[^@\s]+\.[^@\s]+$"))
            {
                Console.WriteLine("El correo electrónico no tiene un formato válido.\n");
                return;
            }

            bool exito = await controller.CrearUsuarioAsync(nuevo);

            Console.WriteLine(exito ? "Usuario registrado exitosamente.\n" : "Error al registrar usuario. Puede que el username ya exista.\n");
        }

    }
}
