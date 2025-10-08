package service_implementation;

import service_interface.BorrowServiceInterface;
import model.Book;
import model.BorrowRecord;
import model.Member;
import utils.SinglyLinkedList_Imp;
import utils.HashMap_Imp;
import java.time.LocalDate;
import java.util.*;

public class BorrowService implements BorrowServiceInterface {

    private SinglyLinkedList_Imp<BorrowRecord> borrowRecords;
    private SinglyLinkedList_Imp<Book> books;
    private SinglyLinkedList_Imp<Member> members;
    private HashMap_Imp<String, Book> bookMap;
    private HashMap_Imp<String, Member> memberMap;
    private FileIOService fileIO;

    private final String booksFile = "books.txt";
    private final String membersFile = "members.txt";
    private final String recordsFile = "borrow_records.txt";

    public BorrowService(List<Member> membersList, List<Book> booksList) {
        this.fileIO = new FileIOService();

        // Initialize books
        this.books = new SinglyLinkedList_Imp<>();
        this.bookMap = new HashMap_Imp<>();
        for (Book book : booksList) {
            books.addLast(book);
            bookMap.put(book.getId(), book);
        }

        // Initialize members
        this.members = new SinglyLinkedList_Imp<>();
        this.memberMap = new HashMap_Imp<>();
        for (Member member : membersList) {
            members.addLast(member);
            memberMap.put(member.getId(), member);
        }

        // Load borrow records
        this.borrowRecords = new SinglyLinkedList_Imp<>();
        List<BorrowRecord> loadedRecords = fileIO.loadBorrowRecords(recordsFile);
        for (BorrowRecord record : loadedRecords) {
            borrowRecords.addLast(record);
        }
    }

    @Override
    public boolean borrowBook(String memberId, String bookId, LocalDate borrowDate) {
        Book book = bookMap.get(bookId);
        Member member = memberMap.get(memberId);

        if (member == null) {
            List<Member> allMembers = fileIO.loadMembers(membersFile);
            for (Member m : allMembers) {
                if (m.getId().equals(memberId)) {
                    member = m;
                    members.addLast(member);
                    memberMap.put(member.getId(), member);
                    break;
                }
            }
        }

        if (book == null || member == null || !book.isAvailable()) return false;

        member.borrowBook(bookId);
        book.setAvailable(false);
        book.setCurrentBorrower(memberId, member.getName());
        book.incrementBorrowCount();

        borrowRecords.addLast(new BorrowRecord(bookId, memberId, member.getName(), borrowDate));

        saveBooks();
        saveMembers();
        saveBorrowRecords();

        return true;
    }

    @Override
    public boolean returnBook(String memberId, String bookId, LocalDate returnDate) {
        // ✅ CRITICAL FIX: Reload borrow records from file to get latest data
        this.borrowRecords = new SinglyLinkedList_Imp<>();
        List<BorrowRecord> loadedRecords = fileIO.loadBorrowRecords(recordsFile);
        for (BorrowRecord record : loadedRecords) {
            borrowRecords.addLast(record);
        }
        System.out.println("[BorrowService] Reloaded " + borrowRecords.size() + " borrow records from file");
        
        Book book = bookMap.get(bookId);
        Member member = memberMap.get(memberId);

        // ✅ If member not found in our map, reload from file and sync
        if (member == null) {
            List<Member> allMembers = fileIO.loadMembers(membersFile);
            for (Member m : allMembers) {
                if (m.getId().equals(memberId)) {
                    member = m;
                    members.addLast(member);
                    memberMap.put(member.getId(), member);
                    break;
                }
            }
        }

        if (book == null || member == null) return false;

        BorrowRecord record = null;
        int recordIndex = -1;
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getBookId().equals(bookId) && r.getMemberId().equals(memberId) && r.getReturnDate() == null) {
                record = r;
                recordIndex = i;
                System.out.println("[BorrowService] Found active borrow record at index " + i);
                break;
            }
        }

        if (record == null) {
            System.out.println("[BorrowService] ERROR: No active borrow record found for book " + bookId + " and member " + memberId);
            return false;
        }

        // ✅ Update the record with return date
        record.setReturnDate(returnDate);
        System.out.println("[BorrowService] Set return date to: " + returnDate);
        
        // ✅ Update member - remove the borrowed book
        member.returnBook(bookId);

        // ✅ Update book
        book.setAvailable(true);
        book.clearCurrentBorrower();

        // ✅ Update the book in the map
        bookMap.put(book.getId(), book);
        
        // ✅ Update the member in the map
        memberMap.put(member.getId(), member);

        // ✅ Save all changes
        System.out.println("[BorrowService] Saving after return - Book available: " + book.isAvailable());
        saveBooks();
        saveMembers();
        saveBorrowRecords();
        
        System.out.println("[BorrowService] Return operation completed successfully");

        return true;
    }

    @Override
    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> result = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) result.add(borrowRecords.get(i));
        return result;
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByMember(String memberId) {
        List<BorrowRecord> result = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getMemberId().equals(memberId)) result.add(r);
        }
        return result;
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByBook(String bookId) {
        List<BorrowRecord> result = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getBookId().equals(bookId)) result.add(r);
        }
        return result;
    }

    @Override
    public boolean isBookBorrowedByMember(String bookId, String memberId) {
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getBookId().equals(bookId) && r.getMemberId().equals(memberId) && r.getReturnDate() == null) return true;
        }
        return false;
    }

    @Override
    public List<String> getCurrentlyBorrowedBookIdsByMember(String memberId) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getMemberId().equals(memberId) && r.getReturnDate() == null) result.add(r.getBookId());
        }
        return result;
    }

    @Override
    public List<BorrowRecord> getCurrentlyBorrowedRecords() {
        List<BorrowRecord> result = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getReturnDate() == null) result.add(r);
        }
        return result;
    }

    @Override
    public void listAllBorrowRecords() {
        for (int i = 0; i < borrowRecords.size(); i++) System.out.println(borrowRecords.get(i));
    }

    // ===============================
    // 🔽 Save Utilities
    // ===============================
    private void saveBooks() {
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) bookList.add(books.get(i));
        fileIO.saveBooksAlphabetically(booksFile, bookList);
    }

    private void saveMembers() {
        // ✅ Build list from memberMap (which has the updated members)
        List<Member> memberList = new ArrayList<>();
        
        // Get all members from the map (these are the updated ones)
        for (int i = 0; i < members.size(); i++) {
            Member m = members.get(i);
            Member fromMap = memberMap.get(m.getId());
            if (fromMap != null) {
                memberList.add(fromMap);
            }
        }
        
        // ✅ Direct save without reloading
        fileIO.saveMembers(membersFile, memberList);
    }

    private void saveBorrowRecords() {
        List<BorrowRecord> recordList = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) recordList.add(borrowRecords.get(i));
        fileIO.saveBorrowRecords(recordsFile, recordList);
        System.out.println("[BorrowService] Saved " + recordList.size() + " borrow records to file");
    }
    
}