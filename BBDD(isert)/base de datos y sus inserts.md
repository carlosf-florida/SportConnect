CREATE DATABASE IF NOT EXISTS sportconnect

DEFAULT CHARACTER SET utf8mb4

COLLATE utf8mb4\_unicode\_ci;



USE sportconnect;



\-- =========================

\-- ROLES

\-- =========================

CREATE TABLE roles (

&#x20; id INT NOT NULL AUTO\_INCREMENT,

&#x20; nombre VARCHAR(50) NOT NULL,

&#x20; PRIMARY KEY (id),

&#x20; UNIQUE KEY (nombre)

) ENGINE=InnoDB;



\-- =========================

\-- USUARIOS

\-- =========================

CREATE TABLE usuarios (

&#x20; id INT NOT NULL AUTO\_INCREMENT,

&#x20; nombre VARCHAR(100) NOT NULL,

&#x20; apellido VARCHAR(100) NOT NULL,

&#x20; nick VARCHAR(50) NOT NULL,

&#x20; email VARCHAR(100) NOT NULL,

&#x20; password VARCHAR(255) NOT NULL,

&#x20; rol\_id INT DEFAULT NULL,

&#x20; fecha\_creacion TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,



&#x20; PRIMARY KEY (id),

&#x20; UNIQUE KEY (nick),

&#x20; UNIQUE KEY (email),

&#x20; KEY (rol\_id),



&#x20; CONSTRAINT usuarios\_ibfk\_1 FOREIGN KEY (rol\_id) REFERENCES roles(id)

) ENGINE=InnoDB;



\-- =========================

\-- CLASES

\-- =========================

CREATE TABLE clases (

&#x20; id INT NOT NULL AUTO\_INCREMENT,

&#x20; titulo VARCHAR(150) NOT NULL,

&#x20; descripcion TEXT,

&#x20; deporte VARCHAR(100),

&#x20; profesor\_id INT NOT NULL,

&#x20; es\_premium TINYINT(1) DEFAULT 0,

&#x20; precio DECIMAL(10,2) DEFAULT 0.00,

&#x20; fecha\_creacion TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,



&#x20; PRIMARY KEY (id),

&#x20; KEY (profesor\_id),



&#x20; CONSTRAINT clases\_ibfk\_1 FOREIGN KEY (profesor\_id) REFERENCES usuarios(id)

) ENGINE=InnoDB;



\-- =========================

\-- SUSCRIPCIONES

\-- =========================

CREATE TABLE suscripciones (

&#x20; id INT NOT NULL AUTO\_INCREMENT,

&#x20; usuario\_id INT NOT NULL,

&#x20; clase\_id INT NOT NULL,

&#x20; fecha\_suscripcion TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,



&#x20; PRIMARY KEY (id),

&#x20; UNIQUE KEY (usuario\_id, clase\_id),

&#x20; KEY (clase\_id),



&#x20; CONSTRAINT suscripciones\_ibfk\_1 FOREIGN KEY (usuario\_id) REFERENCES usuarios(id),

&#x20; CONSTRAINT suscripciones\_ibfk\_2 FOREIGN KEY (clase\_id) REFERENCES clases(id)

) ENGINE=InnoDB;



\-- =========================

\-- PAGOS

\-- =========================

CREATE TABLE pagos (

&#x20; id INT NOT NULL AUTO\_INCREMENT,

&#x20; usuario\_id INT NOT NULL,

&#x20; clase\_id INT NOT NULL,

&#x20; monto DECIMAL(10,2) NOT NULL,

&#x20; fecha\_pago TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,



&#x20; PRIMARY KEY (id),

&#x20; KEY (usuario\_id),

&#x20; KEY (clase\_id),



&#x20; CONSTRAINT pagos\_ibfk\_1 FOREIGN KEY (usuario\_id) REFERENCES usuarios(id),

&#x20; CONSTRAINT pagos\_ibfk\_2 FOREIGN KEY (clase\_id) REFERENCES clases(id)

) ENGINE=InnoDB;



\-- =========================

\-- MENSAJES

\-- =========================

CREATE TABLE mensajes (

&#x20; id INT NOT NULL AUTO\_INCREMENT,

&#x20; remitente\_id INT NOT NULL,

&#x20; clase\_id INT NOT NULL,

&#x20; contenido TEXT,

&#x20; fecha\_envio TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,



&#x20; PRIMARY KEY (id),

&#x20; KEY (remitente\_id),

&#x20; KEY (clase\_id),



&#x20; CONSTRAINT mensajes\_ibfk\_1 FOREIGN KEY (remitente\_id) REFERENCES usuarios(id),

&#x20; CONSTRAINT mensajes\_ibfk\_2 FOREIGN KEY (clase\_id) REFERENCES clases(id)

) ENGINE=InnoDB;



