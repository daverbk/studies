---
slug: /spring/jpa
title: JPA 📀
description: Jakarta Persistence API and Hiberante.
sidebar_position: 4
sidebar_custom_props:
  emoji: 📀
---

# JPA

## Hibernate

Framework for persisting / saving Java objects in a database
The default implementation of JPA

#### Benefits

* Hibernate handles all the low-level SQL
* Minimizes the amount of JDBC code you have to develop
* Hibernate provides the Object-to-Relational Mapping

## JPA

Jakarta Persistence API is only a specification

* Defines a set of interfaces
* Requires an implementation to be usable

JPA 3.1 has the following vendors

* Hibernate
* EclipseLink
* DataNucleus

#### Benefits

* Having a standard API, we aren't locked to vendor's implementation
* Maintain portable, flexible code by coding to JPA spec (interfaces)

## Data Source Configuration

Based on configs, Spring Boot will automatically create the beans that can then be injected via DAOs

Spring Boot will automatically configure your data source based on entries from pom

* JDBC Driver
* ORM
* DB connection info from `application.properties`

``` properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_tracker
spring.datasource.username=springstudent
spring.datasource.password=springstudent
```

## Entity Class

Java class that is mapped to a database table

* Must be annotated with @Entity
* Must have a public or protected no-argument constructor
* The class can have other constructor
  * Map class to database table
  ```java
    @Entity
    @Table(name="student")
    public class Student { }
  ```
  * Map fields to database columns
  ```java
    @Entity
    @Table(name="student")
    public class Student {
    
        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        @Column(name="id")
        private int id;
    
        @Column(name="first_name")
        private String firstName; 
  }
  ```
  
#### ID Generation Strategies

| Name                    |                                 Description                                 |
|-------------------------|:---------------------------------------------------------------------------:|
| GenerationType.AUTO     |          Pick an appropriate strategy for the particular database           |
| GenerationType.IDENTITY |             Assign primary keys using database identity column              |
| GenerationType.SEQUENCE |                Assign primary keys using a database sequence                |
| GenerationType.TABLE    | Assign primary keys using an underlying database table to ensure uniqueness |

## JPA Entity Manager

* JPA Entity Manager needs a Data Source
* The Data Source defines database connection info
* JPA Entity Manager and Data Source are automatically created by Spring Boot
* Based on the file: application.properties (JDBC URL, user id, password, etc …)

## Spring @Transactional

Begins and ends a transaction for your JPA code

## Spring @Repository

* Specialized Annotation for DAOs
* Spring will automatically register the DAO implementation
* Spring also provides translation of any JDBC related exceptions

## Saving a Java Object

```java
// create Java object
Student theStudent = new Student("Paul", "Doe", "paul@uni.com");
        
// save it to database
entityManager.persist(theStudent);
```

## Retrieving a Java Object

```java
// create Java object
Student theStudent = new Student("Paul", "Doe", "paul@uni.com");

// save it to database
entityManager.persist(theStudent);

// now retrieve from database using the primary key
int theId = 1;
Student myStudent = entityManager.find(Student.class, theId);
```

## JPA Query Language

Similar in concept to SQL, but JPQL is based on entity name and entity fields

#### Querying for Java Object

```java
TypedQuery<Student> query = entityManager.createQuery("from Student WHERE lastName='Doe'", Student.class);
List<Student> students= query.getResultList();
```

#### JPQL Named Parameters

```java
TypedQuery<Student> theQuery = entityManager.createQuery("FROM Student WHERE lastName=:theData", Student.class);
theQuery.setParameter("theData", theLastName);
```

## Update a record

```java
Student student = entityManager.find(Student.class, 1);

// change first name to "Rat"
student.setFirstName("Rat");
entityManager.merge(student);
```

#### Update for all

```java
int numRowsUpdated = entityManager
.createQuery("UPDATE Student SET lastName='Rats'”)
.executeUpdate();
```

## Delete a record

```java
// retrieve the student
int id = 1;
Student theStudent = entityManager.find(Student.class, id);

// delete the student
entityManager.remove(theStudent);
```

#### Delete based on a condition

```java
int numRowsDeleted = entityManager
.createQuery("DELETE FROM Student WHERE lastName=‘Crazy’")
.executeUpdate();
```

#### Delete all

```java
int numRowsDeleted = entityManager
.createQuery("DELETE FROM Student")
.executeUpdate();
```

## Create Database Tables from Java Code

#### Configuration

``` properties
spring.jpa.hibernate.ddl-auto=create
```

|    Value    |                                                       Description                                                        |
|:-----------:|:------------------------------------------------------------------------------------------------------------------------:|
|    none     |                                               No action will be performed                                                |
| create-only |                                             Database tables are only created                                             |
|    drop     |                                               Database tables are dropped                                                |
|   create    |                             Database tables are dropped followed by database tables creation                             |
| create-drop | Database tables are dropped followed by database tables creation.<br/> On application shutdown, drop the database tables |
|  validate   |                                           Validate the database tables schema                                            |
|   update    |                                            Update the database tables schema                                             |

## Recommendation from Chad

> [!CAUTION] 
> In general, I don’t recommend auto generation for enterprise, real-time projects 
> > You can VERY easily drop PRODUCTION data if you are not careful

> [!TIP] 
> I recommend SQL scripts
> > * Corporate DBAs prefer SQL scripts for governance and code review 
> > * The SQL scripts can be customized and fine-tuned for complex database designs 
> > * The SQL scripts can be version-controlled 
> > * Can also work with schema migration tools such as Liquibase and Flyway
