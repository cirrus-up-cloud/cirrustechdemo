# README #

### What is this repository for? ###

* Demo RESTful web service using Java & Spring Boot
* How to integrate [localstack](https://github.com/localstack/localstack), using a cool library, named [spring-localstack](https://github.com/fabianoo/spring-localstack) 

### How do I get set up? ###

* Compile with maven -> mvn package
* Start the service 
    * **with localstack:** java -Dspring.profiles.active=local -jar target/demo-java-service-1.0-SAMPLE.jar
    * **to hit AWS:** java -Dspring.profiles.active=dev -DaccessKey=A.. -DsecretKey=y.. -jar target/demo-java-service-1.0-SAMPLE.jar
