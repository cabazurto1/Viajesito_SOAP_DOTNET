using System;
using System.Configuration;
using System.Data.SqlClient;

namespace ec.edu.monster.db
{
    public static class ConexionBD
    {
        private static readonly string connectionString =
            ConfigurationManager.ConnectionStrings["aerolineas_condor_db"]?.ConnectionString;

        public static SqlConnection ObtenerConexion()
        {
            if (string.IsNullOrWhiteSpace(connectionString))
                throw new InvalidOperationException("La cadena de conexión 'aerolineas_condor_db' no está configurada.");

            var cn = new SqlConnection(connectionString);
            try
            {
                cn.Open();
                return cn;
            }
            catch (SqlException ex)
            {
                throw new Exception("Error al conectar a la base de datos: " + ex.Message);
            }
        }
    }
}
