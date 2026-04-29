CREATE DATABASE IF NOT EXISTS sportconnect
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
USE sportconnect;

-- =========================
-- ROLES
-- =========================
CREATE TABLE roles (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY (nombre)
) ENGINE=InnoDB;

-- =========================
-- USUARIOS
-- =========================
CREATE TABLE usuarios (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  apellido VARCHAR(100) NOT NULL,
  nick VARCHAR(50) NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  rol_id INT DEFAULT NULL,
  fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY (nick),
  UNIQUE KEY (email),
  KEY (rol_id),
  CONSTRAINT usuarios_ibfk_1 FOREIGN KEY (rol_id) REFERENCES roles(id)
) ENGINE=InnoDB;

-- =========================
-- CLASES
-- =========================
CREATE TABLE clases (
  id INT NOT NULL AUTO_INCREMENT,
  titulo VARCHAR(150) NOT NULL,
  descripcion TEXT,
  deporte VARCHAR(100),
  profesor_id INT NOT NULL,
  es_premium TINYINT(1) DEFAULT 0,
  precio DECIMAL(10,2) DEFAULT 0.00,
  fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY (profesor_id),
  CONSTRAINT clases_ibfk_1 FOREIGN KEY (profesor_id) REFERENCES usuarios(id)
) ENGINE=InnoDB;

-- =========================
-- SUSCRIPCIONES
-- =========================
CREATE TABLE suscripciones (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  clase_id INT NOT NULL,
  fecha_suscripcion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY (usuario_id, clase_id),
  KEY (clase_id),
  CONSTRAINT suscripciones_ibfk_1 FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
  CONSTRAINT suscripciones_ibfk_2 FOREIGN KEY (clase_id) REFERENCES clases(id)
) ENGINE=InnoDB;

-- =========================
-- PAGOS
-- =========================
CREATE TABLE pagos (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  clase_id INT NOT NULL,
  monto DECIMAL(10,2) NOT NULL,
  fecha_pago TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY (usuario_id),
  KEY (clase_id),
  CONSTRAINT pagos_ibfk_1 FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
  CONSTRAINT pagos_ibfk_2 FOREIGN KEY (clase_id) REFERENCES clases(id)
) ENGINE=InnoDB;

-- =========================
-- MENSAJES
-- =========================
CREATE TABLE mensajes (
  id INT NOT NULL AUTO_INCREMENT,
  remitente_id INT NOT NULL,
  clase_id INT NOT NULL,
  contenido TEXT,
  fecha_envio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY (remitente_id),
  KEY (clase_id),
  CONSTRAINT mensajes_ibfk_1 FOREIGN KEY (remitente_id) REFERENCES usuarios(id),
  CONSTRAINT mensajes_ibfk_2 FOREIGN KEY (clase_id) REFERENCES clases(id)
) ENGINE=InnoDB;

-- =========================
-- VALORACIONES
-- =========================
CREATE TABLE valoraciones (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  clase_id INT NOT NULL,
  puntuacion INT NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
  comentario TEXT,
  fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY (usuario_id),
  KEY (clase_id),
  CONSTRAINT valoraciones_ibfk_1 FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
  CONSTRAINT valoraciones_ibfk_2 FOREIGN KEY (clase_id) REFERENCES clases(id)
) ENGINE=InnoDB;

-- =========================
-- ASISTENCIAS
-- =========================
CREATE TABLE asistencias (
  id INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  clase_id INT NOT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  asistio TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id),
  KEY (usuario_id),
  KEY (clase_id),
  CONSTRAINT asistencias_ibfk_1 FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
  CONSTRAINT asistencias_ibfk_2 FOREIGN KEY (clase_id) REFERENCES clases(id)
) ENGINE=InnoDB;

-- =========================
-- PRODUCTOS
-- =========================
CREATE TABLE productos (
  id INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  descripcion TEXT,
  precio DECIMAL(10,2) NOT NULL,
  stock INT DEFAULT 0,
  vendedor_id INT NOT NULL,
  fecha_publicacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  KEY (vendedor_id),
  CONSTRAINT productos_ibfk_1 FOREIGN KEY (vendedor_id) REFERENCES usuarios(id)
) ENGINE=InnoDB;

-- =========================
-- PEDIDOS
-- =========================
CREATE T
