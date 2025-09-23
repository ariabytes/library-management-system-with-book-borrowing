package model;
import java.util.LinkedList;

/**
 * Book model class representing a book in the library system.
 * Demonstrates use of LinkedList for reservation queue and tracks borrow count for popularity.
 */
public class Book {
    private String id;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable;
    private int borrowCount; // Number of times the book was borrowed
    private LinkedList<String> reservationQueue; // Queue of member IDs waiting for this book

    // Constructor
    public Book(String id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = true;
        this.borrowCount = 0;
        this.reservationQueue = new LinkedList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public int getBorrowCount() { return borrowCount; }
    public LinkedList<String> getReservationQueue() { return reservationQueue; }

    // Setters
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }

    // Increment borrow count when the book is borrowed
    public void incrementBorrowCount() { this.borrowCount++; }

    // Reservation queue operations

    // Add a member to the end of the reservation queue (if not already present)
    public void addToReservationQueue(String memberId) {
        if (!reservationQueue.contains(memberId)) {
            reservationQueue.addLast(memberId);
        }
    }

    // Remove a specific member from the reservation queue
    public void removeFromReservationQueue(String memberId) {
        reservationQueue.remove(memberId);
    }

    // Poll (remove and return) the next member in the reservation queue
    public String pollNextReservation() {
        return reservationQueue.pollFirst();
    }

    // Check if the reservation queue is not empty
    public boolean hasReservations() {
        return !reservationQueue.isEmpty();
    }

    // String representation for file I/O (does not include reservationQueue for simplicity)
    @Override
    public String toString() {
        // Format: id,title,author,category,available/borrowed,borrowCount
        return id + "," + title + "," + author + "," + category + "," +
               (isAvailable ? "available" : "borrowed") + "," + borrowCount;
    }
}