FROM openjdk:17-ea-16-jdk

WORKDIR /app
ENV JAVA_TOOL_OPTIONS -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:15384

COPY target/metric-aggregator.jar /app/
COPY target/classes/config/ /app/config/

EXPOSE 8084 15384
CMD ["java", "-jar", "metric-aggregator.jar"]