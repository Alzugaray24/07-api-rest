# Etapa de construcción
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copiar archivos del proyecto
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Instalar dependencias adicionales
RUN apk add --no-cache curl

# Descargar dependencias (aprovechando la caché de Docker)
RUN ./gradlew dependencies --no-daemon || return 0

# Ahora copia el código fuente
COPY src ./src

# Dar permiso de ejecución al gradlew y construir
RUN chmod +x ./gradlew
RUN ./gradlew build -x test --no-daemon

# Descargar el driver PostgreSQL directamente
RUN mkdir -p build/libs/dependency
RUN curl -L -o build/libs/dependency/postgresql.jar https://jdbc.postgresql.org/download/postgresql-42.6.0.jar

# Etapa de ejecución
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copiar el JAR de la etapa de construcción
COPY --from=build /app/build/libs/*.jar app.jar
# Copiar el driver PostgreSQL
COPY --from=build /app/build/libs/dependency/postgresql.jar /app/postgresql.jar

# Variables de entorno para configuración de memoria
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=prod -jar app.jar"] 