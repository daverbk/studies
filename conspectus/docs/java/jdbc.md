---
slug: /java/jdbc
title: Java Database Connectivity 📀
description: JDBC. JPA w/ Hibernate.
sidebar_position: 13
sidebar_custom_props:
  emoji: 📀
---

# Java Database Connectivity

## Relational Database Basics

### Schema

A schema represents a logical grouping, or namespace, for database objects such as tables, views,
and procedures. In some database systems, like PostgreSQL and Oracle, the term `schema` is used to  
represent the namespace within a database where objects are organized. These systems can have
multiple schemas within a single database. In other database systems, like MySQL, the term schema is
often used interchangeably with database.

### Common Database Objects

| Object  | Description                                                                                                                                                            |
|---------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `table` | A table stores rows or records of data, in attribute fields, with values specific to that record.                                                                      |
| `index` | An index consists of a table name, a key value and a record locator field, to quickly access a record from a table. A primary key is a unique identifier for a record. |
| `view`  | A view is a stored query, which can be accessed like a table, but hides the details of the table implementation from the client.                                       |
| `user`  | A user represents a package a privileges to database artifacts given to an account.                                                                                    |

### SQL

In databases, the language lets us create objects, populate them with information, create
relationships, and query data. This language is called the Structured Query Language, or SQL.

#### DDL

The Data Definition Language is used to define, create, manage, and modify the database objects. DDL
statements don't manipulate the data in the object, instead they manipulate data structures, that
store and organize the data.

| Command    | Description                                                                    |
|------------|--------------------------------------------------------------------------------|
| `create`   | Used to create database objects like tables, indexes, views, and schemas.      |
| `alter`    | Used to modify the structure of existing database objects.                     |
| `drop`     | Used to delete or remove database object.                                      |
| `truncate` | Used to remove all rows from a table while keeping the table structure intact. |
| `rename`   | Used to rename database objects.                                               |

#### DML

The Data Manipulation Language, is used to interact with, and manipulate, the data stored within the
database objects or artifacts. DML statements perform operations like inserting, updating,
retrieving, and deleting data in the database.

| Command  | Description                                    |
|----------|------------------------------------------------|
| `select` | Used to retrieve data from one or more tables. |
| `insert` | Used to add new rows of data into a table.     |
| `update` | Used to modify existing data in a table.       |
| `delete` | Used to remove rows from a table.              |

### Relations

Database tables can be associated with one another, through different kinds of relationships.

| Relationship | Description                                                              |
|--------------|--------------------------------------------------------------------------|
| One to One   | One row in the first table is related to only one row in a second table. |
| One to Many  | One row in the first table is related to many rows in a second table.    |
| Many to Many | Many rows in the first table are related to many rows in a second table. |

### Normalization & Join

On normalization and normal forms check out [this](../dev-concepts.md#normal-forms-in-dbms). A join
is a SQL clause, that combines rows from two or more tables, based on a common field.

| Join Type    | Result                                                                                               |
|--------------|------------------------------------------------------------------------------------------------------|
| `inner join` | Returns all rows from both tables where the join condition is met                                    |
| `left join`  | Returns all rows from the left table, even if there is no matching row in the right table.           |
| `right join` | Returns all rows from the right table, even if there is no matching row in the left table.           |
| `full join`  | Returns all rows from both tables, regardless of whether there is a matching row in the other table. |
| `cross join` | Returns all possible combinations of rows from the two tables.                                       |
