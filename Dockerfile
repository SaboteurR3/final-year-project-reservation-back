FROM openjdk:17-oracle
COPY target/*.jar security.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "security.jar"]