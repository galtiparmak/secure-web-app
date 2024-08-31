FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/secure-web-app.jar /app/secure-web-app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/secure-web-app.jar"]