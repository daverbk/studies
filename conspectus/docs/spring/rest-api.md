# Notes

<!-- TOC -->
* [Notes](#notes)
  * [Web starter is required](#web-starter-is-required)
  * [Rest controller](#rest-controller)
  * [Serialization](#serialization)
  * [Path Variables](#path-variables)
  * [Exception Handling](#exception-handling)
  * [Global Exception Handling](#global-exception-handling)
    * [@ControllerAdvice](#controlleradvice)
  * [Rest service example](#rest-service-example)
  * [@Service](#service)
    * [Purpose of Service Layer](#purpose-of-service-layer)
    * [Service Layer - Best Practice](#service-layer---best-practice)
  * [Spring Data JPA](#spring-data-jpa)
    * [Advanced features available](#advanced-features-available)
    * [JpaRepository Docs](#jparepository-docs)
  * [Spring Data REST](#spring-data-rest)
    * [For Spring Data REST we need](#for-spring-data-rest-we-need)
    * [Spring Data REST advanced features](#spring-data-rest-advanced-features)
    * [@RepositoryRestResource](#repositoryrestresource)
    * [Spring Data Rest Docs](#spring-data-rest-docs)
    * [Spring Data Configuration Props](#spring-data-configuration-props)
  * [Different components covered so far](#different-components-covered-so-far)
  * [Spring Documentation](#spring-documentation)
  * [Understanding HATEOAS](#understanding-hateoas)
  * [HAL Data Format](#hal-data-format-)
<!-- TOC -->

## Web starter is required

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## Rest controller

`@RequestMapping` is needed at the class level to express shared mappings

| Mapping type   | Method |
|----------------|--------|
| @GetMapping    | GET    |
| @PostMapping   | POST   |
| @PutMapping    | PUT    |
| @DeleteMapping | DELETE |
| @PatchMapping  | PATCH  |


```java
@RestController
@RequestMapping("/test")
public class DemoRestController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello World!";
    }
}
```

## Serialization

* Spring uses the [Jackson Project ](https://github.com/FasterXML/jackson-databind)behind the scenes
* By default, Jackson will call appropriate getter/setter method

## Path Variables

We can bind path variable to method parameter using `@PathVariable`

```java
@GetMapping("/students/{studentId}")
public Student getStudent(@PathVariable int studentId) { }
```

## Exception Handling

1. Create a custom error response class
```java
public class StudentErrorResponse {
  private int status;
  private String message;
  private long timeStamp;
  
  // constructors
  // getters / setters
}
```
2. Create a custom exception class
```java
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String message) {
      super(message);
    } 
}
```
3. Update REST service to throw exception if student not found
```java
@GetMapping("/students/{studentId}")
public Student getStudent(@PathVariable int studentId) {

    if ((studentId >= theStudents.size()) || (studentId < 0)) {
        throw new StudentNotFoundException("Student id not found - " + studentId);
    }
    
    return theStudents.get(studentId);
}
```
4. Add an exception handler method using @ExceptionHandler
```java
@ExceptionHandler
public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {
    StudentErrorResponse error = new StudentErrorResponse();
    error.setStatus(HttpStatus.NOT_FOUND.value()); 
    error.setMessage(exc.getMessage()); 
    error.setTimeStamp(System.currentTimeMillis());
    
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND); 
}
```

## Global Exception Handling

### @ControllerAdvice

* Pre-process requests to controllers
* Post-process responses to handle exceptions 

```java
@ControllerAdvice
public class StudentRestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<StudentErrorResponse> handleException(StudentNotFoundException exc) {
        StudentErrorResponse error = new StudentErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value()); 
        error.setMessage(exc.getMessage()); 
        error.setTimeStamp(System.currentTimeMillis());
    
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND); 
    }
}
```

## Rest service example

| HTTP Method | Endpoint                      |    CRUD Action     |
|-------------|-------------------------------|:------------------:|
| POST        | `/api/employees`              |  Create a new one  |
| GET         | `/api/employees`              |   Read a list of   |
| GET         | `/api/employees/{employeeId}` |   Read a single    |
| PUT         | `/api/employees`              | Update an existing |
| DELETE      | `/api/employees/{employeeId}` | Delete an existing |


## @Service

### Purpose of Service Layer

* Service Facade design pattern
* Intermediate layer for custom business logic
* Integrate data from multiple sources (DAO/repositories)

```java
@Service
public class EmployeeServiceImpl implements EmployeeService { 
// inject EmployeeDAO ...

    @Override
    public List<Employee> findAll() {
        return employeeDAO.findAll(); 
    }
}
```

### Service Layer - Best Practice

* Apply transactional boundaries at the service layer
* It is the service layer’s responsibility to manage transaction boundaries
* For implementation code
  * Apply @Transactional on service methods
  * Remove @Transactional on DAO methods if they already exist

## Spring Data JPA

* Create a DAO and just plug in your `entity type` and `primary key`
* Spring will give you a CRUD implementation
* To do this our DAO should implement JpaRepository interface

```java
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
  // that's it :)
}
```

### Advanced features available
* Extending and adding custom queries with JPQL 
* Query Domain Specific Language (Query DSL) 
* Defining custom methods ([low-level coding](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.at-query))

### [JpaRepository Docs](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html)

## Spring Data REST

* Leverages your existing JpaRepository
* Spring will give you a REST CRUD implementation
* Spring Data REST will scan your project for JpaRepository 
* Expose REST APIs for each entity type for your JpaRepository
* By default, Spring Data REST will create endpoints based on entity type
  * First character of Entity type is lowercase
  * adds an "s" to the entity
* Just add Spring Data REST to your Maven POM file
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
```

### For Spring Data REST we need

1. Your entity: `Employee`
2. JpaRepository: `EmployeeRepository extends JpaRepository` 
3. Maven POM dependency for: `spring-boot-starter-data-rest`

### Spring Data REST advanced features
* Pagination, sorting and searching
* Extending and adding custom queries with JPQL Query Domain Specific Language (Query DSL)

### @RepositoryRestResource

Specify plural name / path with an annotation

```java
@RepositoryRestResource(path="members")
public interface EmployeeRepository extends JpaRepository<Employee, Integer> { }
```

### [Spring Data Rest Docs](https://spring.io/projects/spring-data-rest/)

### [Spring Data Configuration Props](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html#appendix.application-properties.data)

## Different components covered so far

| Annotation    | Meaning                                             |
|---------------|-----------------------------------------------------|
| `@Component`  | generic stereotype for any Spring-managed component |
| `@Repository` | stereotype for persistence layer                    |
| `@Service`    | stereotype for service layer                        |
| `@Controller` | stereotype for presentation layer (spring-mvc)      |

## [Spring Documentation](https://spring.io/projects/spring-boot/)

## [Understanding HATEOAS](https://en.wikipedia.org/wiki/HATEOAS)

## [HAL Data Format](https://en.wikipedia.org/wiki/Hypertext_Application_Language) 
