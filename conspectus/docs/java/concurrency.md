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
- Each thread's got what's called a thread stack. This is memory, that only a single thread, will have access to.

Every Java application runs as a single process, and each process can then have multiple threads within it. Every process has a heap, and every thread has a thread stack.

[An interesting aricle on mutlithreading in spring](https://www.stefankreidel.io/blog/spring-webmvc-servlet-threading)
