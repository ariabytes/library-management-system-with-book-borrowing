package model;

import utils.SinglyLinkedList_Imp;

public class Member {
    private String id;
    private String name;
    private SinglyLinkedList_Imp<String> borrowedBookIds;
    private SinglyLinkedList_Imp<String> reservationQueue;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.borrowedBookIds = new SinglyLinkedList_Imp<>();
        this.reservationQueue = new SinglyLinkedList_Imp<>();
    }

    // Default constructor (optional but helpful for GUI use)
    public Member() {
        this.borrowedBookIds = new SinglyLinkedList_Imp<>();
        this.reservationQueue = new SinglyLinkedList_Imp<>();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public SinglyLinkedList_Imp<String> getBorrowedBookIds() { return borrowedBookIds; }
    public SinglyLinkedList_Imp<String> getReservationQueue() { return reservationQueue; }

    // ✅ Added Setters (to fix GUI errors)
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Borrow and reserve management
    public void borrowBook(String bookId) {
        if (borrowedBookIds.indexOf(bookId) == -1) {
            borrowedBookIds.addLast(bookId);
        }
        
    }

    public void returnBook(String bookId) {
        borrowedBookIds.removeItem(bookId);
    }

    public void addToReservationQueue(String bookId) {
        if (reservationQueue.indexOf(bookId) == -1) {
            reservationQueue.addLast(bookId);
        }
    }

    public void removeFromReservationQueue(String bookId) {
        reservationQueue.removeItem(bookId);
    }

    @Override
    public String toString() {
        return id + "," + name;
    }
}
