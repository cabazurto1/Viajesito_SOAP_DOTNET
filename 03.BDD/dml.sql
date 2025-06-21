-- Tabla de Ciudades
IF OBJECT_ID('ciudades', 'U') IS NULL
CREATE TABLE ciudades (
    id_ciudad INT IDENTITY(1,1) PRIMARY KEY,
    codigo_ciudad VARCHAR(3) NOT NULL UNIQUE,
    nombre_ciudad VARCHAR(100) NOT NULL
);
GO

-- Tabla de Vuelos
IF OBJECT_ID('vuelos', 'U') IS NULL
CREATE TABLE vuelos (
    id_vuelo INT IDENTITY(1,1) PRIMARY KEY,
    codigo_vuelo VARCHAR(10) NOT NULL UNIQUE,
    id_ciudad_origen INT NOT NULL,
    id_ciudad_destino INT NOT NULL,
    valor NUMERIC(7,2) NOT NULL,
    hora_salida DATETIME NOT NULL,
    capacidad INT NOT NULL,
    disponibles INT NOT NULL,
    FOREIGN KEY (id_ciudad_origen) REFERENCES ciudades(id_ciudad),
    FOREIGN KEY (id_ciudad_destino) REFERENCES ciudades(id_ciudad)
);
GO

-- Tabla de Usuarios
IF OBJECT_ID('usuarios', 'U') IS NULL
CREATE TABLE usuarios (
    id_usuario INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    username VARCHAR(200) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    telefono VARCHAR(15)
);
GO

-- Tabla de Boletos
IF OBJECT_ID('boletos', 'U') IS NULL
CREATE TABLE boletos (
    id_boleto INT IDENTITY(1,1) PRIMARY KEY,
    numero_boleto VARCHAR(20) NOT NULL UNIQUE,
    id_vuelo INT NOT NULL,
    id_usuario INT NOT NULL,
    fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP,
    precio_compra NUMERIC(7,2) NOT NULL,
    FOREIGN KEY (id_vuelo) REFERENCES vuelos(id_vuelo),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);
GO

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
-- Agregar campo cedula a la tabla usuarios
ALTER TABLE usuarios
ADD cedula VARCHAR(20) NOT NULL DEFAULT '0000000000';

-- Tabla de Facturas
IF OBJECT_ID('facturas', 'U') IS NULL
CREATE TABLE facturas (
    id_factura INT IDENTITY(1,1) PRIMARY KEY,
    numero_factura VARCHAR(20) NOT NULL UNIQUE,
    id_usuario INT NOT NULL,
    precio_sin_iva NUMERIC(10,2) NOT NULL,
    precio_con_iva NUMERIC(10,2) NOT NULL,
    fecha_factura DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);
GO

-- Agregar columna id_factura a boletos
ALTER TABLE boletos
ADD id_factura INT NULL;

-- Agregar clave foránea de boletos a facturas
ALTER TABLE boletos
ADD CONSTRAINT FK_boletos_factura FOREIGN KEY (id_factura) REFERENCES facturas(id_factura);

-- Verificar y agregar campo 'correo' si no existe
IF COL_LENGTH('usuarios', 'correo') IS NULL
BEGIN
    ALTER TABLE usuarios
    ADD correo VARCHAR(150) NOT NULL DEFAULT 'sincorreo@correo.com';
END
GO



IF OBJECT_ID('amortizacion_boletos', 'U') IS NULL
CREATE TABLE amortizacion_boletos (
    id_amortizacion INT IDENTITY(1,1) PRIMARY KEY,
    id_factura INT NOT NULL,
    numero_cuota INT NOT NULL,
    valor_cuota NUMERIC(10,2),
    interes_pagado NUMERIC(10,2),
    capital_pagado NUMERIC(10,2),
    saldo NUMERIC(10,2),
    FOREIGN KEY (id_factura) REFERENCES facturas(id_factura)
);
GO
