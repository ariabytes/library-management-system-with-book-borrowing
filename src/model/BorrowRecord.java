package model;

import java.time.LocalDate;

public class BorrowRecord {
    private String bookId;
    private String memberId;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowRecord(String bookId, String memberId, LocalDate borrowDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.borrowDate = borrowDate;
        this.returnDate = null;
    }

    // Getters and setters
    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return bookId + "," + memberId + "," + borrowDate + "," + (returnDate != null ? returnDate : "not returned");
    }
}