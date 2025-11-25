USE packetWorld;

-- 1. TABLA ROL
CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- 2. TABLA SUCURSAL
CREATE TABLE sucursal (
    id_sucursal INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre_corto VARCHAR(100) NOT NULL,
    estatus ENUM('activa','inactiva') DEFAULT 'activa',
    calle VARCHAR(100) NOT NULL,
    numero VARCHAR(10),
    colonia VARCHAR(100),
    codigo_postal VARCHAR(10),
    ciudad VARCHAR(100),
    estado VARCHAR(100)
);

-- 3. TABLA UNIDAD
CREATE TABLE tipo_unidad (
    id_tipo_unidad INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE unidad (
    id_unidad INT AUTO_INCREMENT PRIMARY KEY,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio YEAR NOT NULL,
    vin VARCHAR(20) NOT NULL UNIQUE,
    numero_interno VARCHAR(20) NOT NULL UNIQUE,
    motivo_baja VARCHAR(255),
    id_tipo_unidad INT NOT NULL,
    id_sucursal INT,
    FOREIGN KEY (id_tipo_unidad) REFERENCES tipo_unidad(id_tipo_unidad),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal)
);

-- 4. TABLA COLABORADOR
CREATE TABLE colaborador (
    id_colaborador INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100),
    curp VARCHAR(18) NOT NULL UNIQUE,
    correo VARCHAR(100),
    numero_personal VARCHAR(20) NOT NULL UNIQUE,
    contrasena VARCHAR(100) NOT NULL,
    fotografia LONGBLOB,
    numero_licencia VARCHAR(30),
    id_rol INT NOT NULL,
    id_sucursal INT NOT NULL,
    id_unidad INT NULL,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal),
    FOREIGN KEY (id_unidad) REFERENCES unidad(id_unidad)
);

-- 5. TABLA CLIENTE
CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100),
    calle VARCHAR(100) NOT NULL,
    numero VARCHAR(10),
    colonia VARCHAR(100),
    codigo_postal VARCHAR(10),
    telefono VARCHAR(20),
    correo VARCHAR(100)
);

-- 6. TABLA ESTATUS_ENVIO
CREATE TABLE estatus_envio (
    id_estatus INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

-- 10. DATOS DESTINATARIO
CREATE TABLE destinatario (
    id_destinatario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido_paterno VARCHAR(100) NOT NULL,
    apellido_materno VARCHAR(100),
    calle VARCHAR(100) NOT NULL,
    numero VARCHAR(10),
    colonia VARCHAR(100),
    codigo_postal VARCHAR(10),
    ciudad VARCHAR(100),
    estado VARCHAR(100)
);


-- 7. TABLA ENVIO
CREATE TABLE envio (
    id_envio INT AUTO_INCREMENT PRIMARY KEY,
    numero_guia VARCHAR(50) NOT NULL UNIQUE,
    id_destinatario INT NOT NULL,  -- Relación con destinatario
    id_cliente INT NOT NULL,
    id_sucursal INT NOT NULL,
    id_conductor INT NULL,
    id_estatus_actual INT NOT NULL,
    costo_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_sucursal) REFERENCES sucursal(id_sucursal),
    FOREIGN KEY (id_conductor) REFERENCES colaborador(id_colaborador),
    FOREIGN KEY (id_estatus_actual) REFERENCES estatus_envio(id_estatus),
    FOREIGN KEY (id_destinatario) REFERENCES destinatario(id_destinatario)
);

-- 8. TABLA PAQUETE
CREATE TABLE paquete (
    id_paquete INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255),
    peso DECIMAL(6,2),
    alto DECIMAL(6,2),
    ancho DECIMAL(6,2),
    profundidad DECIMAL(6,2),
    id_envio INT NOT NULL,
    FOREIGN KEY (id_envio) REFERENCES envio(id_envio)
);

-- 9. TABLA HISTORIAL_ESTATUS_ENVIO
CREATE TABLE historial_estatus_envio (
    id_historial INT AUTO_INCREMENT PRIMARY KEY,
    id_envio INT NOT NULL,
    id_estatus INT NOT NULL,
    id_colaborador INT NOT NULL,
    fecha_cambio DATETIME NOT NULL DEFAULT NOW(),
    comentario VARCHAR(255),
    FOREIGN KEY (id_envio) REFERENCES envio(id_envio),
    FOREIGN KEY (id_estatus) REFERENCES estatus_envio(id_estatus),
    FOREIGN KEY (id_colaborador) REFERENCES colaborador(id_colaborador)
);

-- 11. DATOS INICIALES
INSERT INTO rol (nombre) VALUES 
('Administrador'),
('Ejecutivo de tienda'),
('Conductor');

INSERT INTO estatus_envio (nombre) VALUES
('recibido en sucursal'),
('procesado'),
('en tránsito'),
('detenido'),
('entregado'),
('cancelado');

INSERT INTO tipo_unidad (nombre) VALUES
('Gasolina'),
('Diesel'),
('Eléctrica'),
('Híbrida');

SELECT * FROM pais;

SELECT * FROM estado;

SELECT * FROM municipio WHERE id_estado = 30;
DESCRIBE colonia;

SELECT * FROM colonia;
SELECT *
FROM colonia
WHERE codigo_municipio = 30087;

SELECT * FROM sucursal;

DELETE  FROM sucursal WHERE id_sucursal = 10;

SELECT * FROM estatus_sucursal;

SELECT * FROM cliente;

SELECT * FROM destinatario;
DESCRIBE destinatario;
DELETE  FROM destinatario WHERE id_destinatario = 2;

SELECT * FROM colaborador;

SELECT * FROM rol;

SELECT * FROM unidad;

SELECT * FROM estatus_unidad;

SELECT * FROM envio;

SELECT * FROM estatus_envio;

SELECT * FROM rastreo_envio;

SELECT 
    c.id_colonia,
    c.nombre AS colonia,
    c.codigo_postal AS codigoPostal,
    m.id_municipio,
    m.nombre AS municipio,
    e.id_estado,
    e.nombre AS estado,
    p.id_pais,
    p.nombre AS pais
FROM colonia c
INNER JOIN municipio m ON c.codigo_municipio = m.codigo
INNER JOIN estado e ON m.id_estado = e.id_estado
INNER JOIN pais p ON e.id_pais = p.id_pais
WHERE c.codigo_postal = '91193';

SELECT * FROM colonia WHERE codigo_postal = '91176';

SELECT *
FROM municipio
WHERE codigo = 30087;

SELECT 
    p.id_pais AS idPais,
    p.nombre AS pais,
    e.id_estado AS idEstado,
    e.nombre AS estado,
    m.id_municipio AS idMunicipio,
    m.nombre AS municipio,
    c.id_colonia AS idColonia,
    c.nombre AS colonia,
    c.codigo_postal AS codigoPostal
FROM colonia c
INNER JOIN municipio m ON c.codigo_municipio = m.codigo
INNER JOIN estado e ON m.id_estado = e.id_estado
INNER JOIN pais p ON e.id_pais = p.id_pais
WHERE c.codigo_postal = '91193';


