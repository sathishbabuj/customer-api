# Customer Api 
Spring boot based microservice for managing Customer

## Getting started

## Prerequisites

For versions and complete list of dependencies see pom.xml

- [JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html)
- [Spring Boot Restful webservice](https://spring.io/guides/gs/rest-service/)
- [Maven](https://maven.apache.org/)
- [Hateos](https://spring.io/projects/spring-hateoas)
- [H2 Inmemory database](http://www.h2database.com/html/main.html)
   H2 database is used only for local testing. Please configure proper database for Production.
- [Swagger](https://springfox.github.io/springfox/docs/current/#introduction)


## Building and deploying the application 
  
### Building the application

To build the project execute the following command:

```
mvn clean install
```

To run the test execute the following command
```
mvn test
```

To run the application locally execute the following command
```
mvn spring-boot:run
```

## Rest endpoint API documentation

Run the application locally and use the url given below

- [Customer Api docs](http://localhost:8080/swagger-ui.html#/customer-controller)
