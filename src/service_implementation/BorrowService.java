package service_implementation;

import service_interface.BorrowServiceInterface;
import model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BorrowService implements BorrowServiceInterface {
    private List<BorrowRecord> borrowRecords;
    private List<Book> books;
    private List<Member> members;

    public BorrowService(List<Book> books, List<Member> members) {
        this.borrowRecords = new ArrayList<>();
        this.books = books;
        this.members = members;
    }

    @Override
    public boolean borrowBook(String memberId, String bookId, LocalDate borrowDate) {
        Book book = findBook(bookId);
        Member member = findMember(memberId);

        if (book == null || member == null) return false;

        // Book must be available
        if (!book.isAvailable()) {
            // Add member to reservation queue if not available
            book.addToReservationQueue(memberId);
            member.addToReservationQueue(bookId);
            return false;
        }

        // Borrowing action
        book.setAvailable(false);
        book.incrementBorrowCount();
        member.borrowBook(bookId);
        borrowRecords.add(new BorrowRecord(bookId, memberId, borrowDate));

        return true;
    }

    @Override
    public boolean returnBook(String memberId, String bookId, LocalDate returnDate) {
        Book book = findBook(bookId);
        Member member = findMember(memberId);

        if (book == null || member == null) return false;

        // Find active borrow record
        BorrowRecord record = borrowRecords.stream()
                .filter(r -> r.getBookId().equals(bookId)
                        && r.getMemberId().equals(memberId)
                        && r.getReturnDate() == null)
                .findFirst()
                .orElse(null);

        if (record == null) return false; // Not borrowed by member

        // Return the book
        record.setReturnDate(returnDate);
        member.returnBook(bookId);

        // If reservation queue exists, assign book to next member
        if (book.hasReservations()) {
            String nextMemberId = book.pollNextReservation();
            Member nextMember = findMember(nextMemberId);

            if (nextMember != null) {
                borrowBook(nextMemberId, bookId, LocalDate.now());
                nextMember.removeFromReservationQueue(bookId);
            }
        } else {
            book.setAvailable(true);
        }

        return true;
    }

    @Override
    public List<BorrowRecord> getAllBorrowRecords() {
        return new ArrayList<>(borrowRecords);
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByMember(String memberId) {
        return borrowRecords.stream()
                .filter(r -> r.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecord> getBorrowRecordsByBook(String bookId) {
        return borrowRecords.stream()
                .filter(r -> r.getBookId().equals(bookId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isBookBorrowedByMember(String bookId, String memberId) {
        return borrowRecords.stream()
                .anyMatch(r -> r.getBookId().equals(bookId)
                        && r.getMemberId().equals(memberId)
                        && r.getReturnDate() == null);
    }

    @Override
    public List<String> getCurrentlyBorrowedBookIdsByMember(String memberId) {
        return borrowRecords.stream()
                .filter(r -> r.getMemberId().equals(memberId) && r.getReturnDate() == null)
                .map(BorrowRecord::getBookId)
                .collect(Collectors.toList());
    }

    @Override
    public List<BorrowRecord> getCurrentlyBorrowedRecords() {
        return borrowRecords.stream()
                .filter(r -> r.getReturnDate() == null)
                .collect(Collectors.toList());
    }

    @Override
    public void listAllBorrowRecords() {
        if (borrowRecords.isEmpty()) {
            System.out.println("No borrow records available.");
        } else {
            borrowRecords.forEach(System.out::println);
        }
    }

    // Helper methods
    private Book findBook(String bookId) {
        return books.stream()
                .filter(b -> b.getId().equalsIgnoreCase(bookId))
                .findFirst()
                .orElse(null);
    }

    private Member findMember(String memberId) {
        return members.stream()
                .filter(m -> m.getId().equalsIgnoreCase(memberId))
                .findFirst()
                .orElse(null);
    }
}
