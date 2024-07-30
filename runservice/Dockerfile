FROM openjdk:17-ea-16-jdk

WORKDIR /app
COPY target/runservice.jar /app/
COPY target/classes/config/ /app/config/

EXPOSE 8082 8092

CMD ["java", "-jar", "runservice.jar"]