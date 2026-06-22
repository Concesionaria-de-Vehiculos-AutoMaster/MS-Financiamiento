# --- Etapa 1: Compilación ---
FROM maven:3.9.6-eclipse-temurin-25-alpine AS build
WORKDIR /app
# Copiar el pom y descargar dependencias (aprovecha la caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y compilar el JAR omitiendo los tests para agilizar
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Imagen de ejecución ---
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
# Copiar el JAR generado desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto correspondiente (8080 para ventas, 8081 para stock, etc.)
EXPOSE 8084

# Comando de arranque optimizado para contenedores
ENTRYPOINT ["java", "-jar", "app.jar"]
