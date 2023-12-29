FROM openjdk:11
COPY target/*.jar /fit-stats/fit-stats.jar
RUN apt-get update && apt-get install -y bash
EXPOSE 8106
ENTRYPOINT ["java", "-jar", "/fit-stats/fit-stats.jar"]