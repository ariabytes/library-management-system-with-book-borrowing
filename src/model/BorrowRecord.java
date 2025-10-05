package model;

import java.time.LocalDate;

public class BorrowRecord {
    private String bookId;
    private String memberId;
    private String memberName;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    public BorrowRecord(String bookId, String memberId, String memberName, LocalDate borrowDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.borrowDate = borrowDate;
        this.returnDate = null;
    }

    // Getters
    public String getBookId() { return bookId; }
    public String getMemberId() { return memberId; }
    public String getMemberName() { return memberName; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getReturnDate() { return returnDate; }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return bookId + "," + memberId + "," + memberName + "," + borrowDate + "," + (returnDate != null ? returnDate : "not returned");
    }
}