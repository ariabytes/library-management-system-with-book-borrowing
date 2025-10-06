package service_implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import model.Book;
import utils.MaxHeap_Imp;
import utils.Queue_Imp;

/**
 * BookService - central book manager used by Test_Main.
 *
 * Responsibilities:
 *  - maintain in-memory list of books
 *  - basic search/sort utilities used by Test_Main
 *  - reservation add/cancel (works with Queue_Imp)
 *  - getMostBorrowedBooks(int n) using MaxHeap_Imp to pick top items
 *
 * Note: borrow counts are read from Book.getBorrowCount().
 * Make sure your BorrowService increments Book.incrementBorrowCount() when a borrow succeeds.
 */
public class BookService {

    private final List<Book> books;

    public BookService() {
        this.books = new ArrayList<>();
    }

    // --- basic accessors used by Test_Main ---
    public List<Book> getAllBooks() {
        return books;
    }

    public boolean addBook(Book book) {
        if (searchBookById(book.getId()) != null) return false; // already exists
        books.add(book);
        return true;
    }

    public boolean removeBook(String bookId) {
        Iterator<Book> it = books.iterator();
        while (it.hasNext()) {
            Book b = it.next();
            if (b.getId().equalsIgnoreCase(bookId)) {
                it.remove();
                return true;
            }
        }
        return false;
    }
 // Add this method to BookService.java class

    /**
     * Sorts the books list alphabetically by title (A-Z)
     * This modifies the internal list permanently
     */
    public void sortBooksAlphabetically() {
        books.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        System.out.println("[BookService] Books sorted alphabetically by title");
    }
    
 // In BookService.java
    public BookService(List<Book> booksList) {
        this.books = new ArrayList<>();
        if (booksList != null) {
            this.books.addAll(booksList);
        }
    }

    public Book searchBookById(String bookId) {
        for (Book b : books) {
            if (b.getId().equalsIgnoreCase(bookId)) return b;
        }
        return null;
    }

    public List<Book> searchBooksByTitle(String title) {
        List<Book> result = new ArrayList<>();
        if (title == null) return result;
        String key = title.toLowerCase();
        for (Book b : books) {
            if (b.getTitle() != null && b.getTitle().toLowerCase().contains(key)) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> searchBooksByAuthor(String author) {
        List<Book> result = new ArrayList<>();
        if (author == null) return result;
        String key = author.toLowerCase();
        for (Book b : books) {
            if (b.getAuthor() != null && b.getAuthor().toLowerCase().contains(key)) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> searchBooksByCategory(String category) {
        List<Book> result = new ArrayList<>();
        if (category == null) return result;
        String key = category.toLowerCase();
        for (Book b : books) {
            if (b.getCategory() != null && b.getCategory().toLowerCase().contains(key)) {
                result.add(b);
            }
        }
        return result;
    }

    public List<Book> sortBooksByTitle(boolean ascending) {
        List<Book> copy = new ArrayList<>(books);
        copy.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        if (!ascending) Collections.reverse(copy);
        return copy;
    }

    public List<Book> sortBooksByAuthor(boolean ascending) {
        List<Book> copy = new ArrayList<>(books);
        copy.sort((a, b) -> a.getAuthor().compareToIgnoreCase(b.getAuthor()));
        if (!ascending) Collections.reverse(copy);
        return copy;
    }

    // --- Most borrowed logic using MaxHeap_Imp ---
    // Returns up to n most-borrowed books (descending by borrowCount).
    // Uses MaxHeap_Imp to pick the current max repeatedly (rebuild heap each iteration).
    public List<Book> getMostBorrowedBooks(int n) {
        List<Book> result = new ArrayList<>();
        if (books.isEmpty() || n <= 0) return result;

        // Build wrapper list (book + count) which implements Comparable
        List<BookCount> counts = new ArrayList<>();
        for (Book b : books) {
            counts.add(new BookCount(b, b.getBorrowCount()));
        }

        MaxHeap_Imp<BookCount> heap = new MaxHeap_Imp<>();

        int limit = Math.min(n, counts.size());
        for (int i = 0; i < limit; i++) {
            // rebuild heap from current counts (some entries may have been marked used)
            heap.buildHeap(counts);
            BookCount top = heap.getMax();
            if (top == null) break;
            // If top is already marked (used), stop
            if (top.count == Integer.MIN_VALUE) break;
            result.add(top.book);
            // mark it used so it won't be picked again
            top.count = Integer.MIN_VALUE;
        }

        return result;
    }

    // --- Reservations ---
    // returns true if reservation added; false if book not found or member already in queue
    public boolean addReservation(String bookId, String memberId, String memberName) {
        Book b = searchBookById(bookId);
        if (b == null) return false;

        Queue_Imp<String> ids = b.getReservationQueue();
        Queue_Imp<String> names = b.getReservationQueueNames();

        // check duplicate
        int size = ids.size();
        for (int i = 0; i < size; i++) {
            String idAt = ids.get(i);
            if (idAt != null && idAt.equals(memberId)) return false; // already reserved
        }

        b.addToReservationQueue(memberId, memberName);
        return true;
    }

    // Cancel reservation (remove memberId from the book's reservation queue)
    // returns true if removed, false if not found or book not found
    public boolean cancelReservation(String bookId, String memberId) {
        Book b = searchBookById(bookId);
        if (b == null) return false;

        Queue_Imp<String> ids = b.getReservationQueue();
        Queue_Imp<String> names = b.getReservationQueueNames();

        // temporary queues to rebuild excluding the target memberId
        Queue_Imp<String> tempIds = new Queue_Imp<>();
        Queue_Imp<String> tempNames = new Queue_Imp<>();

        boolean removed = false;

        while (!ids.isEmpty()) {
            String id = ids.dequeue();
            String name = names.dequeue();
            if (id != null && id.equals(memberId)) {
                removed = true;
                // skip adding this pair to temp -> effectively removing it
            } else {
                tempIds.enqueue(id);
                tempNames.enqueue(name);
            }
        }

        // restore remaining reservations back to original queues
        while (!tempIds.isEmpty()) {
            ids.enqueue(tempIds.dequeue());
            names.enqueue(tempNames.dequeue());
        }

        return removed;
    }
    
    public List<Object[]> getAllReservations() {
        List<Object[]> allReservations = new ArrayList<>();

        for (Book b : books) {
            Queue_Imp<String> ids = b.getReservationQueue();
            Queue_Imp<String> names = b.getReservationQueueNames();
            int size = ids.size();

            for (int i = 0; i < size; i++) {
                String memberId = ids.get(i);
                String memberName = names.get(i);
                if (memberId != null && memberName != null) {
                    allReservations.add(new Object[]{b.getId(), memberId, memberName});
                }
            }
        }

        return allReservations;
    }

    // --- helper wrapper used by heap ---
    private static class BookCount implements Comparable<BookCount> {
        final Book book;
        int count;

        BookCount(Book book, int count) {
            this.book = book;
            this.count = count;
        }

        // natural ordering: compare by count (smaller -> less). MaxHeap_Imp expects compareTo > 0
        // when this is greater than other
        @Override
        public int compareTo(BookCount o) {
            return Integer.compare(this.count, o.count);
        }
    }
}
