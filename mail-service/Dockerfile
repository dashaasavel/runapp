FROM openjdk:17-ea-16-jdk

WORKDIR /app
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:15385

COPY target/mail-service.jar /app/
COPY target/classes/config/ /app/config/

EXPOSE 8085 8095 15385

CMD ["java", "-jar", "mail-service.jar"]