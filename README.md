# Concurrent Data Structures using locks and RMW instructions

This repo contains the implementations of `Linked List`, `Stack` and `Binary Search Tree`. The implementation details
are noted in [Programming Assignment 2](analysis/Programming%20Assignment%202.docx) document.

## Requirements
1. java@1.8.0_341
2. maven@3.9.4

## Steps to compile and run the project

This code works by generating an executable jar file of the project.

It uses maven for managing the build and packages and hence is a requirement for compiling and running the application smoothly.

1. `cd` into the project's root directory
2. Execute the `launcher.sh` script to run the project for the algorithm of your choice
```
bash launcher.sh <project_path> <number of runs each> <data structure Id> <number of threads> <write distribution> <key space size>
```

To run all the experiments execute the `run_exp.sh` script as following, this would execute the experiments for the chosen data structures in the thread count range of [2, 12].
```
bash run_exp.sh <project_path> <number of runs each> <data structure Id> <write distribution> <key space size>
```

**NOTE:** write distribution is to be passed in percentage. For example, if you want 10% writes, pass 10.

**Example:**
```
bash launcher.sh "./ConcurrentDataStructures" 10 1 10 100 10000
```

**Data Structure ID:**
1. Fine-grained Linked List
2. Stack
3. Fine-grained Binary Search Tree
