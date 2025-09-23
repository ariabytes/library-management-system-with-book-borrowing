# Library Management System (Conceptual)

## Overview

This is a Java-based Library Management System developed for a Data Structures and Algorithms course. The project demonstrates practical usage of abstract data types (ADTs), core data structures, and searching/sorting algorithms within a real-world library application. The system features a graphical user interface (GUI) built with Java Swing, enabling both library patrons and librarians to interact with the system intuitively.

---

## Features

- **Book Management:**  
  Add, remove, search, and sort books by attributes such as title, author, and category.

- **Member Management:**  
  Register and manage library members, including borrowing history.

- **Borrowing & Returning:**  
  Issue and return books, track the availability and status of each book.

- **Reservation Queue:**  
  Maintain a reservation queue for books using custom or Java built-in queue/linked list implementations.

- **Borrow Request Management:**  
  Librarians can issue, return, or cancel borrow requests.

- **Most Borrowed Books:**  
  Track and display the most frequently borrowed books using a heap data structure.

- **Search & Sort:**  
  Built-in and custom implementations of searching (linear, binary search) and sorting (bubble, insertion sort) algorithms.

- **Persistent Storage:**  
  Books, members, and transactions are stored and loaded from plain `.txt` files.

- **Role-Based Access:**
  - **Users:** Search/view books and availability.
  - **Librarians:** Log in to manage books, members, borrow/return flows, and reservations.

---

## Project Structure

```
src/
  model/        # Data classes: Book, Member, BorrowRecord
  service/      # Business logic: LibraryService, AuthService, FileIOService
  util/         # Algorithms, custom ADTs (e.g., queue/linked list)
  gui/          # Java Swing GUI classes
data/           # Flat text files for persistence (books.txt, members.txt, etc.)
docs/           # Diagrams and documentation
tests/          # Unit and integration tests
```

---

## Data Structures & Algorithms

- **Book Lookup:**  
  `HashMap<String, Book>` for fast book searching by ID.

- **Reservation Queues:**  
  Each book has a queue (`LinkedList<Member>` or a custom queue) for managing reservations.

- **Most Borrowed Books:**  
  Max-heap (`PriorityQueue<Book>`) to efficiently retrieve top borrowed books.

- **Sorting:**

  - Built-in: `Collections.sort`
  - Custom: Bubble Sort, Insertion Sort

- **Searching:**
  - Linear Search
  - Binary Search (on sorted lists)

---

## File I/O

- All data is stored in `.txt` files (e.g., `books.txt`, `members.txt`).
- On startup: data is loaded into memory.
- On changes: data is written back to the files.

---

## GUI Overview

- **Main Page:**  
  Search bar, list of books with status, librarian login button.

- **Librarian Panel:**  
  Add/remove books, manage members, handle issue/return/cancel borrow requests, manage reservations, view most borrowed list.

- **Dialogs:**  
  Login dialog for librarians, book details, reservation queue management.

---

## Documentation

- **File Formats:**  
  Example line in `books.txt`:
  ```
  B001,Harry Potter,J.K. Rowling,Fantasy,available,5
  ```
  (Fields: ID, Title, Author, Category, Status, BorrowCount)

---
