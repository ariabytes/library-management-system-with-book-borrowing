// This is for the CRUD functions for file handling
// To delete or update:
//      Load all objects from file (e.g., loadBooks → List<Book>).
//      Modify the list in memory (remove, update, add).
//      Save the whole list back to file (e.g., saveBooks).

package service;

import java.util.List;
import model.Book;
import model.BorrowRecord;
import model.Member;

public interface FileIOInterface {
    
    // Load all books from a file and return as a List
    List<Book> loadBooks(String filePath);

    // Save all books to a file
    void saveBooks(String filePath, List<Book> books);

    // Load all members from a file and return as a List
    List<Member> loadMembers(String filePath);

    // Save all members to a file
    void saveMembers(String filePath, List<Member> members);

    // Load all borrow records from a file and return as a List
    List<BorrowRecord> loadBorrowRecords(String filePath);

    // Save all borrow records to a file
    void saveBorrowRecords(String filePath, List<BorrowRecord> borrowRecords);
}