\-- =========================

\-- VALORACIONES

\-- =========================

CREATE TABLE valoraciones (

&#x20; id INT NOT NULL AUTO\_INCREMENT,

&#x20; usuario\_id INT NOT NULL,

&#x20; clase\_id INT NOT NULL,

&#x20; puntuacion INT NOT NULL,

&#x20; comentario TEXT,

&#x20; fecha TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,



&#x20; PRIMARY KEY (id),

&#x20; KEY (usuario\_id),

&#x20; KEY (clase\_id),



&#x20; CONSTRAINT valoraciones\_ibfk\_1 FOREIGN KEY (usuario\_id) REFERENCES usuarios(id),

&#x20; CONSTRAINT valoraciones\_ibfk\_2 FOREIGN KEY (clase\_id) REFERENCES clases(id)

) ENGINE=InnoDB;



\-- =========================

\-- ASISTENCIAS

\-- =========================

CREATE TABLE asistencias (

&#x20; id INT NOT NULL AUTO\_INCREMENT,

&#x20; usuario\_id INT NOT NULL,

&#x20; clase\_id INT NOT NULL,

&#x20; fecha DATETIME NOT NULL DEFAULT CURRENT\_TIMESTAMP,

&#x20; asistio TINYINT(1) DEFAULT 0,



&#x20; PRIMARY KEY (id),

&#x20; KEY (usuario\_id),

&#x20; KEY (clase\_id),



&#x20; CONSTRAINT asistencias\_ibfk\_1 FOREIGN KEY (usuario\_id) REFERENCES usuarios(id),

&#x20; CONSTRAINT asistencias\_ibfk\_2 FOREIGN KEY (clase\_id) REFERENCES clases(id)

) ENGINE=InnoDB;



\-- =========================

\-- PRODUCTOS

\-- =========================

CREATE TABLE productos (

&#x20; id INT NOT NULL AUTO\_INCREMENT,

&#x20; nombre VARCHAR(150) NOT NULL,

&#x20; descripcion TEXT,

&#x20; precio DECIMAL(10,2) NOT NULL,

&#x20; stock INT DEFAULT 0,

&#x20; vendedor\_id INT NOT NULL,

&#x20; fecha\_publicacion TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,



&#x20; PRIMARY KEY (id),

&#x20; KEY (vendedor\_id),



&#x20; CONSTRAINT productos\_ibfk\_1 FOREIGN KEY (vendedor\_id) REFERENCES usuarios(id)

) ENGINE=InnoDB;



\-- =========================

\-- PEDIDOS

\-- =========================

CREATE TABLE pedidos (

&#x20; id INT NOT NULL AUTO\_INCREMENT,

&#x20; comprador\_id INT NOT NULL,

&#x20; producto\_id INT NOT NULL,

&#x20; cantidad INT NOT NULL,

&#x20; total DECIMAL(10,2) NOT NULL,

&#x20; fecha\_pedido TIMESTAMP NOT NULL DEFAULT CURRENT\_TIMESTAMP,



&#x20; PRIMARY KEY (id),

&#x20; KEY (comprador\_id),

&#x20; KEY (producto\_id),



&#x20; CONSTRAINT pedidos\_ibfk\_1 FOREIGN KEY (comprador\_id) REFERENCES usuarios(id),

&#x20; CONSTRAINT pedidos\_ibfk\_2 FOREIGN KEY (producto\_id) REFERENCES productos(id)

) ENGINE=INNODB;





















***//insert***





INSERT INTO pedidos (comprador\_id, producto\_id, cantidad, total) VALUES

(1,1,2,59.98),

(3,2,1,9.99),

(5,3,1,14.99),

(7,4,1,24.99),

(9,5,2,39.98),

(10,1,1,29.99),

(11,2,3,29.97),

(13,3,2,29.98);









INSERT INTO productos (nombre, descripcion, precio, stock, vendedor\_id) VALUES

('Proteina Whey', 'Suplemento muscular', 29.99, 50, 2),

('Botella deportiva', 'Botella 1L', 9.99, 100, 4),

('Cuerda crossfit', 'Alta resistencia', 14.99, 30, 8),

('Guantes boxeo', 'Profesionales', 24.99, 20, 12),

('Esterilla yoga', 'Antideslizante', 19.99, 40, 2);







