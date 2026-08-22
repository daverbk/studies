---
slug: /algos/trees
title: Trees 🌳
description: Binary tree & binary search tree, DFS & BFS.
sidebar_position: 6
sidebar_custom_props:
  emoji: 🌳
---

# Trees

## Operations complexity

### Binary Trees

| Operation       | Complexity |
| --------------- | ---------- |
| `dfs traversal` | `O(n)`     |
| `bfs traversal` | `O(n)`     |

# Terminology

The **root** node is the node at the "top" of the tree, the **root** is the only node that has no parent. In a tree, a node cannot have more than one parent. In a binary tree, all nodes have a maximum of two **children**. These children are referred to as the left child and the right child.

If we have a node `A` with an edge to a node `B`, so `A` -> `B`, we call `A` the parent of node `B`, and node `B` a child of node `A`.

If a node has no children, it is called a **leaf** node. The **leaf** nodes are the **leaves** of the tree.

The depth of a node is how far it is from the root node. The root has a depth of 0. Every child has a depth of `parentsDepth + 1`.

A **subtree** of a tree is a node and all its descendants.

```mermaid
graph TB
    A((5))-->B((2))
    A-->C((3))
    B-->D((4))
    B-->E((9))
    C-->F((6))
    C-->G((7))
```

## In code

```java
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode (int val) {
        this.val = val;
    }
}
```

## Depth-first search

You can think of the paths of a binary tree as branches growing from the root. DFS chooses a branch and goes as far down as possible. Once it fully explores the branch, it backtracks until it finds another unexplored branch.

Typical dfs goes like this:

1. Handle the base case(s). Usually, an empty tree (node = null) is a base case.
2. Do some logic for the current node
3. Recursively call on the current node's children
4. Return the answer

Each function call solves and returns the answer to the original problem as if the subtree rooted at the current node was the input.

## The types of dfs

1. Preorder traversal

In **preorder** traversal, logic is done on the current node before moving to the children. In that case, at any given node, we would print the current node's value, then recursively call the left child, then recursively call the right child.

```java
public void preorderDfs(Node node) {
    if (node == null) {
        return;
    }

    System.out.println(node.val); // the logic
    preorderDfs(node.left);
    preorderDfs(node.right);
    return;
}
```

2.Inorder traversal

For **inorder** traversal, we first recursively call the left child, then perform logic (print in this case) on the current node, and then recursively call the right child.

```java
public void inorderDfs(Node node) {
    if (node == null) {
        return;
    }

    inorderDfs(node.left);
    System.out.println(node.val);
    inorderDfs(node.right);
    return;
}
```

3. Postorder traversal

In **postorder** traversal, we recursively call on the children first and then perform logic on the current node. This means no logic will be done until we reach a leaf node since calling on the children takes priority over performing logic. In a **postorder** traversal, the root is the last node where logic is done.

```java
public void postorderDfs(Node node) {
    if (node == null) {
        return;
    }

    postorderDfs(node.left);
    postorderDfs(node.right);
    System.out.println(node.val);
    return;
}
```

## Iterative implementation

```java
class Solution {
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return;
        }

        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.empty()) {
            TreeNode node = stack.pop();
            System.out.println(node.val)

            if (node.left != null) {
                stack.push(node.left);
            }
            if (node.right != null) {
                stack.push(node.right);
            }
        }

        return;
    }
}
```

## Breadth-first search

```
