FROM openjdk:17-ea-16-jdk

WORKDIR /app
COPY target/metric-agregator.jar /app/
COPY target/classes/config/ /app/config/

#EXPOSE 8083
#EXPOSE 8093

CMD ["java", "-jar", "metric-agregator.jar"]