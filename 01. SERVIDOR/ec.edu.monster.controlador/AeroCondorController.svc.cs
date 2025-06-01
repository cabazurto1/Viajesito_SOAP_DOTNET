using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using ec.edu.monster.controlador;
using ec.edu.monster.db;
using ec.edu.monster.modelo;

namespace ec.edu.monster.servicio
{
    public class AeroCondorController : IAeroCondorController
    {
        public List<Ciudades> GetCiudades()
        {
            var lista = new List<Ciudades>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM ciudades", cn);
                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Ciudades
                    {
                        IdCiudad = (int)dr["id_ciudad"],
                        CodigoCiudad = dr["codigo_ciudad"].ToString(),
                        NombreCiudad = dr["nombre_ciudad"].ToString()
                    });
                }
            }
            return lista;
        }

        public List<Vuelos> GetVuelos()
        {
            var lista = new List<Vuelos>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM vuelos", cn);
                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Vuelos
                    {
                        IdVuelo = (int)dr["id_vuelo"],
                        CodigoVuelo = dr["codigo_vuelo"].ToString(),
                        Valor = (decimal)dr["valor"],
                        HoraSalida = (DateTime)dr["hora_salida"],
                        Capacidad = (int)dr["capacidad"],
                        Disponibles = (int)dr["disponibles"],
                        IdCiudadOrigen = (int)dr["id_ciudad_origen"],
                        IdCiudadDestino = (int)dr["id_ciudad_destino"]
                    });
                }
            }
            return lista;
        }

        public List<Usuarios> GetUsuarios()
        {
            var lista = new List<Usuarios>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM usuarios", cn);
                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Usuarios
                    {
                        IdUsuario = (int)dr["id_usuario"],
                        Nombre = dr["nombre"].ToString(),
                        Username = dr["username"].ToString(),
                        Password = dr["password"].ToString(),
                        Telefono = dr["telefono"].ToString()
                    });
                }
            }
            return lista;
        }

        public List<Boletos> GetBoletos()
        {
            var lista = new List<Boletos>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM boletos", cn);
                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Boletos
                    {
                        IdBoleto = (int)dr["id_boleto"],
                        NumeroBoleto = dr["numero_boleto"].ToString(),
                        FechaCompra = (DateTime)dr["fecha_compra"],
                        PrecioCompra = (decimal)dr["precio_compra"],
                        IdUsuario = (int)dr["id_usuario"],
                        IdVuelo = (int)dr["id_vuelo"]
                    });
                }
            }
            return lista;
        }
        public Usuarios ObtenerUsuarioPorId(int id)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM usuarios WHERE id_usuario = @id", cn);
                cmd.Parameters.AddWithValue("@id", id);
                var dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    return new Usuarios
                    {
                        IdUsuario = (int)dr["id_usuario"],
                        Nombre = dr["nombre"].ToString(),
                        Username = dr["username"].ToString(),
                        Password = dr["password"].ToString(),
                        Telefono = dr["telefono"].ToString()
                    };
                }
            }
            return null;
        }

        public bool CrearUsuario(Usuarios usuario)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
            INSERT INTO usuarios (nombre, username, password, telefono) 
            VALUES (@nombre, @username, @password, @telefono)", cn);

                cmd.Parameters.AddWithValue("@nombre", usuario.Nombre);
                cmd.Parameters.AddWithValue("@username", usuario.Username);
                cmd.Parameters.AddWithValue("@password", usuario.Password);
                cmd.Parameters.AddWithValue("@telefono", usuario.Telefono ?? "");

                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public bool EditarUsuario(Usuarios usuario)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
            UPDATE usuarios 
            SET nombre = @nombre, username = @username, password = @password, telefono = @telefono 
            WHERE id_usuario = @id", cn);

                cmd.Parameters.AddWithValue("@nombre", usuario.Nombre);
                cmd.Parameters.AddWithValue("@username", usuario.Username);
                cmd.Parameters.AddWithValue("@password", usuario.Password);
                cmd.Parameters.AddWithValue("@telefono", usuario.Telefono ?? "");
                cmd.Parameters.AddWithValue("@id", usuario.IdUsuario);

                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public bool EliminarUsuario(int id)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("DELETE FROM usuarios WHERE id_usuario = @id", cn);
                cmd.Parameters.AddWithValue("@id", id);
                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public int ContarUsuarios()
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT COUNT(*) FROM usuarios", cn);
                return (int)cmd.ExecuteScalar();
            }
        }

        public List<Usuarios> ListarUsuariosPorRango(int desde, int hasta)
        {
            var lista = new List<Usuarios>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
            SELECT * FROM (
                SELECT ROW_NUMBER() OVER (ORDER BY id_usuario) AS RowNum, * 
                FROM usuarios
            ) AS Sub
            WHERE RowNum BETWEEN @desde AND @hasta", cn);

                cmd.Parameters.AddWithValue("@desde", desde);
                cmd.Parameters.AddWithValue("@hasta", hasta);

                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Usuarios
                    {
                        IdUsuario = (int)dr["id_usuario"],
                        Nombre = dr["nombre"].ToString(),
                        Username = dr["username"].ToString(),
                        Password = dr["password"].ToString(),
                        Telefono = dr["telefono"].ToString()
                    });
                }
            }
            return lista;
        }


        public bool Comprar(CompraBoletoRequest request)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                for (int i = 0; i < request.Cantidad; i++)
                {
                    var cmd = new SqlCommand(@"
                        INSERT INTO boletos (numero_boleto, id_vuelo, id_usuario, precio_compra)
                        VALUES (@num, @vuelo, @usuario, 
                            (SELECT valor FROM vuelos WHERE id_vuelo = @vuelo))", cn);

                    cmd.Parameters.AddWithValue("@num", Guid.NewGuid().ToString().Substring(0, 10).ToUpper());
                    cmd.Parameters.AddWithValue("@vuelo", request.IdVuelo);
                    cmd.Parameters.AddWithValue("@usuario", request.IdUsuario);
                    if (cmd.ExecuteNonQuery() <= 0)
                        return false;
                }
                return true;
            }
        }

        public Usuarios Login(string username, string password)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM usuarios WHERE username = @u AND password = @p", cn);
                cmd.Parameters.AddWithValue("@u", username);
                cmd.Parameters.AddWithValue("@p", password);
                var dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    return new Usuarios
                    {
                        IdUsuario = (int)dr["id_usuario"],
                        Nombre = dr["nombre"].ToString(),
                        Username = dr["username"].ToString(),
                        Password = dr["password"].ToString(),
                        Telefono = dr["telefono"].ToString()
                    };
                }
            }
            return null;
        }

        public Vuelos ObtenerVueloPorId(int id)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM vuelos WHERE id_vuelo = @id", cn);
                cmd.Parameters.AddWithValue("@id", id);
                var dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    return new Vuelos
                    {
                        IdVuelo = (int)dr["id_vuelo"],
                        CodigoVuelo = dr["codigo_vuelo"].ToString(),
                        Valor = (decimal)dr["valor"],
                        HoraSalida = (DateTime)dr["hora_salida"],
                        Capacidad = (int)dr["capacidad"],
                        Disponibles = (int)dr["disponibles"],
                        IdCiudadOrigen = (int)dr["id_ciudad_origen"],
                        IdCiudadDestino = (int)dr["id_ciudad_destino"]
                    };
                }
            }
            return null;
        }

        public bool CrearVuelo(Vuelos vuelo)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
            INSERT INTO vuelos (codigo_vuelo, valor, hora_salida, capacidad, disponibles, id_ciudad_origen, id_ciudad_destino)
            VALUES (@codigo, @valor, @hora, @capacidad, @disponibles, @origen, @destino)", cn);

                cmd.Parameters.AddWithValue("@codigo", vuelo.CodigoVuelo);
                cmd.Parameters.AddWithValue("@valor", vuelo.Valor);
                cmd.Parameters.AddWithValue("@hora", vuelo.HoraSalida);
                cmd.Parameters.AddWithValue("@capacidad", vuelo.Capacidad);
                cmd.Parameters.AddWithValue("@disponibles", vuelo.Disponibles);
                cmd.Parameters.AddWithValue("@origen", vuelo.IdCiudadOrigen);
                cmd.Parameters.AddWithValue("@destino", vuelo.IdCiudadDestino);

                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public bool EditarVuelo(Vuelos vuelo)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
            UPDATE vuelos
            SET codigo_vuelo = @codigo, valor = @valor, hora_salida = @hora,
                capacidad = @capacidad, disponibles = @disponibles,
                id_ciudad_origen = @origen, id_ciudad_destino = @destino
            WHERE id_vuelo = @id", cn);

                cmd.Parameters.AddWithValue("@codigo", vuelo.CodigoVuelo);
                cmd.Parameters.AddWithValue("@valor", vuelo.Valor);
                cmd.Parameters.AddWithValue("@hora", vuelo.HoraSalida);
                cmd.Parameters.AddWithValue("@capacidad", vuelo.Capacidad);
                cmd.Parameters.AddWithValue("@disponibles", vuelo.Disponibles);
                cmd.Parameters.AddWithValue("@origen", vuelo.IdCiudadOrigen);
                cmd.Parameters.AddWithValue("@destino", vuelo.IdCiudadDestino);
                cmd.Parameters.AddWithValue("@id", vuelo.IdVuelo);

                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public bool EliminarVuelo(int id)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("DELETE FROM vuelos WHERE id_vuelo = @id", cn);
                cmd.Parameters.AddWithValue("@id", id);
                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public int ContarVuelos()
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT COUNT(*) FROM vuelos", cn);
                return (int)cmd.ExecuteScalar();
            }
        }

        public List<Vuelos> ListarVuelosPorRango(int desde, int hasta)
        {
            var lista = new List<Vuelos>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
            SELECT * FROM (
                SELECT *, ROW_NUMBER() OVER (ORDER BY id_vuelo) AS fila
                FROM vuelos
            ) AS sub
            WHERE fila BETWEEN @desde AND @hasta", cn);

                cmd.Parameters.AddWithValue("@desde", desde);
                cmd.Parameters.AddWithValue("@hasta", hasta);

                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Vuelos
                    {
                        IdVuelo = (int)dr["id_vuelo"],
                        CodigoVuelo = dr["codigo_vuelo"].ToString(),
                        Valor = (decimal)dr["valor"],
                        HoraSalida = (DateTime)dr["hora_salida"],
                        Capacidad = (int)dr["capacidad"],
                        Disponibles = (int)dr["disponibles"],
                        IdCiudadOrigen = (int)dr["id_ciudad_origen"],
                        IdCiudadDestino = (int)dr["id_ciudad_destino"]
                    });
                }
            }
            return lista;
        }

        public Ciudades ObtenerCiudadPorId(int id)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM ciudades WHERE id_ciudad = @id", cn);
                cmd.Parameters.AddWithValue("@id", id);
                var dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    return new Ciudades
                    {
                        IdCiudad = (int)dr["id_ciudad"],
                        CodigoCiudad = dr["codigo_ciudad"].ToString(),
                        NombreCiudad = dr["nombre_ciudad"].ToString()
                    };
                }
            }
            return null;
        }

        public bool CrearCiudad(Ciudades ciudad)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("INSERT INTO ciudades (codigo_ciudad, nombre_ciudad) VALUES (@codigo, @nombre)", cn);
                cmd.Parameters.AddWithValue("@codigo", ciudad.CodigoCiudad);
                cmd.Parameters.AddWithValue("@nombre", ciudad.NombreCiudad);
                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public bool EditarCiudad(Ciudades ciudad)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("UPDATE ciudades SET codigo_ciudad = @codigo, nombre_ciudad = @nombre WHERE id_ciudad = @id", cn);
                cmd.Parameters.AddWithValue("@codigo", ciudad.CodigoCiudad);
                cmd.Parameters.AddWithValue("@nombre", ciudad.NombreCiudad);
                cmd.Parameters.AddWithValue("@id", ciudad.IdCiudad);
                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public bool EliminarCiudad(int id)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("DELETE FROM ciudades WHERE id_ciudad = @id", cn);
                cmd.Parameters.AddWithValue("@id", id);
                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public int ContarCiudades()
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT COUNT(*) FROM ciudades", cn);
                return (int)cmd.ExecuteScalar();
            }
        }

        public List<Ciudades> ListarCiudadesPorRango(int desde, int hasta)
        {
            var lista = new List<Ciudades>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
            SELECT * FROM (
                SELECT *, ROW_NUMBER() OVER (ORDER BY id_ciudad) AS fila
                FROM ciudades
            ) AS sub
            WHERE fila BETWEEN @desde AND @hasta", cn);

                cmd.Parameters.AddWithValue("@desde", desde);
                cmd.Parameters.AddWithValue("@hasta", hasta);

                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Ciudades
                    {
                        IdCiudad = (int)dr["id_ciudad"],
                        CodigoCiudad = dr["codigo_ciudad"].ToString(),
                        NombreCiudad = dr["nombre_ciudad"].ToString()
                    });
                }
            }
            return lista;
        }
        public Boletos ObtenerBoletoPorId(int id)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM boletos WHERE id_boleto = @id", cn);
                cmd.Parameters.AddWithValue("@id", id);
                var dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    return new Boletos
                    {
                        IdBoleto = (int)dr["id_boleto"],
                        NumeroBoleto = dr["numero_boleto"].ToString(),
                        FechaCompra = (DateTime)dr["fecha_compra"],
                        PrecioCompra = (decimal)dr["precio_compra"],
                        IdUsuario = (int)dr["id_usuario"],
                        IdVuelo = (int)dr["id_vuelo"]
                    };
                }
            }
            return null;
        }

        public bool CrearBoleto(Boletos boleto)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"INSERT INTO boletos 
            (numero_boleto, fecha_compra, precio_compra, id_usuario, id_vuelo) 
            VALUES (@num, @fecha, @precio, @usuario, @vuelo)", cn);

                cmd.Parameters.AddWithValue("@num", boleto.NumeroBoleto);
                cmd.Parameters.AddWithValue("@fecha", boleto.FechaCompra);
                cmd.Parameters.AddWithValue("@precio", boleto.PrecioCompra);
                cmd.Parameters.AddWithValue("@usuario", boleto.IdUsuario);
                cmd.Parameters.AddWithValue("@vuelo", boleto.IdVuelo);

                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public bool EditarBoleto(Boletos boleto)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"UPDATE boletos SET 
            numero_boleto = @num, fecha_compra = @fecha, precio_compra = @precio,
            id_usuario = @usuario, id_vuelo = @vuelo WHERE id_boleto = @id", cn);

                cmd.Parameters.AddWithValue("@num", boleto.NumeroBoleto);
                cmd.Parameters.AddWithValue("@fecha", boleto.FechaCompra);
                cmd.Parameters.AddWithValue("@precio", boleto.PrecioCompra);
                cmd.Parameters.AddWithValue("@usuario", boleto.IdUsuario);
                cmd.Parameters.AddWithValue("@vuelo", boleto.IdVuelo);
                cmd.Parameters.AddWithValue("@id", boleto.IdBoleto);

                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public bool EliminarBoleto(int id)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("DELETE FROM boletos WHERE id_boleto = @id", cn);
                cmd.Parameters.AddWithValue("@id", id);
                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public int ContarBoletos()
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT COUNT(*) FROM boletos", cn);
                return (int)cmd.ExecuteScalar();
            }
        }

        public List<Boletos> ListarBoletosPorRango(int desde, int hasta)
        {
            var lista = new List<Boletos>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
            SELECT * FROM (
                SELECT *, ROW_NUMBER() OVER (ORDER BY id_boleto) AS fila
                FROM boletos
            ) AS sub
            WHERE fila BETWEEN @desde AND @hasta", cn);

                cmd.Parameters.AddWithValue("@desde", desde);
                cmd.Parameters.AddWithValue("@hasta", hasta);

                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Boletos
                    {
                        IdBoleto = (int)dr["id_boleto"],
                        NumeroBoleto = dr["numero_boleto"].ToString(),
                        FechaCompra = (DateTime)dr["fecha_compra"],
                        PrecioCompra = (decimal)dr["precio_compra"],
                        IdUsuario = (int)dr["id_usuario"],
                        IdVuelo = (int)dr["id_vuelo"]
                    });
                }
            }
            return lista;
        }

        public List<Boletos> ObtenerBoletosPorUsuario(int idUsuario)
        {
            var lista = new List<Boletos>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM boletos WHERE id_usuario = @id", cn);
                cmd.Parameters.AddWithValue("@id", idUsuario);
                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Boletos
                    {
                        IdBoleto = (int)dr["id_boleto"],
                        NumeroBoleto = dr["numero_boleto"].ToString(),
                        FechaCompra = (DateTime)dr["fecha_compra"],
                        PrecioCompra = (decimal)dr["precio_compra"],
                        IdUsuario = (int)dr["id_usuario"],
                        IdVuelo = (int)dr["id_vuelo"]
                    });
                }
            }
            return lista;
        }


        public List<Vuelos> BuscarVuelos(string origen, string destino, DateTime fechaSalida)
        {
            var lista = new List<Vuelos>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
                    SELECT * FROM vuelos 
                    WHERE id_ciudad_origen = (SELECT id_ciudad FROM ciudades WHERE codigo_ciudad = @origen)
                      AND id_ciudad_destino = (SELECT id_ciudad FROM ciudades WHERE codigo_ciudad = @destino)
                      AND CAST(hora_salida AS DATE) = @fecha
                    ORDER BY valor DESC", cn);

                cmd.Parameters.AddWithValue("@origen", origen);
                cmd.Parameters.AddWithValue("@destino", destino);
                cmd.Parameters.AddWithValue("@fecha", fechaSalida.Date);

                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Vuelos
                    {
                        IdVuelo = (int)dr["id_vuelo"],
                        CodigoVuelo = dr["codigo_vuelo"].ToString(),
                        Valor = (decimal)dr["valor"],
                        HoraSalida = (DateTime)dr["hora_salida"],
                        Capacidad = (int)dr["capacidad"],
                        Disponibles = (int)dr["disponibles"],
                        IdCiudadOrigen = (int)dr["id_ciudad_origen"],
                        IdCiudadDestino = (int)dr["id_ciudad_destino"]
                    });
                }
            }
            return lista;
        }
    }

}
