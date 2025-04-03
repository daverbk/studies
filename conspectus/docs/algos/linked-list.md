---
slug: /algos/linked-list
title: Linked List 📋
description: Difference from arrays and sentinel tail and head nodes.
sidebar_position: 2
sidebar_custom_props:
  emoji: 📋
---

# Linked List

## Operations complexity

| Operation                   | Time Complexity (Singly Linked List) | Time Complexity (Doubly Linked List) |
|-----------------------------|--------------------------------------|--------------------------------------|
| Accessing by Index          | O(n)                                 | O(n)                                 |
| Insertion at Beginning      | O(1)                                 | O(1)                                 |
| Insertion at End            | O(n)                                 | O(1)                                 |
| Insertion at Given Position | O(n)                                 | O(n)                                 |
| Deletion at Beginning       | O(1)                                 | O(1)                                 |
| Deletion at End             | O(n)                                 | O(1)                                 |
| Deletion at Given Position  | O(n)                                 | O(n)                                 |
| Searching                   | O(n)                                 | O(n)                                 |

## Advantages and disadvantages compared to arrays

* The main advantage of a linked list is that you can add and remove elements at any position in O(
  1). The caveat is that you need to have a reference to a node at the position in which you want to
  perform the addition/removal, otherwise the operation is O(n), because you will need to iterate
  starting from the head until you get to the desired position. However, this is still much better
  than a normal (dynamic) array, which requires O(n) for adding and removing from an arbitrary
  position

* The main disadvantage of a linked list is that there is no random access. If you have a large
  linked list and want to access the 150,000th element, then there usually isn't a better way than
  to start at the head and iterate 150,000 times. So while an array has O(1) indexing, a linked list
  could require O(n) to access an element at a given position

* While dynamic arrays can be resized, under the hood they still are allocated a fixed size - it's
  just that when this size is exceeded, the array is resized, which is expensive. Linked lists don't
  suffer from this. However, linked lists have more overhead than arrays - every element needs to
  have extra storage for the pointers. If you are only storing small items like booleans or
  characters, then you may be more than doubling the space needed

## Sentinel tail and head nodes in use

```java
class ListNode {

    int val;
    ListNode next;
    ListNode prev;

    ListNode(int val) {
        this.val = val;
    }
}

void addToEnd(ListNode nodeToAdd) {
    nodeToAdd.next = tail;
    nodeToAdd.prev = tail.prev;
    tail.prev.next = nodeToAdd;
    tail.prev = nodeToAdd;
}

void removeFromEnd() {
    if (head.next == tail) {
        return;
    }

    ListNode nodeToRemove = tail.prev;
    nodeToRemove.prev.next = tail;
    tail.prev = nodeToRemove.prev;
}

void addToStart(ListNode nodeToAdd) {
    nodeToAdd.prev = head;
    nodeToAdd.next = head.next;
    head.next.prev = nodeToAdd;
    head.next = nodeToAdd;
}

void removeFromStart() {
    if (head.next == tail) {
        return;
    }

    ListNode nodeToRemove = head.next;
    nodeToRemove.next.prev = head;
    head.next = nodeToRemove.next;
}

ListNode head = new ListNode(-1);
ListNode tail = new ListNode(-1);
head.next =tail;
tail.prev =head;
```

## Reverse a linked list

* Whenever we face a linked list problem, we should break the problem down. List all the things you
  need to accomplish and what we need to do to achieve them. The order in which we think of the
  steps will not necessarily be the same as the order in which the steps should happen

```java
ListNode reverseList(ListNode head) {
    ListNode prev = null;
    ListNode curr = head;
    while (curr != null) {
        ListNode nextNode = curr.next; // first, make sure we don't lose the next node
        curr.next = prev;              // reverse the direction of the pointer
        prev = curr;                   // set the current node to prev for the next node
        curr = nextNode;               // move on
    }

    return prev;
}
```

