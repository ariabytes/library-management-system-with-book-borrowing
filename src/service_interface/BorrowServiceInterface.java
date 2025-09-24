package service_interface;

import model.BorrowRecord;
import java.util.List;
import java.time.LocalDate;

public interface BorrowServiceInterface {

    // Borrow a book: returns true if successful (book available, member eligible,
    // etc.)
    boolean borrowBook(String memberId, String bookId, LocalDate borrowDate);

    // Return a book: returns true if successful (book was borrowed by member, etc.)
    boolean returnBook(String memberId, String bookId, LocalDate returnDate);

    // Get a list of all borrow records
    List<BorrowRecord> getAllBorrowRecords();

    // Get borrow records for a specific member
    List<BorrowRecord> getBorrowRecordsByMember(String memberId);

    // Get borrow records for a specific book
    List<BorrowRecord> getBorrowRecordsByBook(String bookId);

    // Check if a book is currently borrowed by a member
    boolean isBookBorrowedByMember(String bookId, String memberId);

    // Get currently borrowed books for a member
    List<String> getCurrentlyBorrowedBookIdsByMember(String memberId);

    // List all currently borrowed books (not yet returned)
    List<BorrowRecord> getCurrentlyBorrowedRecords();

    // Optional: handle overdue, fines, etc. as needed for your project

    // For CLI/debugging
    void listAllBorrowRecords();
}