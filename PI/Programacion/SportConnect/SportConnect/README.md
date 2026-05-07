# SportConnect - Proyecto JavaFX 1º DAM

Versión preparada para la base de datos `sportconnect_corregido_importar.sql`.

La aplicación sigue usando datos en memoria para que sea fácil de ejecutar en 1º DAM, pero ahora los modelos y pantallas están adaptados a las tablas principales de la base de datos:

- `roles`
- `usuarios`
- `clases`
- `productos`
- `suscripciones`
- `pagos`
- `pedidos`
- `valoraciones`

## Cambios importantes añadidos

### Usuarios
La pantalla de registro ahora pide los campos necesarios para la tabla `usuarios`:

- nombre
- apellido
- nick
- email
- password
- rol

Los roles están adaptados así:

- Admin = rol_id 1
- Profesor = rol_id 2
- Alumno = rol_id 3

### Profesores
Un profesor es un usuario con rol `Profesor`. Si entra un profesor, aparece el botón **Panel profesor**.

Desde el panel profesor puede:

- crear sus propias clases
- modificar sus propias clases
- eliminar sus propias clases
- controlar título, descripción, deporte, precio, premium, horario, nivel y plazas

### Clases
Las clases ahora tienen los campos de la tabla `clases`:

- id
- titulo
- descripcion
- deporte
- profesor_id
- es_premium
- precio
- fecha_creacion

Además se mantienen algunos campos útiles para la app de escritorio:

- horario
- nivel
- plazas

### Productos
Se ha añadido pantalla de productos y gestión de productos desde el panel admin.

Campos usados:

- nombre
- descripcion
- precio
- stock
- vendedor_id

### Panel admin
El panel admin ahora permite gestionar:

- configuración de la aplicación
- clases
- productos
- usuarios

El panel admin valida IDs importantes como `profesor_id` y `vendedor_id` para que tengan sentido con la base de datos.

## Usuarios de prueba

### Administrador

```text
Correo: aniri@mail.com
Contraseña: 123456
```

### Profesor

```text
Correo: ana@mail.com
Contraseña: 123456
```

### Alumno

```text
Correo: carlos@mail.com
Contraseña: 123456
```

## Base de datos

El SQL está incluido en:

```text
database/sportconnect_corregido_importar.sql
```

También se ha añadido `DBUtil.java` como base para conectar más adelante con MySQL:

```text
src/main/java/org/example/sportconnect_01/util/DBUtil.java
```

Por ahora la aplicación no depende obligatoriamente de MySQL para funcionar: los datos se cargan en memoria en `DataStore.java` usando ejemplos basados en tu SQL.

## Estructura principal

```text
src/main/java/org/example/sportconnect_01
├── controller
│   ├── LoginController.java
│   ├── RegisterController.java
│   ├── HomeController.java
│   ├── ClasesController.java
│   ├── MisClasesController.java
│   ├── ProfesoresController.java
│   ├── ProductosController.java
│   ├── PerfilController.java
│   ├── ProfesorPanelController.java
│   └── AdminController.java
├── model
│   ├── Usuario.java
│   ├── ClaseDeportiva.java
│   ├── Profesor.java
│   ├── Producto.java
│   ├── Suscripcion.java
│   └── AppConfig.java
├── service
│   ├── DataStore.java
│   └── Session.java
└── util
    ├── AlertUtils.java
    ├── DBUtil.java
    └── SceneManager.java
```

## Nota

Esta versión está pensada para entregar y defender como un proyecto de 1º DAM: tiene POO, MVC, JavaFX/FXML, panel admin y panel profesor, pero todavía no obliga a configurar MySQL para poder probarla.

## Corrección conexión MySQL AWS

Esta versión está corregida para conectarse directamente a la base de datos MySQL del servidor AWS EC2.

Datos configurados en `DBUtil.java`:

- Host: `34.206.126.154`
- Puerto MySQL: `3306`
- Base de datos: `sportconnect`
- Usuario: `sportconnect_user`
- Contraseña: `Sport1234!`

Cambios principales realizados:

- `DBUtil.java` ahora usa la IP pública de AWS y el usuario real de MySQL.
- `DataStore.java` carga usuarios, clases, productos, pedidos, suscripciones y valoraciones desde MySQL.
- Al comprar productos desde la app, se inserta un pedido en la tabla `pedidos`.
- El stock se descuenta automáticamente en la tabla `productos` mediante el trigger `reducir_stock` de la base de datos.
- La app recarga productos y compras desde MySQL después de realizar una compra.
- Se ha añadido `TestConexionBD.java` para comprobar rápidamente si IntelliJ conecta con MySQL.

Para que funcione desde tu PC:

1. En AWS debe estar abierto el puerto `3306` para tu IP.
2. En `docker-compose.yml` del servidor MySQL debe estar publicado así:

```yaml
ports:
  - "3306:3306"
```

3. En phpMyAdmin debe existir la base de datos `sportconnect` con las tablas importadas.

Usuarios de prueba:

- Admin: `aniri@mail.com` / `123456`
- Profesor: `ana@mail.com` / `123456`
- Alumno: `carlos@mail.com` / `123456`


## Corrección aplicada para IntelliJ

Se ha eliminado `module-info.java` para ejecutar el proyecto en modo classpath y evitar el error:

```text
module not found: com.mysql.cj
```

El driver MySQL sigue incluido en `pom.xml` mediante la dependencia `mysql-connector-j`.
Después de abrir el proyecto en IntelliJ hay que pulsar Maven > Reload All Maven Projects.

Clase de prueba incluida:

```text
src/main/java/org/example/sportconnect_01/TestConexionBD.java
```

Ejecutar primero esta clase para comprobar que la conexión con AWS MySQL funciona.
