FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar libms-ci-cd-flow.jar
EXPOSE 8080
ADD target/libms-ci-cd-flow.jar libms-ci-cd-flow.jar
ENTRYPOINT ["java","-jar","/libms-ci-cd-flow.jar"]