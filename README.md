<img width="413" height="197" alt="image" src="https://github.com/user-attachments/assets/0cb29f42-8c5a-44a7-9502-59237ef8ced2" />



SportConnect - Proyecto JavaFX 1º DAM

Versión preparada para la base de datos sportconnect_corregido_importar.sql.
La app usa datos en memoria para facilitar la ejecución, con modelos y pantallas adaptados a las tablas principales: roles, usuarios, clases, productos, suscripciones, pagos, pedidos y valoraciones.

Cambios importantes añadidos
Usuarios — El registro pide: nombre, apellido, nick, email, password y rol. Los roles están mapeados así: Admin = rol_id 1, Profesor = rol_id 2, Alumno = rol_id 3.

Profesores — Un profesor es un usuario con rol Profesor. Al iniciar sesión, ve el botón Panel profesor desde donde puede crear, modificar y eliminar sus propias clases.

Clases — Incluyen los campos de la tabla clases: id, titulo, descripcion, deporte, profesor_id, es_premium, precio, fecha_creacion, más horario, nivel y plazas.

Productos — Nueva pantalla de productos con gestión desde el panel admin (nombre, descripción, precio, stock, vendedor_id).
Panel admin — Gestiona configuración, clases, productos y usuarios, validando IDs como profesor_id y vendedor_id.

<img width="544" height="179" alt="image" src="https://github.com/user-attachments/assets/c667a6af-847e-47bd-a26e-bf408580e4e5" />

----------------------------------------------------------------------------------------------------

<img width="959" height="484" alt="image" src="https://github.com/user-attachments/assets/9753fe2f-e896-4d37-9900-ccce8c5fddb4" />





Base de datos

SQL: database/sportconnect_corregido_importar.sql
Utilidad de conexión: src/main/java/.../util/DBUtil.java
Por ahora no es obligatorio MySQL — los datos se cargan en memoria desde DataStore.java.


<img width="557" height="251" alt="image" src="https://github.com/user-attachments/assets/f9393ea9-132f-4775-bc5c-442a98659c10" />


Conexión MySQL en AWS EC2
DBUtil.java está configurado con:

Host: 34.206.126.154
Puerto: 3306
BD: sportconnect
Usuario: sportconnect_user

DataStore.java carga usuarios, clases, productos, pedidos, suscripciones y valoraciones desde MySQL. Al comprar un producto se inserta en pedidos y el trigger reducir_stock descuenta el stock automáticamente.
Para que funcione desde tu PC:

El puerto 3306 debe estar abierto en AWS para tu IP.
En docker-compose.yml debe estar publicado el puerto 3306:3306.
La BD sportconnect debe existir en phpMyAdmin con las tablas importadas.

Hay una clase de prueba de conexión en TestConexionBD.java para verificar que todo funciona antes de lanzar la app.
