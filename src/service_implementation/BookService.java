package service_implementation;

import service_interface.BookServiceInterface;
import model.Book;

import java.util.*;
import java.util.stream.Collectors;

public class BookService implements BookServiceInterface {
    private List<Book> books;

    public BookService() {
        this.books = new ArrayList<>();
    }

    @Override
    public boolean addBook(Book book) {
        if (searchBookById(book.getId()) == null) {
            books.add(book);
            return true;
        }
        return false; // book already exists
    }

    @Override
    public boolean removeBook(String bookId) {
        Book book = searchBookById(bookId);
        if (book != null) {
            books.remove(book);
            return true;
        }
        return false;
    }
// TO DO Hash Maps
    @Override
    public Book searchBookById(String bookId) {
        return books.stream()
                .filter(b -> b.getId().equalsIgnoreCase(bookId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        return books.stream()
                .filter(b -> b.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> searchBooksByCategory(String category) {
        return books.stream()
                .filter(b -> b.getCategory().toLowerCase().contains(category.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> sortBooksByTitle(boolean ascending) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getTitle,
                        ascending ? Comparator.naturalOrder() : Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> sortBooksByAuthor(boolean ascending) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getAuthor,
                        ascending ? Comparator.naturalOrder() : Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> sortBooksByCategory(boolean ascending) {
        return books.stream()
                .sorted(Comparator.comparing(Book::getCategory,
                        ascending ? Comparator.naturalOrder() : Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> sortBooksByPopularity(boolean ascending) {
        return books.stream()
                .sorted(Comparator.comparingInt(Book::getBorrowCount)
                        .thenComparing(Book::getTitle))
                .collect(Collectors.toList());
    }
// TODO Make max heap adt
    @Override
    public List<Book> getMostBorrowedBooks(int n) {
        PriorityQueue<Book> maxHeap = new PriorityQueue<>(
                (b1, b2) -> Integer.compare(b2.getBorrowCount(), b1.getBorrowCount())
        );
        maxHeap.addAll(books);

        List<Book> result = new ArrayList<>();
        for (int i = 0; i < n && !maxHeap.isEmpty(); i++) {
            result.add(maxHeap.poll());
        }
        return result;
    }

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books);
    }

    @Override
    public void listAllBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available in the library.");
        } else {
            books.forEach(System.out::println);
        }
    }
}
