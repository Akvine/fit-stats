# fit-stats
Application for fitness statistic tracking

## Table of contents
* [General info](#general-info)
* [Requirements](#requirements)
* [Running the application locally](#running-the-application-locally)
* [Technologies](#technologies)

## General info
Application for fitness statistic tracking and taking into account various indicators:

* Calories
* Proteins
* Fats
* Alcohol
* Carbohydrates

The application supports integration with the telegram bot

## Requirements
For building and running the application you need:
- [JDK 11](https://www.oracle.com/java/technologies/downloads/#java11-linux)
- [Maven 3](https://maven.apache.org)

## Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the `main` method
in the `ru.akvine.fitstats.FitStatsHubApplication` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Technologies
Project is created with:
* Java version: 11
* Spring Boot version: 2.7.17
* Maven version: 3
* Migration library: Liquibase
* ORM: Hibernate
