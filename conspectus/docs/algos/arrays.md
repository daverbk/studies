---
slug: /algos/arrays
title: Arrays 🫥 
description: Arrays algorithms, their operations complexity.
sidebar_position: 1
sidebar_custom_props:
  emoji: 🫥
---

# Arrays

## Operations complexity

| Operation                  | ArrayList | String |
|----------------------------|-----------|--------|
| Appending to end           | *O(1)     | O(n)   |
| Popping from end           | O(1)      | O(n)   |
| Insertion, not from end    | O(n)      | O(n)   |
| Deletion, not from end     | O(n)      | O(n)   |
| Modifying element          | O(1)      | O(n)   |
| Random access              | O(1)      | O(1)   |
| Checking if element exists | O(n)      | O(n)   |

`*O(1)` amortized time complexity for appending to the end of an ArrayList is caused by the need to resize the array
when it's full

## Sub-arrays, subsequences, and subsets

### Sub-arrays

Sub-array or sub-string is a contiguous section of an array or string.

The size of a sub-array between `i` and `j` (inclusive) is `j - i + 1`. This is also the number of sub-arrays that end
at `j`, starting from `i` or later.

#### Advice

1. If a problem has explicit constraints such as:

* Sum greater than or less than `k`
* Limits on what is contained, such as the maximum of `k` unique elements or no duplicates allowed
* And/or asks for:
    * Minimum or maximum length
    * Number of sub-arrays/sub-strings
    * Max or minimum sum

Think about a sliding window.

2. If a problem's input is an integer array, and you find yourself needing to calculate multiple sub-array sums, consider
   building a prefix sum.

### Subsequences

Subsequence is a set of elements of an array/string that keeps the same relative order but doesn't need to be
contiguous.

For example, subsequences of `[1, 2, 3, 4]` include: `[1, 3]`, `[4]`, `[]`, `[2, 3]`, but not `[3, 2]`, `[5]`, `[4, 1]`.

#### Advice

Dynamic programming is used to solve a lot of subsequence problems.

One more associated with subsequences is two pointers when two input arrays/strings are given. Because prefix sums and
sliding windows represent sub-arrays/sub-strings, they are not applicable here.

### Subsets

A subset is any set of elements from the original array or string. The order doesn't matter and neither do the elements
being beside each other. For example, given `[1, 2, 3, 4]`, all of these are subsets: `[3, 2]`, `[4, 1, 2]`, `[1]`.
Subsets that contain the same elements are considered the same, so `[1, 2, 4]` is the same subset as `[4, 1, 2]`.

#### Advice

Subsets are being used with backtracking.

One thing to note is that if a problem involves subsequences, but the order of the subsequence doesn't actually matter,
then we can treat it the same as a subset.

## Prefix sum

Building a prefix sum is a form of pre-processing. Pre-processing is a useful strategy in a variety of problems where we
store pre-computed data in a data structure before running the main logic of our algorithm. While it takes some time to
pre-process, it's an investment that will save us a huge amount of time during the main parts of the algorithm.

A prefix sum is a great tool whenever a problem involves sums of a subarray. It only costs `O(n)`to build but allows all
future subarray queries to be `O(1)`.

```
prefix = [nums[0]]
for (int i = 1; i < nums.length; i++)
    prefix.append(nums[i] + prefix[prefix.length - 1])
```

## Two pointers

1. Start one pointer at the first index 0 and the other pointer at the last index input.length - 1.
2. Use a while loop until the pointers are equal to each other.
3. At each iteration of the loop, move the pointers towards each other. This means either increment the pointer that
   started at the first index, decrement the pointer that started at the last index, or both. Deciding which pointers to
   move will depend on the problem we are trying to solve.

```
function fn(arr):
    left = 0
    right = arr.length - 1

    while left < right:
        Do some logic here depending on the problem
        Do some more logic here to decide on one of the following:
            1. left++
            2. right--
            3. Both left++ and right--
```

### Usage for multiple input arrays

1. Create two pointers, one for each iterable. Each pointer should start at the first index
2. Use a while loop until one of the pointers reaches the end of its iterable
3. At each iteration of the loop, move the pointers forward. This means incrementing either one of the pointers or both
   of the pointers. Deciding which pointers to move will depend on the problem we are trying to solve
4. Because our while loop will stop when one of the pointers reaches the end, the other pointer will not be at the end
   of its respective iterable when the loop finishes. Sometimes, we need to iterate through all elements - if this is
   the case, you will need to write extra code here to make sure both iterables are exhausted

```
function fn(arr1, arr2):
    i = j = 0
    while i < arr1.length AND j < arr2.length:
        Do some logic here depending on the problem
        Do some more logic here to decide on one of the following:
            1. i++
            2. j++
            3. Both i++ and j++

    // Step 4: make sure both iterables are exhausted
    // Note that only one of these loops would run
    while i < arr1.length:
        Do some logic here depending on the problem
        i++

    while j < arr2.length:
        Do some logic here depending on the problem
        j++
```


## Sliding window

### When should we use sliding window?

1. the problem will either explicitly or implicitly define criteria that make a subarray "valid". There are 2 components
   regarding what makes a subarray valid:
    * A constraint metric. This is some attribute of a subarray. It could be the sum, the number of unique elements, the
      frequency of a specific element, or any other attribute
    * A numeric restriction on the constraint metric. This is what the constraint metric should be for a subarray to be
      considered valid
2. the problem will ask you to find valid sub-arrays in some way
    * The most common task you will see is finding the best valid subarray. The problem will define what makes a subarray
      better than another. For example, a problem might ask you to find the longest valid subarray
    * Another common task is finding the number of valid sub-arrays

Example problems:
* Find the longest subarray with a sum less than or equal to `k`
* Find the longest substring that has at most one `"0"`
* Find the number of sub-arrays that have a product less than `k`


### General usage

1. Initially, we have left = right = 0, which means that the first subarray we look at is just the first element of the
   array on its own
2. We want to expand the size of our "window", and we do that by incrementing right. When we increment right, this is
   like "adding" a new element to our window
3. To "remove" elements, we can increment left, which shrinks our window
4. As we add and remove elements, we are "sliding" our window along the input from left to right. it always slides along
   to the right, until we reach the end of the input

```
function fn(arr):
left = 0
for (int right = 0; right < arr.length; right++):
Do some logic to "add" element at arr[right] to window

        while WINDOW_IS_INVALID:
            Do some logic to "remove" element at arr[left] from window
            left++

        Do some logic to update the answer

```

### Usage for fixed window size

1. Start by building the first window (from index `0` to `k - 1`)
2. Once we have a window of size `k`, if we add an element at index `i`, we need to remove the element at index `i - k`

```
function fn(arr, k):
    curr = some data to track the window

    // build the first window
    for (int i = 0; i < k; i++)
        Do something with curr or other variables to build first window

    ans = answer variable, probably equal to curr here depending on the problem
    for (int i = k; i < arr.length; i++)
        Add arr[i] to window
        Remove arr[i - k] from window
        Update ans

    return ans
```
