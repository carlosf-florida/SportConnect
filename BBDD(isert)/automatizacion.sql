-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.4.32-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.17.0.7270
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para sportconnect
CREATE DATABASE IF NOT EXISTS `sportconnect` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `sportconnect`;

-- Volcando estructura para tabla sportconnect.asistencias
CREATE TABLE IF NOT EXISTS `asistencias` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `clase_id` int(11) NOT NULL,
  `fecha` datetime NOT NULL DEFAULT current_timestamp(),
  `asistio` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `clase_id` (`clase_id`),
  CONSTRAINT `asistencias_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `asistencias_ibfk_2` FOREIGN KEY (`clase_id`) REFERENCES `clases` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla sportconnect.asistencias: ~12 rows (aproximadamente)
INSERT INTO `asistencias` (`id`, `usuario_id`, `clase_id`, `fecha`, `asistio`) VALUES
	(1, 1, 1, '2026-04-29 13:25:25', 1),
	(2, 1, 2, '2026-04-29 13:25:25', 1),
	(3, 3, 1, '2026-04-29 13:25:25', 1),
	(4, 3, 4, '2026-04-29 13:25:25', 0),
	(5, 5, 2, '2026-04-29 13:25:25', 1),
	(6, 6, 3, '2026-04-29 13:25:25', 1),
	(7, 7, 1, '2026-04-29 13:25:25', 1),
	(8, 9, 6, '2026-04-29 13:25:25', 1),
	(9, 10, 2, '2026-04-29 13:25:25', 1),
	(10, 11, 4, '2026-04-29 13:25:25', 1),
	(11, 13, 7, '2026-04-29 13:25:25', 0),
	(12, 14, 8, '2026-04-29 13:25:25', 1);

-- Volcando estructura para tabla sportconnect.clases
CREATE TABLE IF NOT EXISTS `clases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(150) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `deporte` varchar(100) DEFAULT NULL,
  `profesor_id` int(11) NOT NULL,
  `es_premium` tinyint(1) DEFAULT 0,
  `precio` decimal(10,2) DEFAULT 0.00,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `profesor_id` (`profesor_id`),
  CONSTRAINT `clases_ibfk_1` FOREIGN KEY (`profesor_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla sportconnect.clases: ~8 rows (aproximadamente)
INSERT INTO `clases` (`id`, `titulo`, `descripcion`, `deporte`, `profesor_id`, `es_premium`, `precio`, `fecha_creacion`) VALUES
	(1, 'Yoga para principiantes', 'Clase básica de yoga', 'Yoga', 2, 0, 0.00, '2026-04-29 11:25:25'),
	(2, 'Crossfit intensivo', 'Entrenamiento avanzado', 'Crossfit', 4, 1, 15.00, '2026-04-29 11:25:25'),
	(3, 'Futbol técnico', 'Mejora tu técnica', 'Futbol', 8, 0, 0.00, '2026-04-29 11:25:25'),
	(4, 'Pilates avanzado', 'Control corporal', 'Pilates', 12, 1, 12.00, '2026-04-29 11:25:25'),
	(5, 'Running urbano', 'Entrenamiento de resistencia', 'Running', 2, 0, 0.00, '2026-04-29 11:25:25'),
	(6, 'Boxeo básico', 'Defensa personal', 'Boxeo', 4, 1, 20.00, '2026-04-29 11:25:25'),
	(7, 'Ciclismo indoor', 'Cardio intenso', 'Ciclismo', 8, 0, 0.00, '2026-04-29 11:25:25'),
	(8, 'Fitness full body', 'Entrenamiento completo', 'Fitness', 12, 1, 18.00, '2026-04-29 11:25:25');

-- Volcando estructura para tabla sportconnect.mensajes
CREATE TABLE IF NOT EXISTS `mensajes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `remitente_id` int(11) NOT NULL,
  `clase_id` int(11) NOT NULL,
  `contenido` text DEFAULT NULL,
  `fecha_envio` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `remitente_id` (`remitente_id`),
  KEY `clase_id` (`clase_id`),
  CONSTRAINT `mensajes_ibfk_1` FOREIGN KEY (`remitente_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `mensajes_ibfk_2` FOREIGN KEY (`clase_id`) REFERENCES `clases` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla sportconnect.mensajes: ~8 rows (aproximadamente)
INSERT INTO `mensajes` (`id`, `remitente_id`, `clase_id`, `contenido`, `fecha_envio`) VALUES
	(1, 1, 1, '¿A qué hora empieza la clase?', '2026-04-29 11:25:25'),
	(2, 3, 2, 'Me gusta mucho este entrenamiento', '2026-04-29 11:25:25'),
	(3, 5, 2, 'Muy intensa la sesión', '2026-04-29 11:25:25'),
	(4, 7, 1, '¿Habrá nivel intermedio?', '2026-04-29 11:25:25'),
	(5, 9, 6, 'Excelente clase de boxeo', '2026-04-29 11:25:25'),
	(6, 10, 2, 'Muy dura pero efectiva', '2026-04-29 11:25:25'),
	(7, 11, 4, 'Buen ritmo de pilates', '2026-04-29 11:25:25'),
	(8, 13, 7, 'Gran sesión de ciclismo', '2026-04-29 11:25:25');

-- Volcando estructura para tabla sportconnect.pagos
CREATE TABLE IF NOT EXISTS `pagos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `clase_id` int(11) NOT NULL,
  `monto` decimal(10,2) NOT NULL,
  `fecha_pago` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `clase_id` (`clase_id`),
  CONSTRAINT `pagos_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `pagos_ibfk_2` FOREIGN KEY (`clase_id`) REFERENCES `clases` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla sportconnect.pagos: ~10 rows (aproximadamente)
INSERT INTO `pagos` (`id`, `usuario_id`, `clase_id`, `monto`, `fecha_pago`) VALUES
	(1, 1, 2, 15.00, '2026-04-29 11:25:25'),
	(2, 3, 4, 12.00, '2026-04-29 11:25:25'),
	(3, 5, 2, 15.00, '2026-04-29 11:25:25'),
	(4, 9, 6, 20.00, '2026-04-29 11:25:25'),
	(5, 10, 2, 15.00, '2026-04-29 11:25:25'),
	(6, 11, 4, 12.00, '2026-04-29 11:25:25'),
	(7, 13, 8, 18.00, '2026-04-29 11:25:25'),
	(8, 14, 8, 18.00, '2026-04-29 11:25:25'),
	(9, 12, 2, 15.00, '2026-04-29 11:25:25'),
	(10, 4, 5, 0.00, '2026-04-29 11:25:25');

-- Volcando estructura para tabla sportconnect.pedidos
CREATE TABLE IF NOT EXISTS `pedidos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comprador_id` int(11) NOT NULL,
  `producto_id` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `fecha_pedido` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `comprador_id` (`comprador_id`),
  KEY `producto_id` (`producto_id`),
  CONSTRAINT `pedidos_ibfk_1` FOREIGN KEY (`comprador_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `pedidos_ibfk_2` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla sportconnect.pedidos: ~8 rows (aproximadamente)
INSERT INTO `pedidos` (`id`, `comprador_id`, `producto_id`, `cantidad`, `total`, `fecha_pedido`) VALUES
	(1, 1, 1, 2, 59.98, '2026-04-29 11:25:25'),
	(2, 3, 2, 1, 9.99, '2026-04-29 11:25:25'),
	(3, 5, 3, 1, 14.99, '2026-04-29 11:25:25'),
	(4, 7, 4, 1, 24.99, '2026-04-29 11:25:25'),
	(5, 9, 5, 2, 39.98, '2026-04-29 11:25:25'),
	(6, 10, 1, 1, 29.99, '2026-04-29 11:25:25'),
	(7, 11, 2, 3, 29.97, '2026-04-29 11:25:25'),
	(8, 13, 3, 2, 29.98, '2026-04-29 11:25:25');

-- Volcando estructura para tabla sportconnect.productos
CREATE TABLE IF NOT EXISTS `productos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(150) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `precio` decimal(10,2) NOT NULL,
  `stock` int(11) DEFAULT 0,
  `vendedor_id` int(11) NOT NULL,
  `fecha_publicacion` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `vendedor_id` (`vendedor_id`),
  CONSTRAINT `productos_ibfk_1` FOREIGN KEY (`vendedor_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla sportconnect.productos: ~5 rows (aproximadamente)
INSERT INTO `productos` (`id`, `nombre`, `descripcion`, `precio`, `stock`, `vendedor_id`, `fecha_publicacion`) VALUES
	(1, 'Proteina Whey', 'Suplemento muscular', 29.99, 50, 2, '2026-04-29 11:25:25'),
	(2, 'Botella deportiva', 'Botella 1L', 9.99, 100, 4, '2026-04-29 11:25:25'),
	(3, 'Cuerda crossfit', 'Alta resistencia', 14.99, 30, 8, '2026-04-29 11:25:25'),
	(4, 'Guantes boxeo', 'Profesionales', 24.99, 20, 12, '2026-04-29 11:25:25'),
	(5, 'Esterilla yoga', 'Antideslizante', 19.99, 40, 2, '2026-04-29 11:25:25');

-- Volcando estructura para tabla sportconnect.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla sportconnect.roles: ~3 rows (aproximadamente)
INSERT INTO `roles` (`id`, `nombre`) VALUES
	(1, 'administrador'),
	(2, 'profesor'),
	(3, 'usuario');

-- Volcando estructura para procedimiento sportconnect.suscribirse_clase
DELIMITER //
CREATE PROCEDURE `suscribirse_clase`(
    IN p_usuario INT,
    IN p_clase INT
)
BEGIN
    DECLARE precio_clase DECIMAL(10,2);
    DECLARE ya_suscrito INT DEFAULT 0;

    SELECT COUNT(*) INTO ya_suscrito
    FROM suscripciones
    WHERE usuario_id = p_usuario AND clase_id = p_clase;

    IF ya_suscrito > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El usuario ya está suscrito a esta clase';
    END IF;

    SELECT precio INTO precio_clase
    FROM clases WHERE id = p_clase;

    START TRANSACTION;

        INSERT INTO suscripciones(usuario_id, clase_id)
        VALUES (p_usuario, p_clase);

        IF precio_clase > 0 THEN
            INSERT INTO pagos(usuario_id, clase_id, monto)
            VALUES (p_usuario, p_clase, precio_clase);
        END IF;

    COMMIT;

END//
DELIMITER ;

-- Volcando estructura para tabla sportconnect.suscripciones
CREATE TABLE IF NOT EXISTS `suscripciones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `clase_id` int(11) NOT NULL,
  `fecha_suscripcion` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `usuario_id` (`usuario_id`,`clase_id`),
  KEY `clase_id` (`clase_id`),
  CONSTRAINT `suscripciones_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `suscripciones_ibfk_2` FOREIGN KEY (`clase_id`) REFERENCES `clases` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla sportconnect.suscripciones: ~20 rows (aproximadamente)
INSERT INTO `suscripciones` (`id`, `usuario_id`, `clase_id`, `fecha_suscripcion`) VALUES
	(1, 1, 1, '2026-04-29 11:25:25'),
	(2, 1, 2, '2026-04-29 11:25:25'),
	(3, 3, 1, '2026-04-29 11:25:25'),
	(4, 3, 4, '2026-04-29 11:25:25'),
	(5, 5, 2, '2026-04-29 11:25:25'),
	(6, 6, 3, '2026-04-29 11:25:25'),
	(7, 7, 1, '2026-04-29 11:25:25'),
	(8, 7, 5, '2026-04-29 11:25:25'),
	(9, 9, 6, '2026-04-29 11:25:25'),
	(10, 10, 2, '2026-04-29 11:25:25'),
	(11, 11, 4, '2026-04-29 11:25:25'),
	(12, 13, 7, '2026-04-29 11:25:25'),
	(13, 14, 8, '2026-04-29 11:25:25'),
	(14, 15, 1, '2026-04-29 11:25:25'),
	(15, 2, 3, '2026-04-29 11:25:25'),
	(16, 4, 5, '2026-04-29 11:25:25'),
	(17, 8, 6, '2026-04-29 11:25:25'),
	(18, 12, 2, '2026-04-29 11:25:25'),
	(19, 9, 8, '2026-04-29 11:25:25'),
	(20, 5, 4, '2026-04-29 11:25:25');

-- Volcando estructura para tabla sportconnect.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `nick` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol_id` int(11) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `nick` (`nick`),
  UNIQUE KEY `email` (`email`),
  KEY `rol_id` (`rol_id`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla sportconnect.usuarios: ~16 rows (aproximadamente)
INSERT INTO `usuarios` (`id`, `nombre`, `apellido`, `nick`, `email`, `password`, `rol_id`, `fecha_creacion`) VALUES
	(1, 'Carlos', 'Larrea', 'aniri', 'aniri@mail.com', '123456', 1, '2026-04-29 11:25:25'),
	(2, 'Ana', 'Lopez', 'analopez', 'ana@mail.com', '123456', 2, '2026-04-29 11:25:25'),
	(3, 'Juan', 'Perez', 'juanp', 'juan@mail.com', '123456', 2, '2026-04-29 11:25:25'),
	(4, 'Maria', 'Gomez', 'mariag', 'maria@mail.com', '123456', 2, '2026-04-29 11:25:25'),
	(5, 'Carlos', 'Ruiz', 'carlosr', 'carlos@mail.com', '123456', 3, '2026-04-29 11:25:25'),
	(6, 'Laura', 'Sanchez', 'lauras', 'laura@mail.com', '123456', 3, '2026-04-29 11:25:25'),
	(7, 'Pedro', 'Diaz', 'pedrod', 'pedro@mail.com', '123456', 3, '2026-04-29 11:25:25'),
	(8, 'Lucia', 'Martinez', 'luciam', 'lucia@mail.com', '123456', 2, '2026-04-29 11:25:25'),
	(9, 'David', 'Hernandez', 'davidh', 'david@mail.com', '123456', 3, '2026-04-29 11:25:25'),
	(10, 'Sofia', 'Garcia', 'sofiag', 'sofia@mail.com', '123456', 3, '2026-04-29 11:25:25'),
	(11, 'Miguel', 'Torres', 'miguelt', 'miguel@mail.com', '123456', 3, '2026-04-29 11:25:25'),
	(12, 'Elena', 'Vega', 'elenav', 'elena@mail.com', '123456', 2, '2026-04-29 11:25:25'),
	(13, 'Alberto', 'Navarro', 'alberton', 'alberto@mail.com', '123456', 3, '2026-04-29 11:25:25'),
	(14, 'Raquel', 'Moreno', 'raquelm', 'raquel@mail.com', '123456', 3, '2026-04-29 11:25:25'),
	(15, 'Jorge', 'Castro', 'jorgec', 'jorge@mail.com', '123456', 3, '2026-04-29 11:25:25'),
	(16, 'Sara', 'Ortega', 'sarao', 'sara@mail.com', '123456', 3, '2026-04-29 11:25:25');

-- Volcando estructura para tabla sportconnect.valoraciones
CREATE TABLE IF NOT EXISTS `valoraciones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) NOT NULL,
  `clase_id` int(11) NOT NULL,
  `puntuacion` int(11) NOT NULL CHECK (`puntuacion` between 1 and 5),
  `comentario` text DEFAULT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `clase_id` (`clase_id`),
  CONSTRAINT `valoraciones_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `valoraciones_ibfk_2` FOREIGN KEY (`clase_id`) REFERENCES `clases` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla sportconnect.valoraciones: ~8 rows (aproximadamente)
INSERT INTO `valoraciones` (`id`, `usuario_id`, `clase_id`, `puntuacion`, `comentario`, `fecha`) VALUES
	(1, 1, 2, 5, 'Excelente clase', '2026-04-29 11:25:25'),
	(2, 3, 4, 4, 'Muy buena', '2026-04-29 11:25:25'),
	(3, 5, 2, 5, 'Intensa pero útil', '2026-04-29 11:25:25'),
	(4, 7, 1, 3, 'Normal', '2026-04-29 11:25:25'),
	(5, 9, 6, 5, 'Muy recomendable', '2026-04-29 11:25:25'),
	(6, 10, 2, 4, 'Buena experiencia', '2026-04-29 11:25:25'),
	(7, 11, 4, 5, 'Perfecto', '2026-04-29 11:25:25'),
	(8, 13, 7, 4, 'Bien estructurada', '2026-04-29 11:25:25');

-- Volcando estructura para vista sportconnect.vista_ingresos_clases
-- Creando tabla temporal para superar errores de dependencia de VIEW
CREATE TABLE `vista_ingresos_clases` (
	`id` INT(11) NOT NULL,
	`titulo` VARCHAR(1) NOT NULL COLLATE 'utf8mb4_unicode_ci',
	`total_pagos` BIGINT(21) NOT NULL,
	`ingresos_totales` DECIMAL(32,2) NULL
);

-- Volcando estructura para disparador sportconnect.evitar_stock_negativo
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_ZERO_IN_DATE,NO_ZERO_DATE,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER evitar_stock_negativo
BEFORE INSERT ON pedidos
FOR EACH ROW
BEGIN
    DECLARE stock_actual INT;

    SELECT stock INTO stock_actual
    FROM productos
    WHERE id = NEW.producto_id;

    IF stock_actual < NEW.cantidad THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Stock insuficiente';
    END IF;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Volcando estructura para disparador sportconnect.reducir_stock
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_ZERO_IN_DATE,NO_ZERO_DATE,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER reducir_stock
AFTER INSERT ON pedidos
FOR EACH ROW
BEGIN
    UPDATE productos
    SET stock = stock - NEW.cantidad
    WHERE id = NEW.producto_id;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- Eliminando tabla temporal y crear estructura final de VIEW
DROP TABLE IF EXISTS `vista_ingresos_clases`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vista_ingresos_clases` AS SELECT 
    c.id,
    c.titulo,
    COUNT(p.id)  AS total_pagos,
    SUM(p.monto) AS ingresos_totales
FROM clases c
LEFT JOIN pagos p ON c.id = p.clase_id
GROUP BY c.id, c.titulo 
;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
