FROM openjdk:17-ea-16-jdk

WORKDIR /app
COPY target/userservice-0.0.1-SNAPSHOT.jar /app/
COPY target/classes/config/application.properties /app/config/

#EXPOSE 8080

CMD ["java", "-jar", "userservice-0.0.1-SNAPSHOT.jar"]