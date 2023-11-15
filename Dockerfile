FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar libms-ci-cd-flow.jar
EXPOSE 8080
ADD target/springboot-images-new.jar springboot-images-new.jar
ENTRYPOINT ["java","-jar","/springboot-images-new.jar"]