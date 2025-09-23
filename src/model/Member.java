package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Member {
    private String id;
    private String name;
    private List<String> borrowedBookIds; // List of borrowed book IDs
    private LinkedList<String> reservationQueue; // List of reserved book IDs

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
        this.borrowedBookIds = new ArrayList<>();
        this.reservationQueue = new LinkedList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public List<String> getBorrowedBookIds() { return borrowedBookIds; }
    public LinkedList<String> getReservationQueue() { return reservationQueue; }

    // Borrow and reserve management
    public void borrowBook(String bookId) {
        if (!borrowedBookIds.contains(bookId)) {
            borrowedBookIds.add(bookId);
        }
    }

    public void returnBook(String bookId) {
        borrowedBookIds.remove(bookId);
    }

    public void addToReservationQueue(String bookId) {
        if (!reservationQueue.contains(bookId)) {
            reservationQueue.addLast(bookId);
        }
    }

    public void removeFromReservationQueue(String bookId) {
        reservationQueue.remove(bookId);
    }

    @Override
    public String toString() {
        return id + "," + name;
    }
}