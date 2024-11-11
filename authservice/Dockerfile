FROM openjdk:17-ea-16-jdk

WORKDIR /app
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:15386

COPY target/authservice.jar /app/
COPY target/classes/config/ /app/config/

EXPOSE 8086 8096 11386 15386

CMD ["java", "-jar", "authservice.jar"]