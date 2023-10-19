FROM khipu/openjdk17-alpine:latest
WORKDIR /app
COPY target/ekartms-0.0.1-SNAPSHOT.jar kartApp.jar
CMD ["java", "-jar", "kartApp.jar"]
EXPOSE 8080