
-- Inserción de datos en la tabla de ciudades
INSERT INTO ciudades (codigo_ciudad, nombre_ciudad) VALUES
('UIO', 'Quito'),
('GYE', 'Guayaquil'),
('CUE', 'Cuenca'),
('MIA', 'Miami');
GO

-- Inserción de datos en la tabla de vuelos (usando IDs de ciudades)
INSERT INTO vuelos (codigo_vuelo, id_ciudad_origen, id_ciudad_destino, valor, hora_salida, capacidad, disponibles)
VALUES
('VUE001', 1, 2, 120.50, '2025-06-15 08:30:00', 150, 150),
('VUE002', 2, 1, 125.75, '2025-06-15 10:45:00', 150, 150),
('VUE003', 1, 3, 90.25, '2025-06-16 09:15:00', 120, 120),
('VUE004', 3, 1, 95.50, '2025-06-16 11:30:00', 120, 120),
('VUE005', 2, 3, 85.00, '2025-06-17 07:45:00', 100, 100),
('VUE006', 3, 2, 80.25, '2025-06-17 13:20:00', 100, 100),
('VUE007', 1, 4, 450.75, '2025-06-18 23:45:00', 200, 200),
('VUE008', 4, 1, 475.50, '2025-06-19 05:30:00', 200, 200);
GO

-- Inserción de datos en la tabla de usuarios
INSERT INTO usuarios (nombre, username, password, telefono)
VALUES ('MONSTER', 'MONSTER', 'MONSTER9', '0987654321');
GO
