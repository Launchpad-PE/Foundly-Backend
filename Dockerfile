# Construcción
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Ejecución
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Configurar puerto
EXPOSE 8080

# Crear usuario no-root (seguridad)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Comando de inicio
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]