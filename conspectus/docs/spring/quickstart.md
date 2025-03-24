# Notes 

<!-- TOC -->
* [Notes](#notes-)
  * [Spring goals](#spring-goals)
  * [Standard Directory Structure](#standard-directory-structure)
  * [Project Coordinates](#project-coordinates)
  * [Why starters](#why-starters)
  * [Spring Boot Starter Parent](#spring-boot-starter-parent)
  * [Spring Boot Dev Tools](#spring-boot-dev-tools)
  * [Spring Boot Actuator](#spring-boot-actuator)
  * [Running from the Command-Line](#running-from-the-command-line)
    * [Using java -jar](#using-java--jar)
    * [Using maven plugin](#using-maven-plugin)
  * [Spring Boot Properties](#spring-boot-properties)
<!-- TOC -->

## Spring goals
* Lightweight development with Java POJOs (Plain-Old-Java-Objects)
* Dependency injection to promote loose coupling
* Declarative programming with Aspect-Oriented-Programming (AOP)
* Minimize boilerplate Java code

## Standard Directory Structure

| Directory          |                               Description                                |
|--------------------|:------------------------------------------------------------------------:|
| src/main/java      |                             Java source code                             |
| src/main/resources |                        Properties / config files                         |
| src/main/webapp    |  JSP files and web config files other web assets (images, css, js, etc)  |
| src/test           |                     Unit testing code and properties                     |
| target             | Destination directory for compiled code.  Automatically created by Maven |

## Project Coordinates

| Name        |                                               Description                                                |
|-------------|:--------------------------------------------------------------------------------------------------------:|
| Group ID    |            Name of company, group, or organization. Convention is to use reverse domain name             |
| Artifact ID |                                          Name for this project                                           |
| Version     | A specific release version like: 1.0, 1.6, 2.0 If project is under active development then: 1.0-SNAPSHOT |                                                                                |

When adding a dependency version is optional,
but it's best practice to include the version for repeatable builds

## Why starters
* A curated list of Maven dependencies
* A collection of dependencies grouped together
* Tested and verified by the Spring Development team
* Makes it much easier for the developer to get started with Spring
* Reduces the amount of Maven configuration

Starters list can be found [here](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using.build-systems.starters)

## Spring Boot Starter Parent

* Default Maven configuration: Java version, UTF-encoding etc
* Dependency management
* Use version on parent only – `spring-boot-starter-*` dependencies inherit version from it
* Default configuration of Spring Boot plugin

## Spring Boot Dev Tools

Automatically restarts your application when code is updated

Set up for IntelliJ:

```
Preferences > Build, Execution, Deployment > Compiler > Build project automatically
Preferences > Advanced Settings > Allow auto-make to ...
```

Dev Tools documentation can be found [here](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#using.devtools) 

## Spring Boot Actuator

* Exposes endpoints to monitor and manage your application
* Automatically exposes endpoints for metrics out-of-the-box
* Endpoints are prefixed with: /actuator

Actuator endpoints can be found [here](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#actuator.endpoints)

## Running from the Command-Line

### Using java -jar

```shell
mvn package
java -jar ourcoolapp-0.0.1.jar
```

### Using maven plugin

```shell
mvn spring-boot:run
```

## Spring Boot Properties

We can read our custom properties from `application.properties`
via the `@Value` annotation

Common application properties are listed [here](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties)
