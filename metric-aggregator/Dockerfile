FROM openjdk:17-ea-16-jdk

WORKDIR /app
COPY target/metric-aggregator.jar /app/
COPY target/classes/config/ /app/config/

#EXPOSE 8083
#EXPOSE 8093

CMD ["java", "-jar", "metric-aggregator.jar"]