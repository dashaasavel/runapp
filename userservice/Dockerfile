FROM openjdk:17-ea-16-jdk

WORKDIR /app
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:15383

COPY target/userservice.jar /app/
COPY target/classes/config/ /app/config/

EXPOSE 8083 8093 11383 15383

CMD ["java", "-jar", "userservice.jar"]