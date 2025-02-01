# BookIndex

## Description

This program implements a book index in Java. It allows adding terms with page numbers, removing terms, updating terms, removing specific pages, searching terms by prefix, saving the index to a file, and loading the index from a file.

## Features

- Add a term with its page number.
- Remove a term from the index.
- Update an existing term.
- Remove a specific page from the index.
- Search for terms that start with a specific prefix.
- Save the index to a text file.
- Load the index from a text file.
- Display the current index.

## Usage

1. Compile the program using a Java compiler:
   ```sh
   javac BookIndex.java
   ```

2. Run the program
    ```sh
    java BookIndex
    ```
3. Follow the menu instructions to use the different functionalities of the program.

## Time Complexity

### 1. addTerm(String term, int page)
- Complexity: O(n), where n is the number of entries in the index. In the worst case, we need to traverse all entries to find the term.

### 2. removeTerm(String term)
- Complexity: O(n), where n is the number of entries in the index. In the worst case, we need to traverse all entries to remove the term.

### 3. updateTerm(String oldTerm, String newTerm)
- Complexity: O(n), where n is the number of entries in the index. In the worst case, we need to traverse all entries to find and update the term.

### 4. removePage(int page)
- Complexity: O(n * log m), where n is the number of entries in the index and m is the number of pages in an entry. We need to traverse all entries and, for each entry, remove the page from a TreeSet, which has logarithmic complexity.

### 5. searchByPrefix(String prefix)
- Complexity: O(n * k), where n is the number of entries in the index and k is the length of the prefix. In the worst case, we need to traverse all entries and check if the term starts with the prefix.

### 6. readFromFile(String filename)
- Complexity: O(m * n), where m is the number of lines in the file and n is the number of pages in a line. We need to read each line of the file and add each page to the index.

### 7. saveToFile(String filename)
- Complexity: O(n), where n is the number of entries in the index. We need to write each entry to the file

### 8. printIndex()
- Complexity: O(n), where n is the number of entries in the index. We need to print each entry

## Program Time Complexity
The overall complexity of the program depends on the usage of different functionalities. In the worst case, if all functionalities are used extensively, the complexity can be dominated by file read/write operations, which are O(m * n), and by search and update operations, which are O(n).