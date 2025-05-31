---
slug: /kotlin/oop
title: OOP 🏛️ 
description: Classes, objects and what's inside. 
sidebar_position: 1
sidebar_custom_props:
  emoji: 🏛️
---

# `class` / `object`

## Modifiers

We can declare constants with `const` modifier, and it's going to be substituted with the value on the JVM level. It
works only for primitive types.

| modifier   | explanation                                                 |
|------------|-------------------------------------------------------------|
| `final`    | cannot be overridden (is sed by default)                    |
| `open`     | can be overridden                                           |
| `abstract` | must be overridden (can't have implementation)              |
| `override` | overrides a member in a superclass or interface (mandatory) |

| modifier    | class member                 | top-level declaration |
|-------------|------------------------------|-----------------------|
| `public`    | visible everywhere           | visible everywhere    |
| `internal`  | visible in the module        | visible in the module |
| `protected` | visible in subclasses (only) | –                     |
| `private`   | visible in the class         | visible in the file   |

| kotlin modifier | jvm level                     |
|-----------------|-------------------------------|
| `public`        | `public`                      |
| `protected`     | `protected`                   |
| `private`       | `private` / `package private` |
| `internal`      | `public` & name ruining       |

### `enum` class

In kotlin `enum` is a modifier for classes to create enumerations

### `data` class

Generates `equals`, `hashCode`, `copy`, `toString`

#### `equals` and reference equality

In kotlin `==` calls `equals`. There is also `===` operator which checks reference equality. Bare `class` `eqauls` uses
reference equality check.

```kotlin
val set1 = setOf(1, 2, 3)
val set2 = setOf(1, 2, 3)

set1 == set2 // true
set1 === set2 // false
```

We can still exclude props from the basic methods in a `data class`by moving them outside the primary constructor.

```kotlin
data class User(val email: String) {
    val nickname: String? = null
}
```

### `sealed` class

Restricts class hierarchy, all subclasses must be located in the sames file.

### Class delegation with `by`

We can delegate methods from one class another. We don't need to write boilerplate code for it.

```kotlin
interface Printer {
    fun printMessage(message: String)
}

class ConsolePrinter : Printer {
    override fun printMessage(message: String) {
        pirntln(message)
    }
}

class PrinterManager(printer: Printer) : Printer by printer

fun main() {
    val consolePrinter = ConsolePrinter()
    val manager = PrinterManager(consolePrinter)

    manager.printMessage("Hi there!") // <-- we are calling a method, the code of which 
    // we did not write in PrinterManager explicitly
}
```

## Package structure

In kotlin we can put multiple classes inside one file, this file can also contain top-level statements

## Inheritance

```kotlin
interface Base
class BaseImpl : Base
```

```kotlin
open class Parent
class Child : Parent() // <-- () is the constructor call, so we can pass parameters there if any
```

## `object`

`object` = singleton

`java`

```java
public class JSingleton {
    public final static JSingleton INSTANCE = new JSingleton();

    private JSignleton() {
    }

    private foo() {
    }
}
```

`kotlin`

```kotlin
object KSingleton {
    fun foo() {}
}
```

### `companion object`

In kotlin there are no `static` methods and `companion object`s might be a replacement for that.

```kotlin
class A {
    companion object {
        fun foo() = 1
    }
}

fun main(args: Array<String>) {
    A.foo()
}
```

Companion objects can implement interfaces and be receiver of extension function.

```kotlin
interface Factory<T> {
    fun create(): T
}

class A {
    private constructor()

    companion object : Factory<A> {
        override fun create(): A {
            return A()
        }
    }
}

fun <T> createNewInstance(factory: Factory<T>) { /* some code */
}

createNewInstance(A)
A.create()
```

```kotlin
class Person(val firstName: String, val lastName: String) {
    companion object {}
}

fun Person.Companion.fromJson(json: String): Person {
    // ... 
}

val p = Person.fromJson(json)
```

Not all objects are singletons, object expressions are the java's anonymous class alternative. They are used for the
cases when we have to override multiple methods, otherwise we could just use lambdas.

```kotlin
widnow.addMouseListener() {
    object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            // ..
        }

        override fun mouseEntered(e: MouseEvent) {
            // ..
        }
    }
}
```

## Functions

There are no `static` members in kotlin. The closest thing to that would be:

- top-level statements
- `object`s' members
- `companion object`s' members

Calling a top-level function from Java:

```kotlin
package intro

fun foo() = 0
```

```java
packge other;

import intro.MyFileKt;

public class UsingFoo {
    public static void main(String[] args) {
        MyFileKt.foo();
    }
}
```

We can use the `@JvmName` to change the name of the package to import.

```kotlin
@file:JvmName("Util")

package intro

fun foo() = 0
```

```java
packge other;

import static intro.Util;

public class JavaUsage {
    public static void main(String[] args) {
        into i = Util.foo();
    }
}
```

### Constructors

```kotlin
class A

val a = A() // <-- calling a default constructor 
```

Full primary constructor syntax looks like this. `val` or `var` in the constructor would automatically create a
property. Constructor's visibility can be changed.

```kotlin
class Person(name: String) { // <-- name is a constructor parameter
    val name: String

    init {
        this.name = name
    }
}
```

We can declare secondary constructors and must use the primary constructor.

```kotlin
class Rectangle(val height: Int, val width: Int) {
    constructor(side: Int) : this(side, side) { /* some logic */
    }
}
```

## Properties

`Kotlin`

```kotlin
contact.address
contact.address = "..."
```

`Java`

```java
contact.getAddress();
contact.

setAdderss("...");
```

`property` = `accessor(s)`

`val` = `getter`

`var` = `getter` + `setter`

If the `field` is not mentioned in custom accessor then no backing field is generated. All properties are `open`.

### Lazy and late initialization

Lazy props' values is calculated on the first access.

```kotlin
val lazyValue: String by lazy {
    println("completed!")
    "Hello"
}
```

`lateinit` is useful when we want to initialize the values not in the constructor and not to use nullable accessors
everywhere. If the property was not initialize a runtime `UninitializedPropertyAccessException` is thrown. `lateinit`
can't be `val`, can't be nullable or of a primitive type.

```kotlin
class KotlinActivity : Activity() {
    lateinit var myData: MyData

    override fun onCreate(savedInstanceState: Budnle?) {
        super.onCreate(savedInstanceState)

        myData = intent.getParcelableExtra("MY_DATA")
    }
}
```

```kotlin
myData.foo // we can call props of myData with no safe accessors
```
