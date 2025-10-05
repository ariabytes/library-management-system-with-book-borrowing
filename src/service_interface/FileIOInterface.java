// This is for the CRUD functions for file handling
// To delete or update:
//      Load all objects from file (e.g., loadBooks → List<Book>).
//      Modify the list in memory (remove, update, add).
//      Save the whole list back to file (e.g., saveBooks).

package service_interface;

import java.util.List;
import model.Book;
import model.BorrowRecord;
import model.Member;

public interface FileIOInterface {
    // BOOKS
    void saveBooks(String filename, List<Book> books);
    void appendBook(String filename, Book book);
    List<Book> loadBooks(String filename);
    void deleteBook(String filename, String bookId);

    // MEMBERS
    void saveMembers(String filename, List<Member> members);
    void appendMember(String filename, Member member);
    List<Member> loadMembers(String filename);
    void deleteMember(String filename, String memberId);

    // BORROW RECORDS
    void saveBorrowRecords(String filename, List<BorrowRecord> records);
    void appendBorrowRecord(String filename, BorrowRecord record);
    List<BorrowRecord> loadBorrowRecords(String filename);
    void deleteBorrowRecord(String filename, String memberId, String bookId);
}
