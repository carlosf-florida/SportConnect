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
