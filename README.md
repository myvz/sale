# ReadingIsGood Sale

For building and running the application you need:

- [JDK 11](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven 3](https://maven.apache.org)

## Running the application locally

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `de.codecentric.springbootsample.Application` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```

## Testing Sample Requests

There are POSTMAN request samples on the repo. You can easily test sale api services.

Please see file --> readingisgood.postman_collection.json
```shell
mvn spring-boot:run
```

## Building a Docker Image

Sale service use jib maven plugin in order to create docker images

You can see its documentation https://github.com/GoogleContainerTools/jib

```shell
mvn clean install
```

## Securing Sale Api Services

Sale service secures api services using signature verified jwt token. You can set signature key by setting ${security.jwt.secret} property.
In tests, you can use already generated for John Doer jwt : Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyLCJpc3MiOiJSZWFkaW5nSXNHb29kIn0.7UqcWUkjxSHgnAVf8lPBnPTs6HHUQEVGi9OWVUUvcaA

You can see its documentation hhttps://jwt.io/