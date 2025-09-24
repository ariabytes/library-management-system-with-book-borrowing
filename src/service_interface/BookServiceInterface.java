package service_interface;
import java.util.*;
import model.Book;

public interface BookServiceInterface {

    boolean addBook(Book book);
    boolean removeBook(String bookId);

    Book searchBookById(String bookId); 
    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooksByAuthor(String author);
    List<Book> searchBooksByCategory(String category);

    List<Book> sortBooksByTitle(boolean ascending);
    List<Book> sortBooksByAuthor(boolean ascending);
    List<Book> sortBooksByCategory(boolean ascending);
    List<Book> sortBooksByPopularity(boolean ascending);

    // NEW: Top N most borrowed books using a heap
    List<Book> getMostBorrowedBooks(int n);

    List<Book> getAllBooks(); // returns a list of all books

    void listAllBooks(); // prints all books to console
}