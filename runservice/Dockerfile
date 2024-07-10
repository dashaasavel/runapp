FROM openjdk:17-ea-16-jdk

WORKDIR /app
COPY target/runservice.jar /app/
COPY target/classes/config/application.properties /app/config/

EXPOSE 8082
EXPOSE 8092

CMD ["java", "-jar", "runservice.jar"]