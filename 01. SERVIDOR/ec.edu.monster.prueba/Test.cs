using System;
using System.Collections.Generic;
using ec.edu.monster.modelo;
using ec.edu.monster.controlador;

namespace ec.edu.monster.prueba
{
    public class Test
    {
        public static void Main(string[] args)
        {
            Console.WriteLine("=== PRUEBAS DEL CONTROLADOR ===");

            var controller = new AeroCondorController();

            Console.WriteLine("---- Ciudades ----");
            var ciudades = controller.GetCiudades();
            foreach (var ciudad in ciudades)
            {
                Console.WriteLine($"{ciudad.IdCiudad} - {ciudad.CodigoCiudad} - {ciudad.NombreCiudad}");
            }

            Console.WriteLine("\n---- Usuarios ----");
            var usuario = new Usuarios
            {
                Nombre = "Prueba",
                Username = "prueba123",
                Password = "clave",
                Telefono = "0999999999",
                Cedula = "1234567890",
                Correo = "prueba@email.com"
            };

            var creado = controller.CrearUsuario(usuario);
            Console.WriteLine(creado ? "Usuario creado" : "Fallo al crear usuario");

            Console.WriteLine("---- Login ----");
            var login = controller.Login("prueba123", "clave");
            Console.WriteLine(login != null ? $"Login exitoso: {login.Nombre}" : "Login fallido");

            Console.WriteLine("\nFIN DE LAS PRUEBAS.");
            Console.ReadLine();
        }
    }
}
