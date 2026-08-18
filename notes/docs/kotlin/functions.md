---
slug: /kotlin/functions
title: Functions 🎭 
description: Lambdas, inline functions and extensions
sidebar_position: 2
sidebar_custom_props:
  emoji: 🎭
---

# Lambdas

If lambda is the last argument in a function we can move it out of the presences

```kotlin
list.any { i: Int -> i > 0 }
```

`it` or `this` can denote the argument if it's only one

```kotlin
list.any { it > 0 }
```

the last expression is the result

```kotlin
list.any {
    println("processing $it")
    it > 0 // is the same as return it > 0
}
```

we can destruct arguments

```kotlin
map.mapValues { (key, value) -> "$key -> $value!" }
```

example of storing a lambda in a variable of type `(Int, Int) -> Int`

```kotlin
val sum: (Int, Int) -> Int = { x, y -> x + y }
```

Bound reference stores the object on which the member can delay to called, while unbound can be called on any object of a
given type

`Bound`

```kotlin
class Person(val name: String, val age: Int) {
    fun isOlder(ageLimit: Int) = age > ageLimit

    fun getAgePredicate() = this::isOlder // <- here is a bound reference
}
```

`Unbound`

```kotlin
fun isEven(i: Int): Boolean = i % 2 == 0
::isEven
```

`return` in lambdas

A return inside a `fun` would return from the whole function

```kotlin
fun duplicateNonZero(list: List<Int>): List<Int> {
    return list.flatMap {
        if (it == 0) return listOf()
        listOf(it, it)
    }
}

println(duplicateNonZero(listOf(3, 0, 5))) // would return [] - an empty list 
```

We can specify what lambda to return from so that we do not return from the whole function (`@`)

```kotlin
fun duplicateNonZero(list: List<Int>): List<Int> {
    return list.flatMap {
        if (it == 0) return@flatMap listOf()
        listOf(it, it)
    }
}
```

The labeled return inside a `forEach` actually corresponds to java's `continue`

```kotlin
list.forEach {
    if (it == 0) return@forEach
    println(it)
}
```

is the same as

```kotlin
for (element in list) {
    if (element == 0) continue
    print(element)
}
```

## [Extensions](https://kotlinlang.org/docs/extensions.html)

Kotlin's extensions are basically static functions defined in a separate auxiliary class. We can't call private members
from extensions. As extension functions are static under the hood they cannot be overridden. Properties can be extended.

```kotlin
fun String.lastChar() = this[this.length - 1]
```

Reference of the [coding convention](https://kotlinlang.org/docs/coding-conventions.html#source-file-organization)

... when defining extension functions for a class which are relevant for all clients of this class, put them in the same
file with the class itself. When defining extension functions that make sense only for a specific client, put them next
to the code of that client. Avoid creating files just to hold all extensions of some class.

## [Scoped extensions](https://kotlinlang.org/docs/extensions.html#scope-of-extensions)

A scoped extension in Kotlin is simply an extension function defined within a limited scope

- inside a function
- inside a class

It only exists in that scope, not globally. This lets us write context-specific logic without
polluting the global namespace. It's really useful when building DSLs. The best example would be
`kotlinx.html`.

```kotlin
html {
    body {
        p {
            +"Hello, world!"
        }
    }
}
```

the `body` function on `HTML` looks like 

```kotlin
inline fun HTML.body(classes : String? = null, crossinline block : BODY.() -> Unit = {}) : Unit {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    BODY(attributesMapOf("class", classes), consumer).visit(block)
}
```

## Lambda with receiver

It's easier to understand lambda with receiver concept via comparison to extension function

| regular function   | regular lambda       |
|--------------------|----------------------|
| extension function | lambda with receiver |

Example would be the `with()` function

```kotlin
var sb = StringBuilder()
with(sb) {
    appendLine("Alphabet: ")
    for (c in 'a'..'z') {
        append(c)
    }
}

// Storing a lambda with receiver in a variable
val isOdd: Int.() -> Boolean = { this % 2 == 1 }

// It can later be called as
1.isOdd()
```

### Useful library function that take a lambda with receiver as an argument

```kotlin
inline fun <T, R> with(receiver: T, block: T.() -> R): R = receiver.block()

inline fun <T, R> T.run(block: T.() -> R): R = block()

inline fun <T, R> T.let(block: (T) -> R): R = block(this)

inline fun <T> T.apply(block: T.() -> Unit): T { block(); return this }

inline fun <T> T.also(block: (T) -> Unit): T { block(this); return this }
```

|                         | `this`         | `it`   |
|-------------------------|----------------|--------|
| return result of lambda | `with` / `run` | `let`  |
| return receiver         | `apply`        | `also` |


## Inline functions

Inlining a function means that compiler will substitute a body of the function instead of calling it. No anonymous class
will be created for it. Inlining has some drawbacks though, it might negatively affect applications' size. Only small functions
must be inlined. By default, it's not recommended to define functions as `inline` as HotSpot will still identify frequently used
places and inline them. Inlining is not aiming to reduce the stack calls, but rather to reduce anonymous classes allocation.

### `run`

Runs the block of code (lambda) and returns the last expression as the result.

```kotlin
inline fun <R> run(block: () -> R): R = block()

val name = "Kotlin"
run { println("Hi, $name!") }
```

Will be compiled to the bytecode

```kotlin
val name = "Kotlin"
println("Hi, $name!") // inlined code of lambda body
```

### `let`

Allows to check the argument for being non-null, not only the receiver

```kotlin
fun getEmail(): Email?

val email = getEmail()
if (email != null) sendEmailTo(email)

// can be done simpler with let
email?.let { e -> sendEmailTo(e) }

// or
getEmail()?.let { sendEmailTo(it) }
```

### `takeIf`

Returns the receiver object if it satisfies the given predicate, otherwise returns `null`

```kotlin
issue.takeIf { it.status == FIXED }
person.patronymicName.takeIf(String::isNotEmpty)
```

### `takeUnless`

Returns the receiver object if it does not satisfy the given predicate, otherwise return `null`

```kotlin
person.patronymicName.takeUnless(String?::isNullOrEmpty)
```

### `repeat`

Repeats an action for a given number of times

```kotlin
repeat(10) {
    println("Welcome!")
}
```

### Resource management with `use`

`java`

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

static String readFirstLineFormFile(String path) throws IOException {
  try (BufferedReader br = new BufferedReader(new FileReader(path))) {
    return br.readLine();
  }
}
``` 

`kotlin`

```kotlin
import java.io.BufferedReader
import java.io.FileReader

fun readFirstLineFromFile(path: String): String {
    BufferedReader(FileReader(path)).use { br ->
        return br.readLine()
    }
}
```
