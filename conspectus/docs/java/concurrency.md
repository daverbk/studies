---
slug: /java/concurrency
title: Concurrency 🧵
description: Multithreading in Java.
sidebar_position: 10 
sidebar_custom_props:
  emoji: 🧵 
---

# Concurrency

## Basics

Process is a unit of execution, that has its own memory space - heap. The heap isn't shared between two applications or two processes, they each have their own. Each process can have multiple threads. Every application has at least one thread, and that's the main thread.

- Creating a thread doesn't require as many resources as creating a process does
- Every thread created by a process, shares that process's memory space, the heap
- Each thread's got what's called a thread stack. This is memory, that only a single thread, will have access to

Every Java application runs as a single process, and each process can then have multiple threads within it. Every process has a heap, and every thread has a thread stack.

### Threads accessing memory

```mermaid
block-beta
    columns 4
    title{{"Process"}}:4

    l(["Thread One"])
    space:2
    r(["Thread Two"])

    heap(("Heap (shared)\n Objects")):4
    l --> heap
    r --> heap

    block:stack1:2
        columns 1
        stack1m("Stack memory")
        lv1("Local variable")
        ma1("Method argument")
    end
    space
    block:stack2:2
        columns 1
        stack2m("Stack memory")
        lv2("Local variable")
        ma2("Method argument")
    end

    l --"Only Thread One \ncan access its stack"--> stack1
    r --"Only Thread Two \ncan access its stack"--> stack2

    style l fill:#90EE90, stroke:none
    style stack1 fill:#90EE90
    style r fill:#ADD8E6, stroke:none
    style stack2 fill:#ADD8E6
    style heap fill:none,stroke:black
    style title fill:none,stroke:none
    style stack1m fill:none,stroke:none
    style stack2m fill:none,stroke:none
```

Each thread has its own stack for local variables and method calls. One thread doesn't have access to another thread's stack. Every concurrent thread additionally has access to the process memory, or the heap. This is where objects and their data reside. This shared memory space allows all threads, to read and modify the same objects. When one thread changes an object on the heap, these changes are visible to other threads.

### Time Slicing

It's a technique used in multitasking operating systems, to allow multiple threads or processes to share a single CPU for execution. Available CPU time is sliced into small time intervals, which are divvied out to the threads. Each thread gets that interval, to attempt to make some progress, on the tasks it has to do. Whether it completes its task or not, in that time slice, doesn't matter to the thread management system. When the time is up, it has to yield to another thread, and wait until its turn again.

### [JMM](https://jenkov.com/tutorials/java-concurrency/java-memory-model.html)

The Java Memory Model, is a specification that defines some rules and behaviors for threads, to help control and manage shared access to data, and operations.

- Atomicity of Operations. Few operations are truly atomic.
- Synchronization is the process of controlling threads' access to shared resources.

## Java's Threads

### `java.util.Thread`

```mermaid
classDiagram
    class Runnable {
        void run()
    }

    class Thread {
        private ThreadGroup group
        private String name
        private int priority
        private Runnable target
        private int threadStatus
        private int tid

        Thread()
        Thread(Runnable target)

        void run()
        void start()
    }

    <<Interface>> Runnable
    Thread
    Runnable <|.. Thread
```

#### Priority

Thread priority is a value from 1 to 10. The Thread class has three pre-defined priorities

- `Thread.MIN_PRIORITY = 1` - low
- `Thread.MIN_PRIORITY = 5` - default
- `Thread.MIN_PRIORITY = 10` - high

Higher-priority threads have a better chance of being scheduled, by a thread scheduler, over the lower-priority threads. We can think of the priority as more of a suggestion, to the thread management process.

### Ways to create a thread

- Extend the `Thread` class, and create an instance of this new subclass
- Create a new instance of `Thread`, and pass it any instance that implements the `Runnable` interface
- Use an `Executor`, to create one or more threads

#### Extending the `Thread` class

```mermaid
classDiagram
    class Runnable {
        void run()
    }

    class Thread {
        void run()
        void start()
    }

    class CustomThread {
        void run()
    }
    <<Interface>> Runnable
    Thread
    Runnable <|.. Thread
    Thread <|-- CustomThread
```

Then we can do something like

```java
new CustomThread().start();
```

Advantages of extending a thread are

- We have more control over the thread's behavior and properties
- We can access the thread's methods and fields directly from our sub class
- We can create a new thread for each task

Disadvantages

- We can only extend one class in Java, so our subclass can't extend any other classes
- Our class is tightly coupled to the `Thread` class, which may make it difficult to maintain

#### Implementing `Runnable`

```mermaid
classDiagram
    class Runnable {
        void run()
    }

    class Thread {
        void run()
        void start()
    }

    class CustomRunnable {
        void run()
    }

    note for CustomRunnable "Sub Class / Anonymous Class / Lambda Expression / Method Reference"

    <<Interface>> Runnable
    Thread
    Runnable <|.. Thread
    Runnable <|.. CustomRunnable
```

Then we can do something like

```java
new Thread(CustomRunnable).start();
```

Advantages of implementing a `Runnable`

- We can extend any class and still implement `Runnable`
- Our class (if we create a class) is loosely coupled to the `Thread` class, which makes it easier to maintain
- We can use anonymous classes, lambda expressions, or method references, to very quickly describe thread behavior

Disadvantages

- We do have less control over the thread's behavior and properties

### `run()` and `start()`

There's a big difference between calling `run()` and `start()`.

:::important
If we execute the `run90` method, it's executed _synchronously_, by the running thread it's invoked from. If we want our code to be run _asynchronously_, we must call the `native start()` method.
:::

### States

| Status        | Meaning                                                                                                              |
|---------------|----------------------------------------------------------------------------------------------------------------------|
| NEW           | A thread that has not yet started is in this state                                                                   |
| RUNNABLE      | A thread executing in the Java virtual machine is in this state                                                      |
| BLOCKED       | A thread that is blocked waiting for a monitor lock is in this state                                                 |
| WAITING       | A thread that is waiting indefinitely for another thread to perform a particular action is in this state             |
| TIMED_WAITING | A thread that is waiting for another thread to perform an action for up to a specified waiting time is in this state |
| TERMINATED    | A thread that has exited is in this state                                                                            |

## Links

[An interesting aricle on mutlithreading in Spring](https://www.stefankreidel.io/blog/spring-webmvc-servlet-threading)
