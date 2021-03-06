# aoc 21 :christmas_tree: :santa:

## Advent of Code repository for year '21 in Java 17

### Day 1

[aoc21_1.java](src/main/java/ch/aoc21/aoc21_1.java)

Hints:

- calculate differences while loading or averaging values

### Day 2

[aoc21_2.java](src/main/java/ch/aoc21/aoc21_2.java)

### Day 3

[aoc21_3.java](src/main/java/ch/aoc21/aoc21_3.java)

Hints:

- use recursion

### Day 4

[aoc21_4.java](src/main/java/ch/aoc21/aoc21_4.java)

Hints:

- create class for a bingo board with functions:
    - add new value at position
    - check if drawn number exists on board
    - check if any row or column is completely checked

### Day 5

[aoc21_5.java](src/main/java/ch/aoc21/aoc21_5.java)

Hints:

- loop over point between start and end points, not over area. would be super slow.

### Day 6

[aoc21_6.java](src/main/java/ch/aoc21/aoc21_6.java)

Hints:

- count fishes at stages and don't track the fish as an object. critical memory.

### Day 7

[aoc21_7.java](src/main/java/ch/aoc21/aoc21_7.java)

Hints:

- can be solved with brute force or optimization problem

### Day 8

[aoc21_8.java](src/main/java/ch/aoc21/aoc21_8.java)

Hints:

- See a combination of chars as a set and use set operations (intersect, difference)

### Day 9

[aoc21_9.java](src/main/java/ch/aoc21/aoc21_9.java)

Hints:

- Again, the holy power of recursion :zap:

### Day 10

[aoc21_10.java](src/main/java/ch/aoc21/aoc21_10.java)

Hints:

- Use a stack for opening brackets

### Day 11

[aoc21_11.java](src/main/java/ch/aoc21/aoc21_11.java)

Hints:

- check if you have already visited a flashing octopus :octopus:

### Day 12

[aoc21_12.java](src/main/java/ch/aoc21/aoc21_12.java)

Hints:

- check [DFS](https://en.wikipedia.org/wiki/Depth-first_search)

### Day 13

[aoc21_13.java](src/main/java/ch/aoc21/aoc21_13.java)

Hints:

- think about what a fold means to an array and how you can optimize this.

### Day 14

[aoc21_14.java](src/main/java/ch/aoc21/aoc21_14.java)

Hints:

- similar to day 6

### Day 15

[aoc21_15.java](src/main/java/ch/aoc21/aoc21_15.java)

Hints:

- [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)

### Day 16

[aoc21_16.java](src/main/java/ch/aoc21/aoc21_16.java)

### Day 17

[aoc21_17.java](src/main/java/ch/aoc21/aoc21_17.java)

Hints:

```math
y[n] = n\cdot v_y[0] + \frac{n\cdot(n-1)}{2}
x[n] = \begin{cases} n\cdot v_y[0] + \frac{n\cdot(n-1)}{2} & n<v_x[0] \\
v_y^2[0]-\frac{v_x[0]\cdot(v_x[0]-1)}{2} & n\geq v_x[0] \end{cases}
```

### Day 18

[aoc21_18.java](src/main/java/ch/aoc21/aoc21_18.java)

### Day 19

[aoc21_19.java](src/main/java/ch/aoc21/aoc21_19.java)

Hints:

- use absolute vector distance to check whether a set overlaps or not

### Day 20

[aoc21_20.java](src/main/java/ch/aoc21/aoc21_20.java)

Hints:

- think about what happens with the infinite pixel with your enhancing algorithm.