---
slug: /dev
title: Development concepts 🧱
sidebar_position: 2 
pagination_next: null
---

# Things to know about development

## OOP

Programming paradigm based on the concept of objects, which can contain data and code: data in the form of
fields (often known as attributes or properties), and code in the form of procedures (often known as methods)

### Polymorphism

One interface, multiple implementations

#### Parametric

In Java, it is implemented using inheritance. The child class inherits the method signatures of the parent class, but
the implementation of these methods can be different to suit the specifics of the child class. This is called method
overriding. Other functions can operate on the parent class object, but one of the child classes will be substituted for
it at runtime - late binding

#### Ad hock

When methods with the same signature take different parameters as input. This is called method
overloading

### Inheritance

An abstract data type can inherit the data and functionality of some existing type, facilitating the reuse of software
components

### Encapsulation

Hiding the internal implementation of the class and separating it from the external user interface

### Abstraction

Highlighting significant information and excluding insignificant information from consideration. OOP considers only data
abstraction, implying a set of the most significant characteristics of an object that are available to the rest of the
program

## [SOLID](https://drive.google.com/file/d/0BwhCYaYDn8EgODUxZTJhOWEtMTZlMi00OWRiLTg0ZmEtZWQ5ODRlY2RmNDlk/view?resourcekey=0-yJZQZu3pFfMafpcZ_O8y0Q)

### S — Single Responsibility

`A class should have a single responsibility`

If a Class has many responsibilities, it increases the possibility of bugs because making changes to one of its
responsibilities, could affect the other ones without you knowing

### O — Open-Closed

`Classes should be open for extension, but closed for modification`

Changing the current behaviour of a Class will affect all the systems using that Class. If you want the Class to perform
more functions, the ideal approach is to add to the functions that already exist NOT change them

### L — Liskov Substitution

`If S is a subtype of T, then objects of type T in a program may be replaced with objects of type S without altering
any of the desirable properties of that program`

The child Class should be able to process the same requests and deliver the same result as the parent Class, or it could
deliver a result that is of the same type

### I — Interface Segregation

`Clients should not be forced to depend on methods that they do not use`

This principle aims at splitting a set of actions into smaller sets so that a Class executes ONLY the set of actions it
requires

### D — Dependency Inversion

`High-level modules should not depend on low-level modules. Both should depend on the abstraction`

`Abstractions should not depend on details. Details should depend on abstractions`

This principle says a Class should not be fused with the tool it uses to execute an action. Rather, it should be fused
to the interface that will allow the tool to connect to the Class

It also says that both the Class and the interface should not know how the tool works. However, the tool needs to meet
the specification of the interface

## ACID

