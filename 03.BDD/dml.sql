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
