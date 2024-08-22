FROM openjdk:21-jdk-slim

COPY target/testovoe_for_greenatom-1.0-SNAPSHOT.jar /app.jar

WORKDIR /app

CMD ["java", "--enable-preview", "-jar", "/app.jar"]
