# Notes

<!-- TOC -->
* [Notes](#notes)
  * [Inversion of Control (IoC)](#inversion-of-control-ioc)
  * [Dependency Injection](#dependency-injection)
      * [Recommended Injection Types](#recommended-injection-types)
      * [Which one to use?](#which-one-to-use)
      * [AutoWiring](#autowiring)
  * [@Component](#component)
  * [Scanning for Component Classes](#scanning-for-component-classes)
  * [Setter Injection](#setter-injection)
  * [Qualifiers](#qualifiers)
  * [Primary](#primary)
  * [Which one: @Primary or @Qualifier?](#which-one-primary-or-qualifier)
  * [Lazy Initialization](#lazy-initialization)
  * [Bean Scopes](#bean-scopes)
      * [Singleton](#singleton)
      * [Additional Spring Bean Scopes](#additional-spring-bean-scopes)
  * [Bean Lifecycle Methods](#bean-lifecycle-methods)
  * [Configuring Beans](#configuring-beans)
<!-- TOC -->

## Inversion of Control (IoC)

The approach of outsourcing the
construction and management of objects

## Dependency Injection

* The dependency inversion principle
* The client delegates to another object
the responsibility of providing its
dependencies

#### Recommended Injection Types

* Constructor Injection
* Setter Injection

#### Which one to use?

* Constructor Injection
  * Use this when you have required dependencies
  * Generally recommended by the `spring.io` development team as first choice
* Setter Injection
  * Use this when you have optional dependencies
  * If dependency is not provided, your app can provide reasonable default logic

#### AutoWiring

Spring will look for a class that matches by type: class or interface and inject it automatically

## @Component

* Marks the class as a Spring Bean
* Makes the bean available for dependency injection

## Scanning for Component Classes

* Spring will scan your Java classes for special annotations
* Automatically register the beans in the Spring container

@SpringBootApplication is composed of

| Annotation               |                                      Description                                      |
|--------------------------|:-------------------------------------------------------------------------------------:|
| @EnableAutoConfiguration |                   Enables Spring Boot's auto-configuration support                    |
| @ComponentScan           | Enables component scanning of current package<br/>Also recursively scans sub-packages |
| @Configuration           |     Able to register extra beans with @Bean or import other configuration classes     |

* By default, Spring Boot starts component scanning
  * From same package as your main Spring Boot application
  * Also scans sub-packages recursively
* This implicitly defines a base search package
  * Allows you to leverage default component scanning
  * No need to explicitly reference the base package name

```java
  @SpringBootApplication(
  scanBasePackages = {"com.studies.springcoredemo",
                      "com.studies.util",
                      "org.acme.cart",
                      "edu.cmu.srs"})
  public class SpringcoredemoApplication { }
```

## Setter Injection

Inject dependencies by calling setter methods on your class

## Qualifiers

* Our application can have multiple implementation of an injected interface
* ```java
  @Autowired
  public DemoController(@Qualifier("cricketCoach") Coach coach) {
      myCoach = coach;
  }
  ```
* Beans ids are same as class names, first character lower-case (until custom)

## Primary

* Alternative solution for the issue mentioned in [Qualifiers](#qualifiers)
* When using @Primary, can have only one for multiple implementations
* ```java
  @Component
  @Primary
  public class TrackCoach implements Coach { }
  ```
* If we mix @Primary and @Qualifier, @Qualifier has higher priority

## Which one: @Primary or @Qualifier?

* @Primary leaves it up to the implementation classes and could have the issue of multiple @Primary classes leading to an error
* @Qualifier allows us to be very specific on which bean you want
* @Qualifier is more recommended as it's more specific and has a higher priority

## Lazy Initialization

* By default, when your application starts, all beans are initialized
* Instead of creating all beans up front, we can specify lazy initialization
* A bean will only be initialized in the following cases:
  * It is needed for dependency injection
  * Or it is explicitly requested
* ```java
  @Component
  @Lazy
  public class TrackCoach implements Coach { }
  ```
* We can set lazy initialization for all beans in global configuration
* ``` properties
  spring.main.lazy-initialization=true
  ```

Advantages
* Only create objects as needed
* May help with faster startup time if you have large number of components

Disadvantages
* If you have web related components like @RestController, not created until requested
* May not discover configuration issues until too late
* Need to make sure you have enough memory for all beans once created

## Bean Scopes

Default scope is singleton

#### Singleton

* Spring Container creates only one instance of the bean, by default
* It is cached in memory
* All dependency injections for the bean will reference the same bean

Explicitly specified scope:

```java
  @Component
  @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
  public class CricketCoach implements Coach { }
```

#### Additional Spring Bean Scopes

| Scope          |                         Description                          |
|----------------|:------------------------------------------------------------:|
| singleton      | Create a single shared instance of the bean. Default scope.  |
| prototype      |   Creates a new bean instance for each container request.    |
| request        |    Scoped to an HTTP web request. Only used for web apps.    |
| session        |    Scoped to an HTTP web session. Only used for web apps.    |
| global-session | Scoped to a global HTTP web session. Only used for web apps. |

## Bean Lifecycle Methods

* We can add custom logic during bean initialization or destruction
* Initialization
  * ```java
    @PostConstruct
    public void doInitializationStuff() { }
    ```
* Destruction
  * ```java
    @PostConstruct
    public void doCleanupStuff() { }
    ```

## Configuring Beans

```java
  @Configuration
  public class SportConfig {
      @Bean
      public Coach swimCoach() {
          return new SwimCoach();
      }
  }
```

* The bean id defaults to the method name
* Then we can inject the bean using the bean id
* Whole of this has a use case – make third-party class available to Spring framework
