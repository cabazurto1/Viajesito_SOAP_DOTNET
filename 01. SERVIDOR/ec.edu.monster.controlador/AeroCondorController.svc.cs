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

                var vuelosTemp = new List<Vuelos>();
                while (dr.Read())
                {
                    vuelosTemp.Add(new Vuelos
                    {
                        IdVuelo = (int)dr["id_vuelo"],
                        CodigoVuelo = dr["codigo_vuelo"].ToString(),
                        Valor = (decimal)dr["valor"],
                        HoraSalida = (DateTime)dr["hora_salida"],
                        Capacidad = (int)dr["capacidad"],
                        IdCiudadOrigen = (int)dr["id_ciudad_origen"],
                        IdCiudadDestino = (int)dr["id_ciudad_destino"]
                    });
                }

                dr.Close();

                foreach (var vuelo in vuelosTemp)
                {
                    int vendidos = ObtenerBoletosVendidos(vuelo.IdVuelo);
                    vuelo.Disponibles = vuelo.Capacidad - vendidos;
                    lista.Add(vuelo);
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
                        Telefono = dr["telefono"].ToString(),
                        Cedula = dr["cedula"].ToString(),
                        Correo = dr["correo"].ToString()
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
                        Telefono = dr["telefono"].ToString(),
                        Cedula = dr["cedula"].ToString(),
                        Correo = dr["correo"].ToString()
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
            INSERT INTO usuarios (nombre, username, password, telefono, cedula, correo) 
            VALUES (@nombre, @username, @password, @telefono, @cedula, @correo)", cn);

                cmd.Parameters.AddWithValue("@nombre", usuario.Nombre);
                cmd.Parameters.AddWithValue("@username", usuario.Username);
                cmd.Parameters.AddWithValue("@password", usuario.Password);
                cmd.Parameters.AddWithValue("@telefono", usuario.Telefono ?? "");
                cmd.Parameters.AddWithValue("@cedula", usuario.Cedula ?? "");
                cmd.Parameters.AddWithValue("@correo", usuario.Correo ?? "sincorreo@correo.com");

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
        private List<Amortizacion> GenerarTablaAmortizacion(decimal monto, double tasaAnual, int cuotas, int idFactura)
        {
            var lista = new List<Amortizacion>();
            double tasaMensual = tasaAnual / 12 / 100;
            decimal saldo = monto;

            decimal cuota = monto * (decimal)(tasaMensual / (1 - Math.Pow(1 + tasaMensual, -cuotas)));

            for (int i = 1; i <= cuotas; i++)
            {
                decimal interes = saldo * (decimal)tasaMensual;
                decimal capital = cuota - interes;
                saldo -= capital;

                lista.Add(new Amortizacion
                {
                    IdFactura = idFactura,
                    NumeroCuota = i,
                    ValorCuota = Math.Round(cuota, 2),
                    InteresPagado = Math.Round(interes, 2),
                    CapitalPagado = Math.Round(capital, 2),
                    Saldo = Math.Round(Math.Max(saldo, 0), 2)
                });
            }

            return lista;
        }
        public List<Amortizacion> ObtenerAmortizacionPorFactura(int idFactura)
        {
            var lista = new List<Amortizacion>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
            SELECT id_amortizacion, id_factura, numero_cuota, 
                   valor_cuota, interes_pagado, capital_pagado, saldo
            FROM amortizacion_boletos
            WHERE id_factura = @id
            ORDER BY numero_cuota", cn);

                cmd.Parameters.AddWithValue("@id", idFactura);
                var dr = cmd.ExecuteReader();

                while (dr.Read())
                {
                    lista.Add(new Amortizacion
                    {
                        IdAmortizacion = (int)dr["id_amortizacion"],
                        IdFactura = (int)dr["id_factura"],
                        NumeroCuota = (int)dr["numero_cuota"],
                        ValorCuota = (decimal)dr["valor_cuota"],
                        InteresPagado = (decimal)dr["interes_pagado"],
                        CapitalPagado = (decimal)dr["capital_pagado"],
                        Saldo = (decimal)dr["saldo"]
                    });
                }
            }
            return lista;
        }


        public bool Comprar(CompraBoletoRequest request)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            using (var trans = cn.BeginTransaction())
            {
                try
                {
                    decimal totalSinIVA = 0;

                    // Calcular total sin IVA por todos los vuelos
                    foreach (var vueloCompra in request.Vuelos)
                    {
                        var precioCmd = new SqlCommand("SELECT valor FROM vuelos WHERE id_vuelo = @id", cn, trans);
                        precioCmd.Parameters.AddWithValue("@id", vueloCompra.IdVuelo);
                        var result = precioCmd.ExecuteScalar();
                        if (result == null) throw new Exception("Vuelo no encontrado");

                        decimal precioUnitario = (decimal)result;
                        totalSinIVA += precioUnitario * vueloCompra.Cantidad;
                    }

                    decimal totalConIVA = totalSinIVA * 1.15m; // 15% IVA

                    // Obtener número de factura
                    var getLastFacturaCmd = new SqlCommand("SELECT MAX(id_factura) FROM facturas", cn, trans);
                    var lastFacturaObj = getLastFacturaCmd.ExecuteScalar();
                    int nextFacturaNum = (lastFacturaObj != DBNull.Value) ? Convert.ToInt32(lastFacturaObj) + 1 : 1;
                    string numeroFactura = "FAC-" + nextFacturaNum.ToString("D9");

                    // Insertar factura
                    var facturaCmd = new SqlCommand(@"
                INSERT INTO facturas (numero_factura, id_usuario, precio_sin_iva, precio_con_iva)
                VALUES (@numero, @usuario, @siniva, @coniva);
                SELECT SCOPE_IDENTITY();", cn, trans);

                    facturaCmd.Parameters.AddWithValue("@numero", numeroFactura);
                    facturaCmd.Parameters.AddWithValue("@usuario", request.IdUsuario);
                    facturaCmd.Parameters.AddWithValue("@siniva", totalSinIVA);
                    facturaCmd.Parameters.AddWithValue("@coniva", totalConIVA);

                    int idFactura = Convert.ToInt32(facturaCmd.ExecuteScalar());

                    // Si es pago a crédito, generar y guardar la tabla de amortización
                    if (request.EsCredito)
                    {
                        var amortizaciones = GenerarTablaAmortizacion(totalConIVA, request.TasaInteresAnual, request.NumeroCuotas, idFactura);

                        foreach (var cuota in amortizaciones)
                        {
                            var amortCmd = new SqlCommand(@"
                        INSERT INTO amortizacion_boletos 
                        (id_factura, numero_cuota, valor_cuota, interes_pagado, capital_pagado, saldo)
                        VALUES (@factura, @num, @valor, @interes, @capital, @saldo)", cn, trans);

                            amortCmd.Parameters.AddWithValue("@factura", cuota.IdFactura);
                            amortCmd.Parameters.AddWithValue("@num", cuota.NumeroCuota);
                            amortCmd.Parameters.AddWithValue("@valor", cuota.ValorCuota);
                            amortCmd.Parameters.AddWithValue("@interes", cuota.InteresPagado);
                            amortCmd.Parameters.AddWithValue("@capital", cuota.CapitalPagado);
                            amortCmd.Parameters.AddWithValue("@saldo", cuota.Saldo);

                            amortCmd.ExecuteNonQuery();
                        }
                    }

                    // Procesar cada vuelo
                    foreach (var vueloCompra in request.Vuelos)
                    {
                        // Obtener precio unitario
                        decimal precioUnitario;
                        var precioCmd = new SqlCommand("SELECT valor FROM vuelos WHERE id_vuelo = @id", cn, trans);
                        precioCmd.Parameters.AddWithValue("@id", vueloCompra.IdVuelo);
                        var result = precioCmd.ExecuteScalar();
                        if (result == null) throw new Exception("Vuelo no encontrado");
                        precioUnitario = (decimal)result;

                        // Insertar boletos
                        for (int i = 0; i < vueloCompra.Cantidad; i++)
                        {
                            var boletoCmd = new SqlCommand(@"
                        INSERT INTO boletos (numero_boleto, id_vuelo, id_usuario, precio_compra, id_factura)
                        VALUES (@num, @vuelo, @usuario, @precio, @factura)", cn, trans);

                            boletoCmd.Parameters.AddWithValue("@num", Guid.NewGuid().ToString().Substring(0, 10).ToUpper());
                            boletoCmd.Parameters.AddWithValue("@vuelo", vueloCompra.IdVuelo);
                            boletoCmd.Parameters.AddWithValue("@usuario", request.IdUsuario);
                            boletoCmd.Parameters.AddWithValue("@precio", precioUnitario);
                            boletoCmd.Parameters.AddWithValue("@factura", idFactura);

                            if (boletoCmd.ExecuteNonQuery() <= 0)
                            {
                                trans.Rollback();
                                return false;
                            }
                        }

                        // Actualizar cupos disponibles
                        var updateCmd = new SqlCommand(@"
                    UPDATE vuelos 
                    SET disponibles = disponibles - @cantidad 
                    WHERE id_vuelo = @vuelo AND disponibles >= @cantidad", cn, trans);

                        updateCmd.Parameters.AddWithValue("@cantidad", vueloCompra.Cantidad);
                        updateCmd.Parameters.AddWithValue("@vuelo", vueloCompra.IdVuelo);

                        if (updateCmd.ExecuteNonQuery() <= 0)
                        {
                            trans.Rollback();
                            return false;
                        }
                    }

                    trans.Commit();
                    return true;
                }
                catch (Exception ex)
                {
                    trans.Rollback();
                    Console.WriteLine("Error en compra: " + ex.Message);
                    return false;
                }
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
                        Telefono = dr["telefono"].ToString(),
                        Cedula = dr["cedula"].ToString(),
                        Correo = dr["correo"].ToString()
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
        public int ObtenerBoletosVendidos(int idVuelo)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT COUNT(*) FROM boletos WHERE id_vuelo = @id", cn);
                cmd.Parameters.AddWithValue("@id", idVuelo);
                return (int)cmd.ExecuteScalar();
            }
        }

        // Agregar implementación para Facturas en AeroCondorController

        public List<Facturas> GetFacturas()
        {
            var lista = new List<Facturas>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM facturas", cn);
                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Facturas
                    {
                        IdFactura = (int)dr["id_factura"],
                        NumeroFactura = dr["numero_factura"].ToString(),
                        IdUsuario = (int)dr["id_usuario"],
                        PrecioSinIVA = (decimal)dr["precio_sin_iva"],
                        PrecioConIVA = (decimal)dr["precio_con_iva"],
                        FechaFactura = (DateTime)dr["fecha_factura"]
                    });
                }
            }
            return lista;
        }

        public List<Facturas> GetFacturasPorUsuario(int idUsuario)
        {
            var lista = new List<Facturas>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM facturas WHERE id_usuario = @idUsuario", cn);
                cmd.Parameters.AddWithValue("@idUsuario", idUsuario);
                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Facturas
                    {
                        IdFactura = (int)dr["id_factura"],
                        NumeroFactura = dr["numero_factura"].ToString(),
                        IdUsuario = (int)dr["id_usuario"],
                        PrecioSinIVA = (decimal)dr["precio_sin_iva"],
                        PrecioConIVA = (decimal)dr["precio_con_iva"],
                        FechaFactura = (DateTime)dr["fecha_factura"]
                    });
                }
            }
            return lista;
        }


        public Facturas ObtenerFacturaPorId(int id)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                Facturas factura = null;

                // 1. Obtener datos de la factura
                var cmd = new SqlCommand("SELECT * FROM facturas WHERE id_factura = @id", cn);
                cmd.Parameters.AddWithValue("@id", id);
                var dr = cmd.ExecuteReader();
                if (dr.Read())
                {
                    factura = new Facturas
                    {
                        IdFactura = (int)dr["id_factura"],
                        NumeroFactura = dr["numero_factura"].ToString(),
                        IdUsuario = (int)dr["id_usuario"],
                        PrecioSinIVA = (decimal)dr["precio_sin_iva"],
                        PrecioConIVA = (decimal)dr["precio_con_iva"],
                        FechaFactura = (DateTime)dr["fecha_factura"],
                        BoletosRelacionados = new List<Boletos>()
                    };
                }
                dr.Close(); // Necesario para ejecutar otro SqlCommand en la misma conexión

                if (factura != null)
                {
                    // 2. Obtener boletos relacionados
                    var boletosCmd = new SqlCommand("SELECT * FROM boletos WHERE id_factura = @id", cn);
                    boletosCmd.Parameters.AddWithValue("@id", id);
                    var drBoletos = boletosCmd.ExecuteReader();
                    while (drBoletos.Read())
                    {
                        factura.BoletosRelacionados.Add(new Boletos
                        {
                            IdBoleto = (int)drBoletos["id_boleto"],
                            NumeroBoleto = drBoletos["numero_boleto"].ToString(),
                            FechaCompra = (DateTime)drBoletos["fecha_compra"],
                            PrecioCompra = (decimal)drBoletos["precio_compra"],
                            IdUsuario = (int)drBoletos["id_usuario"],
                            IdVuelo = (int)drBoletos["id_vuelo"],
                            IdFactura = (int)drBoletos["id_factura"]
                        });
                    }
                    drBoletos.Close();
                }

                return factura;
            }
        }


        public bool CrearFactura(Facturas factura)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"INSERT INTO facturas (numero_factura, id_usuario, precio_sin_iva, precio_con_iva) 
                                    VALUES (@num, @usuario, @siniva, @coniva)", cn);
                cmd.Parameters.AddWithValue("@num", factura.NumeroFactura);
                cmd.Parameters.AddWithValue("@usuario", factura.IdUsuario);
                cmd.Parameters.AddWithValue("@siniva", factura.PrecioSinIVA);
                cmd.Parameters.AddWithValue("@coniva", factura.PrecioConIVA);

                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public bool EditarFactura(Facturas factura)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"UPDATE facturas SET 
                                    numero_factura = @num, id_usuario = @usuario, 
                                    precio_sin_iva = @siniva, precio_con_iva = @coniva 
                                  WHERE id_factura = @id", cn);
                cmd.Parameters.AddWithValue("@num", factura.NumeroFactura);
                cmd.Parameters.AddWithValue("@usuario", factura.IdUsuario);
                cmd.Parameters.AddWithValue("@siniva", factura.PrecioSinIVA);
                cmd.Parameters.AddWithValue("@coniva", factura.PrecioConIVA);
                cmd.Parameters.AddWithValue("@id", factura.IdFactura);

                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public bool EliminarFactura(int id)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("DELETE FROM facturas WHERE id_factura = @id", cn);
                cmd.Parameters.AddWithValue("@id", id);
                return cmd.ExecuteNonQuery() > 0;
            }
        }

        public int ContarFacturas()
        {
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT COUNT(*) FROM facturas", cn);
                return (int)cmd.ExecuteScalar();
            }
        }

        public List<Facturas> ListarFacturasPorRango(int desde, int hasta)
        {
            var lista = new List<Facturas>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand(@"
            SELECT * FROM (
                SELECT *, ROW_NUMBER() OVER (ORDER BY id_factura) AS fila
                FROM facturas
            ) AS sub
            WHERE fila BETWEEN @desde AND @hasta", cn);

                cmd.Parameters.AddWithValue("@desde", desde);
                cmd.Parameters.AddWithValue("@hasta", hasta);

                var dr = cmd.ExecuteReader();
                while (dr.Read())
                {
                    lista.Add(new Facturas
                    {
                        IdFactura = (int)dr["id_factura"],
                        NumeroFactura = dr["numero_factura"].ToString(),
                        IdUsuario = (int)dr["id_usuario"],
                        PrecioSinIVA = (decimal)dr["precio_sin_iva"],
                        PrecioConIVA = (decimal)dr["precio_con_iva"],
                        FechaFactura = (DateTime)dr["fecha_factura"]
                    });
                }
            }
            return lista;
        }

        public List<Boletos> ObtenerBoletosDeFactura(int idFactura)
        {
            var lista = new List<Boletos>();
            using (var cn = ConexionBD.ObtenerConexion())
            {
                var cmd = new SqlCommand("SELECT * FROM boletos WHERE id_factura = @id", cn);
                cmd.Parameters.AddWithValue("@id", idFactura);
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
                        IdVuelo = (int)dr["id_vuelo"],
                        IdFactura = dr["id_factura"] == DBNull.Value ? null : (int?)dr["id_factura"]
                    });
                }
            }
            return lista;
        }

        public bool AsociarBoletosAFactura(int idFactura, List<int> idsBoletos)
        {
            using (var cn = ConexionBD.ObtenerConexion())
            using (var trans = cn.BeginTransaction())
            {
                try
                {
                    foreach (int idBoleto in idsBoletos)
                    {
                        var cmd = new SqlCommand("UPDATE boletos SET id_factura = @idFactura WHERE id_boleto = @idBoleto", cn, trans);
                        cmd.Parameters.AddWithValue("@idFactura", idFactura);
                        cmd.Parameters.AddWithValue("@idBoleto", idBoleto);

                        if (cmd.ExecuteNonQuery() <= 0)
                        {
                            trans.Rollback();
                            return false;
                        }
                    }
                    trans.Commit();
                    return true;
                }
                catch
                {
                    trans.Rollback();
                    return false;
                }
            }
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
