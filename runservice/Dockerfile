FROM openjdk:17-ea-16-jdk

WORKDIR /app
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:15382

COPY target/runservice.jar /app/
COPY target/classes/config/ /app/config/

EXPOSE 8082 8092 15382

CMD ["java", "-jar", "runservice.jar"]