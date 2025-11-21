# Imagen base con Java 21
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiar archivos del proyecto
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn .mvn

# Dar permisos
RUN chmod +x ./mvnw

# Compilar
RUN ./mvnw clean package -DskipTests

# Ejecutar
CMD ["java", "-jar", "target/*.jar"]