[ACID Database Properties](https://intuting.medium.com/acid-database-properties-6bc2b049ed2d)

### A — Atomicity

All operations in a transaction succeed or every operation is rolled back.

### C — Consistency

On the completion of a transaction, the database is structurally sound.

### I — Isolation

Transactions do not contend with one another. Contentious access to data is moderated by the database so that
transactions appear to run sequentially.

### D — Durability

The results of applying a transaction are permanent, even in the presence of failures.

## KISS

[KISS and DRY Principles in Software Engineering](https://medium.com/@susithapb/kiss-and-dry-principles-in-software-engineering-3aee36e72879#1376)

Keep It Simple, Stupid: This principle advocates for simplicity in design. It suggests that systems should be
kept as simple as possible, avoiding unnecessary complexity

## DRY

[KISS and DRY Principles in Software Engineering](https://medium.com/@susithapb/kiss-and-dry-principles-in-software-engineering-3aee36e72879#b953)

Don't Repeat Yourself: DRY principle states that every piece of knowledge or logic should have a single,
unambiguous representation within a system. It encourages code reuse and helps in maintaining consistency

## YAGNI

[The Principles of Clean Code: DRY, KISS, and YAGNI](https://medium.com/@curiousraj/the-principles-of-clean-code-dry-kiss-and-yagni-f973aa95fc4d#c66a)

You Ain't Gonna Need It: YAGNI advises against adding functionality until it's actually needed. It discourages
developers from implementing features based on speculative future requirements

## OLTP vs OLAP

| Feature         | 	OLTP (Online Transaction Processing)              | OLAP (Online Analytical Processing)             |
|:----------------|----------------------------------------------------|-------------------------------------------------|
| Purpose         | Handle real-time transactions	                     | Analyze large volumes of historical data        |
| Focus           | Fast reads/writes for day-to-day operations	       | Complex queries for business insights           |
| Speed	          | Optimized for transactional speed and consistency	 | Optimized for query performance and flexibility |
| Data freshness	 | Real-time	                                         | Near-real-time or batch updated                 |

## [Gradle Lifecycle](https://docs.gradle.org/current/userguide/build_lifecycle.html)

Gradle goes through three main phases during build

> Initialization ➝ Configuration ➝ Execution

1. Initialization

- Gradle determines which projects are involved 
- Gradle reads `settings.gradle.kts`

2. Configuration Phase

- Gradle evaluates all `build.gradle(.kts)` scripts
- All tasks are created and configured
- The entire task graph is built, even if you only run one task
- This phase sets up inputs/outputs, dependencies, and logic

3. Execution

- Only the tasks explicitly requested, and their dependencies, are executed
- Each task's `doFirst`, `doLast`, or actions are called here
- Gradle checks if a task is up-to-date via inputs/outputs - if so, it can be skipped (incremental
  build)

## [Normal Forms in DBMS](https://www.geeksforgeeks.org/dbms/normal-forms-in-dbms/)

**_Normalization_** is a systematic approach to organize data within a database to reduce redundancy and
eliminate undesirable characteristics such as insertion, update, and deletion anomalies. **_Normal
Forms_** are different stages of normalization, and each stage imposes certain rules to improve the
structure and performance of a database.

### Why?

- Reduces Data Redundancy: duplicate data is stored efficiently, saving disk space and reducing
  inconsistency
- Improves Data Integrity: ensures the accuracy and consistency of data by organizing it in a
  structured manner
- Simplifies Database Design: by following a clear structure, database designs become easier to
  maintain and update
- Optimizes Performance: Reduces the chance of anomalies and increases the efficiency of database
  operations

### Stages

Each stage must satisfy the previous stage's requirements.

1. First Normal Form (`1NF`): Eliminating Duplicate Records

- All columns contain atomic values (i.e., indivisible values)
- Each row is unique (i.e., no duplicate rows)
- Each column has a unique name
- The order in which data is stored does not matter

2. Second Normal Form (`2NF`): Eliminating Partial Dependency

Every non-prime attribute (non-key attribute) must depend on the entire primary key, not just a part
of it.

3. Third Normal Form (`3NF`): Eliminating Transitive Dependency

Non-prime attributes should not depend on other non-prime attributes.

4. Boyce-Codd Normal Form (`BCNF`): A stronger form of 3NF

A stricter version of `3NF` where for every non-trivial functional dependency (`X` → `Y`), `X` must be
a superkey (a unique identifier for a record in the table).

5. Fourth Normal Form (`4NF`): Removing Multi-Valued Dependencies

A table is in `4NF` if it is in `BCNF` and has no multivalued dependencies. A multivalued dependency
occurs when one attribute determines another, and both attributes are independent of all other
attributes in the table.

6. Fifth Normal Form (`5NF`): Eliminating Join Dependency

When a table is in `4NF` and all join dependencies are removed. This form ensures that every table is
fully decomposed into smaller tables that are logically connected without losing information.

### Over-Normalization

Excessive normalization can lead to

- Complex Queries: Too many tables may result in multiple joins, making queries slow and difficult
  to manage
- Performance Overhead: Additional processing required for joins in overly normalized databases may
  hurt performance, especially in large-scale systems

In many cases, **_denormalization_** (combining tables to reduce the need for complex joins) is used
for performance optimization in specific applications, such as reporting systems.

## [Metric Structure](https://prometheus.io/docs/concepts/data_model/)

Every metric is uniquely identified by its metric name and optional key-value pairs called labels.

### Names

- Metric names SHOULD specify the general feature of a system that is measured (e.g.
  `http_requests_total` - the total number of HTTP requests received)
- Metric names MAY use any UTF-8 characters
- Metric names SHOULD match the regex `[a-zA-Z_:][a-zA-Z0-9_:]*` for the best experience. Metric
  names outside of that set will require quoting e.g. when used in PromQL

### Labels

- Label names MAY use any UTF-8 characters
- Label names beginning with `__` (two underscores) MUST be reserved for internal Prometheus use
- Label names SHOULD match the regex `[a-zA-Z_][a-zA-Z0-9_]*` for the best experience. Label names
  outside of that regex will require quoting e.g. when used in PromQL (see the UTF-8 guide)
- Label values MAY contain any UTF-8 characters
- Labels with an empty label value are considered equivalent to labels that do not exist

## [Metric Types](https://prometheus.io/docs/concepts/metric_types/)

### Counter

A **counter** is a cumulative metric that represents a single **monotonically increasing** counter
whose value can only increase or be reset to zero on restart. For example, you can use a counter to
represent the number of requests served, tasks completed, or errors.

### Gauge

A **gauge** is a metric that represents a single numerical value that can arbitrarily go up and down.
Gauges are typically used for measured values like temperatures or current memory usage, but also 
"counts" that can go up and down, like the number of concurrent requests.

### Histogram

A histogram samples observations (usually things like request durations or response sizes) and
counts them in configurable buckets. It also provides a sum of all observed values. A histogram with
a base metric name of `<basename>` exposes multiple time series during a scrape

- cumulative counters for the observation buckets, exposed as
  `<basename>_bucket{le="<upper inclusive bound>"}`
- the total sum of all observed values, exposed as `<basename>_sum`
- the count of events that have been observed, exposed as `<basename>_count` (identical to
  `<basename>_bucket{le="+Inf"}` above)
 
### Summary

Similar to a histogram, a summary samples observations (usually things like request durations and
response sizes). While it also provides a total count of observations and a sum of all observed
values, it calculates configurable quantiles over a sliding time window. A summary with a base
metric name of `<basename>` exposes multiple time series during a scrape:

- streaming φ-quantiles (0 ≤ φ ≤ 1) of observed events, exposed as `<basename>{quantile="<φ>"}`
- the total sum of all observed values, exposed as `<basename>_sum`
- the count of events that have been observed, exposed as `<basename>_count`

### [Summary vs Histogram](https://prometheus.io/docs/practices/histograms/)

If we use a **summary**, we control the error in the dimension of φ. If we use a **histogram**, we 
control the error in the dimension of the observed value (via choosing the appropriate bucket
layout). With a broad distribution, small changes in φ result in large deviations in the observed
value. With a sharp distribution, a small interval of observed values covers a large interval of φ.

Two rules of thumb

1. If you need to aggregate, choose histograms
2. Otherwise, choose a histogram if you have an idea of the range and distribution of values that
   will be observed. Choose a summary if you need an accurate quantile, no matter what the range and
   distribution of the values is

## Database [Partitioning](https://www.postgresql.org/docs/current/ddl-partitioning.html) & [Sharding](https://aws.amazon.com/what-is/database-sharding/)

| Aspect          | Partitioning                                                                        | Sharding                                                      |
|:----------------|-------------------------------------------------------------------------------------|---------------------------------------------------------------|
| Definition      | Dividing a single database/table into parts (partitions) based on a key or strategy | Splitting data across multiple databases or servers (shards)  |
| Scope           | Within a single database instance                                                   | 	Across multiple database instances                           |
| Managed By	     | Usually the database engine itself	                                                 | Often requires application-level logic or external middleware |
| Goal            | 	Improve query performance and data organization                                    | Enable horizontal scaling, handle very large datasets         |
| Failure Domain	 | All partitions are on the same DB — one server fails, all partitions are down	      | Each shard is independent — one shard down ≠ total failure    |
| Examples        | Table partitioning in PostgreSQL by date range	                                     | User data sharded by user ID hash across 5 database servers   |

## [OSI](https://en.wikipedia.org/wiki/OSI_model)

| # | Layer Name     | Protocol Data Unit | Function                                                                                                                                         |
|---|----------------|--------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| 7 | `Application`  | Data               | High-level protocols such as for resource sharing or remote file access, e.g. HTTP                                                               |
| 6 | `Presentation` | Data               | Translation of data between a networking service and an application; including character encoding, data compression and encryption/decryption    |
| 5 | `Session`      | Data               | Managing communication sessions, i.e., continuous exchange of information in the form of multiple back-and-forth transmissions between two nodes |
| 4 | `Transport`    | Segment            | Reliable transmission of data segments between points on a network, including segmentation, acknowledgement and multiplexing                     |
| 3 | `Network`      | Packet, Datagram   | Structuring and managing a multi-node network, including addressing, routing and traffic control                                                 |
| 2 | `Data link`    | Frame              | Transmission of data frames between two nodes connected by a physical layer                                                                      |
| 1 | `Physical`     | Bit, Symbol        | Transmission and reception of raw bit streams over a physical medium                                                                             |



