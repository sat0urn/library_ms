FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar Library_MS.jar
ENTRYPOINT ["java","-jar","/Library_MS.jar"]
EXPOSE 8080