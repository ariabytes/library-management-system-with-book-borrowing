package service_implementation;

import service_interface.BorrowServiceInterface;
import model.Book;
import model.BorrowRecord;
import model.Member;
import utils.SinglyLinkedList_Imp;
import utils.HashMap_Imp;
import utils.MaxHeap_Imp;

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

        // Initialize books (shared references between list and map)
        this.books = new SinglyLinkedList_Imp<>();
        this.bookMap = new HashMap_Imp<>();
        for (Book book : booksList) {
            books.addLast(book);
            bookMap.put(book.getId(), book);
        }

        // Initialize members (shared references)
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

        // If already borrowed
        if (!book.isAvailable()) {
            return false;
        }

        // ✅ Update member - this modifies the actual object
        member.borrowBook(bookId);
        
        // ✅ Update book
        book.setAvailable(false);
        book.setCurrentBorrower(memberId, member.getName());
        book.incrementBorrowCount();

        // ✅ Add borrow record
        borrowRecords.addLast(new BorrowRecord(bookId, memberId, member.getName(), borrowDate));

        // ✅ Update maps (member object is already modified above)
        bookMap.put(book.getId(), book);
        memberMap.put(member.getId(), member);

        // ✅ Write all updated data to files
        saveBooks();
        saveMembers();
        saveBorrowRecords();

        System.out.println("[INFO] Borrow successful: " + book.getTitle() + " borrowed by " + member.getName());
        System.out.println("[DEBUG] Member now has " + member.getBorrowedBookIds().size() + " borrowed books");
        return true;
    }

    @Override
    public boolean returnBook(String memberId, String bookId, LocalDate returnDate) {
        Book book = bookMap.get(bookId);
        Member member = memberMap.get(memberId);

        if (book == null || member == null) return false;

        BorrowRecord record = null;
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getBookId().equals(bookId) && r.getMemberId().equals(memberId) && r.getReturnDate() == null) {
                record = r;
                break;
            }
        }

        if (record == null) return false;

        record.setReturnDate(returnDate);
        member.returnBook(bookId);

        if (book.hasReservations()) {
            String nextMemberId = book.pollNextReservation();
            Member nextMember = memberMap.get(nextMemberId);

            if (nextMember != null) {
                borrowBook(nextMemberId, bookId, LocalDate.now());
                nextMember.removeFromReservationQueue(bookId);
            }
        } else {
            book.setAvailable(true);
            book.clearCurrentBorrower();
        }

        saveBooks();
        saveMembers();
        saveBorrowRecords();

        System.out.println("[INFO] Book returned: " + book.getTitle() + " by " + member.getName());
        return true;
    }

    @Override
    public List<BorrowRecord> getAllBorrowRecords() {
        List<BorrowRecord> result = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) {
            result.add(borrowRecords.get(i));
        }
        return result;
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByMember(String memberId) {
        List<BorrowRecord> result = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getMemberId().equals(memberId)) {
                result.add(r);
            }
        }
        return result;
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByBook(String bookId) {
        List<BorrowRecord> result = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getBookId().equals(bookId)) {
                result.add(r);
            }
        }
        return result;
    }

    @Override
    public boolean isBookBorrowedByMember(String bookId, String memberId) {
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getBookId().equals(bookId) && r.getMemberId().equals(memberId) && r.getReturnDate() == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getCurrentlyBorrowedBookIdsByMember(String memberId) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getMemberId().equals(memberId) && r.getReturnDate() == null) {
                result.add(r.getBookId());
            }
        }
        return result;
    }

    @Override
    public List<BorrowRecord> getCurrentlyBorrowedRecords() {
        List<BorrowRecord> result = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) {
            BorrowRecord r = borrowRecords.get(i);
            if (r.getReturnDate() == null) {
                result.add(r);
            }
        }
        return result;
    }

    @Override
    public void listAllBorrowRecords() {
        if (borrowRecords.isEmpty()) {
            System.out.println("No borrow records available.");
        } else {
            for (int i = 0; i < borrowRecords.size(); i++) {
                System.out.println(borrowRecords.get(i));
            }
        }
    }
    public void addMember(Member member) {
        members.addLast(member);
        memberMap.put(member.getId(), member);
    }

    /**
     * ✅ Get top N most borrowed books using MaxHeap_Imp
     */
    public List<Book> getMostBorrowedBooks(int n) {
        List<Book> allBooks = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            allBooks.add(books.get(i));
        }

        MaxHeap_Imp<Book> heap = new MaxHeap_Imp<>();
        heap.buildHeap(allBooks);

        List<Book> result = new ArrayList<>();
        for (int i = 0; i < n && !heap.isEmpty(); i++) {
            result.add(heap.getMax());
            allBooks.remove(heap.getMax());
            heap.buildHeap(allBooks);
        }
        return result;
    }

    // ===============================
    // 🔽 Save Utilities
    // ===============================

    private void saveBooks() {
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            bookList.add(books.get(i));
        }
        fileIO.saveBooks(booksFile, bookList);
    }

 // Find the private saveMembers() method in BorrowService.java (around line 280)
 // Replace it with this:

    private void saveMembers() {
        // ✅ Reload all members from file to get the latest state
        List<Member> latestMembers = fileIO.loadMembers(membersFile);
        
        // ✅ Update the loaded members with data from our memberMap
        for (int i = 0; i < latestMembers.size(); i++) {
            Member loaded = latestMembers.get(i);
            Member fromMap = memberMap.get(loaded.getId());
            
            if (fromMap != null) {
                // Replace the loaded member with our updated one
                latestMembers.set(i, fromMap);
            }
        }
        
        // ✅ Save the updated list
        fileIO.saveMembers(membersFile, latestMembers);
    }

    private void saveBorrowRecords() {
        List<BorrowRecord> recordList = new ArrayList<>();
        for (int i = 0; i < borrowRecords.size(); i++) {
            recordList.add(borrowRecords.get(i));
        }
        fileIO.saveBorrowRecords(recordsFile, recordList);
    }
}
