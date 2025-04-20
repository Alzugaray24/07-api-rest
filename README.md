## API REST

Es una interfaz de programación de aplicaciones (API) que sigue los principios de la arquitectura REST (Representational State Transfer).

Es un conjunto de reglas que permite a sistemas interactuar entre sí utilizando métodos estándar del protocolo HTTP, como GET, POST, PUT, DELETE, etc.
El término "API REST" se usa para referirse a una API que implementa parcialmente o completamente las restricciones definidas por REST.

## RESTful

Es un adjetivo que describe una API que cumple completamente con los principios de REST.

Una API es RESTful si sigue estrictamente los siguientes principios:

- Arquitectura cliente-servidor: Separación entre cliente (interfaz) y servidor (datos y lógica).

- Stateless: Cada solicitud del cliente al servidor debe contener toda la información necesaria para entenderla.

- Caché: Las respuestas deben ser cacheables cuando sea posible para mejorar el rendimiento.

- Interfaz uniforme: Uso consistente de métodos HTTP, URIs, y representaciones de recursos.

- Sistema en capas: Posibilidad de usar capas intermedias entre cliente y servidor.

## A que nos referimos con Stateless

En el contexto de REST, el término stateless (sin estado) significa que cada solicitud del cliente al servidor debe ser independiente y contener toda la información necesaria para que el servidor pueda procesarla, sin depender del estado de solicitudes anteriores.

## Que haremos en este repositorio...

Vamos a estructurar un API Restful para la heladería. Usaremos Java 17 con Gradle y organizaremos el proyecto de manera que sea fácil de extender. Nos enfocaremos en el inventario, que incluirá funcionalidades para gestionar productos, como agregar, actualizar, eliminar y listar.

Para empezar, estructuraremos el proyecto con estas consideraciones:

- Controladores: Gestionarán las rutas del API.
- Servicios: Contendrán la lógica del negocio.
- Modelos: Representarán las entidades de dominio (como "Producto").
- Repositorios en memoria: Simularán la interacción con la base de datos, usando colecciones como Map, Set y LinkedList.

Patrones de diseño:

- Singleton para manejar el repositorio.
- DTO para separar las entidades de dominio de los datos de respuesta o petición.

## Configuración del Entorno

Para ejecutar la aplicación localmente, necesitas configurar las variables de entorno. Sigue estos pasos:

1. Crea un archivo `.env` en la raíz del proyecto basado en el ejemplo:

```
DB_URL=jdbc:mysql://localhost:3306/restaurant
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
DB_DRIVER=com.mysql.cj.jdbc.Driver
JPA_DDL_AUTO=update
SHOW_SQL=true
FORMAT_SQL=true
PORT=8080
```

2. Asegúrate de tener MySQL instalado y una base de datos llamada `restaurant` creada.

3. Para iniciar la aplicación con las variables de entorno del archivo `.env`, puedes usar plugins como [dotenv-gradle](https://github.com/uzzu/dotenv-gradle) o ejecutar:

```bash
# En Linux/Mac
export $(grep -v '^#' .env | xargs) && ./gradlew bootRun

# En Windows PowerShell
foreach($line in Get-Content .env) {
  if($line -match '^[^#]') {
    $key, $value = $line -split '=', 2
    [Environment]::SetEnvironmentVariable($key, $value)
  }
}
./gradlew bootRun
```

**Nota:** El archivo `.env` está incluido en `.gitignore` para no compartir credenciales sensibles en el repositorio.

## Despliegue en Render con Docker

Para desplegar esta aplicación en Render usando Docker, sigue estos pasos:

1. Asegúrate de tener un repositorio Git con el código de la aplicación (incluyendo el Dockerfile).

2. Regístrate en [Render](https://render.com/).

3. En el Dashboard de Render, selecciona "New" y luego "Web Service".

4. Conecta tu repositorio de GitHub.

5. Configura el servicio:

   - **Nombre**: Elige un nombre para tu aplicación
   - **Entorno**: Selecciona "Docker"
   - **Rama**: Selecciona la rama principal (main/master)
   - **Plan**: Selecciona el plan gratuito

6. En la sección de "Environment Variables", agrega las siguientes variables:

   ```
   DB_URL=[URL de tu base de datos externa]
   DB_USERNAME=[Usuario de la base de datos]
   DB_PASSWORD=[Contraseña de la base de datos]
   DB_DRIVER=com.mysql.cj.jdbc.Driver
   ```

7. Haz clic en "Create Web Service".

Render detectará automáticamente el Dockerfile y lo usará para construir y ejecutar tu aplicación.

**Nota**: En el plan gratuito, la aplicación se suspenderá después de 15 minutos de inactividad y tardará aproximadamente 30 segundos en responder cuando reciba tráfico nuevamente.
