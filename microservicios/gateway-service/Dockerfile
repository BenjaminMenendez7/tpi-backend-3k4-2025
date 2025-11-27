FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copia el jar
COPY target/gateway-service-0.0.1-SNAPSHOT.jar app.jar

# Copia la configuración (ESTO ES LO QUE FALTABA)
COPY src/main/resources/application.yml /app/application.yml

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "app.jar"]