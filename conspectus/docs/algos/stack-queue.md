---
slug: /algos/stack-queue
title: Stack & Queue 🧑‍🤝‍🧑
description: FIFO & LIFO, monotonic stacks & queues.
sidebar_position: 5
sidebar_custom_props:
  emoji: 🃏
---

# Stack & Queue

## Operations complexity

### Stack

| Operation | Complexity |
|-----------|------------|
| `push()`  | `O(1)`     |
| `pop()`   | `O(1)`     |
| `top()`   | `O(1)`     |

### Queue

| Operation   | Complexity |
|-------------|------------|
| `enqueue()` | `O(1)`     |
| `dequeue()` | `O(1)`     |
| `peek()`    | `O(1)`     |

## Stack

The characteristic that makes something a "stack" is that we can only add and remove elements from
the same end. It doesn't matter how we implement it, a "stack" is just an abstract interface.

For algorithm problems, a stack is a good option whenever we can recognize the LIFO pattern.
Usually, there will be some component of the problem that involves elements in the input interacting
with each other

Example problem:

```java
class Solution {

    public String removeDuplicates(String s) {
        StringBuilder stack = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (!stack.isEmpty() && stack.charAt(stack.length() - 1) == c)
                stack.deleteCharAt(stack.length() - 1);
            else
                stack.append(c);
        }

        return stack.toString();
    }
}
```

## Queue

For algorithm problems, queues are less common than stacks, and the problems are general.

Example problem `933. Number of Recent Calls`:

```java
class RecentCounter {

    Queue<Integer> queue;

    public RecentCounter() {
        queue = new LinkedList<>();
    }

    public int ping(int t) {
        while (!queue.isEmpty() && queue.peek() < t - 3000) {
            queue.poll();
        }

        queue.offer(t);
        return queue.size();
    }
}
```

## Monotonic

Monotonic stacks and queues are useful in problems that, for each element, involves finding the "
next" element based on some criteria, for example, the next greater element. They're also good when
you have a dynamic window of elements and you want to maintain knowledge of the maximum or minimum
element as the window changes. In more advanced problems, sometimes a monotonic stack or queue is
only one part of the algorithm.

Pseudocode:

```
stack = []
for num in nums:
    while stack.length > 0 AND stack.top >= num:
        stack.pop()
    // Some logic depending on the problem
    stack.push(num)
```

Example problem `739. Daily Temperatures`:

```java
class Solution {

    public int[] dailyTemperatures(int[] temperatures) {
        Stack<Integer> stack = new Stack<>();
        int[] answer = new int[temperatures.length];

        for (int i = 0; i < temperatures.length; i++) {
            while (!stack.empty() && temperatures[stack.peek()] < temperatures[i]) {
                int j = stack.pop();
                answer[j] = i - j;
            }

            stack.push(i);
        }

        return answer;
    }
}
```

## Fast and slow pointers

```
// head is the head node of a linked list
function fn(head):
    slow = head
    fast = head

    while fast and fast.next:
        Do something here
        slow = slow.next
        fast = fast.next.next
```