INSERT INTO asistencias (usuario\_id, clase\_id, asistio) VALUES

(1,1,1),

(1,2,1),

(3,1,1),

(3,4,0),

(5,2,1),

(6,3,1),

(7,1,1),

(9,6,1),

(10,2,1),

(11,4,1),

(13,7,0),

(14,8,1);





INSERT INTO valoraciones (usuario\_id, clase\_id, puntuacion, comentario) VALUES

(1,2,5,'Excelente clase'),

(3,4,4,'Muy buena'),

(5,2,5,'Intensa pero útil'),

(7,1,3,'Normal'),

(9,6,5,'Muy recomendable'),

(10,2,4,'Buena experiencia'),

(11,4,5,'Perfecto'),

(13,7,4,'Bien estructurada');











INSERT INTO mensajes (remitente\_id, clase\_id, contenido) VALUES

(1,1,'¿A qué hora empieza la clase?'),

(3,2,'Me gusta mucho este entrenamiento'),

(5,2,'Muy intensa la sesión'),

(7,1,'¿Habrá nivel intermedio?'),

(9,6,'Excelente clase de boxeo'),

(10,2,'Muy dura pero efectiva'),

(11,4,'Buen ritmo de pilates'),

(13,7,'Gran sesión de ciclismo');







INSERT INTO pagos (usuario\_id, clase\_id, monto) VALUES

(1,2,15.00),

(3,4,12.00),

(5,2,15.00),

(9,6,20.00),

(10,2,15.00),

(11,4,12.00),

(13,8,18.00),

(14,8,18.00),

(12,2,15.00),

(4,5,0.00);







INSERT INTO suscripciones (usuario\_id, clase\_id) VALUES

(1,1),(1,2),(3,1),(3,4),(5,2),

(6,3),(7,1),(7,5),(9,6),(10,2),

(11,4),(13,7),(14,8),(15,1),(2,3),

(4,5),(8,6),(12,2),(9,8),(5,4);









INSERT INTO clases (titulo, descripcion, deporte, profesor\_id, es\_premium, precio) VALUES

('Yoga para principiantes', 'Clase básica de yoga', 'Yoga', 2, 0, 0.00),

('Crossfit intensivo', 'Entrenamiento avanzado', 'Crossfit', 4, 1, 15.00),

('Futbol técnico', 'Mejora tu técnica', 'Futbol', 8, 0, 0.00),

('Pilates avanzado', 'Control corporal', 'Pilates', 12, 1, 12.00),

('Running urbano', 'Entrenamiento de resistencia', 'Running', 2, 0, 0.00),

('Boxeo básico', 'Defensa personal', 'Boxeo', 4, 1, 20.00),

('Ciclismo indoor', 'Cardio intenso', 'Ciclismo', 8, 0, 0.00),

('Fitness full body', 'Entrenamiento completo', 'Fitness', 12, 1, 18.00);









INSERT INTO usuarios (nombre, apellido, nick, email, password, rol\_id) VALUES

('Carlos', 'Larrea', 'aniri', 'aniri@mail.com', '123456', 1),

('Ana', 'Lopez', 'analopez', 'ana@mail.com', '123456', 3),

('Juan', 'Perez', 'juanp', 'juan@mail.com', '123456', 2),

('Maria', 'Gomez', 'mariag', 'maria@mail.com', '123456', 3),

('Carlos', 'Ruiz', 'carlosr', 'carlos@mail.com', '123456', 2),

('Laura', 'Sanchez', 'lauras', 'laura@mail.com', '123456', 3),

('Pedro', 'Diaz', 'pedrod', 'pedro@mail.com', '123456', 3),

('Lucia', 'Martinez', 'luciam', 'lucia@mail.com', '123456', 3),

('David', 'Hernandez', 'davidh', 'david@mail.com', '123456', 2),

('Sofia', 'Garcia', 'sofiag', 'sofia@mail.com', '123456', 3),

('Miguel', 'Torres', 'miguelt', 'miguel@mail.com', '123456', 3),

('Elena', 'Vega', 'elenav', 'elena@mail.com', '123456', 3),

('Alberto', 'Navarro', 'alberton', 'alberto@mail.com', '123456', 2),

('Raquel', 'Moreno', 'raquelm', 'raquel@mail.com', '123456', 3),

('Jorge', 'Castro', 'jorgec', 'jorge@mail.com', '123456', 3),

('Sara', 'Ortega', 'sarao', 'sara@mail.com', '123456', 3);













INSERT INTO roles (nombre) VALUES

('administrador'),

('profesor'),

('usuario');

