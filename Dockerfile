FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY target/finance-tracker.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]