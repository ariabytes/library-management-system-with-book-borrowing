package model;

import utils.Queue_Imp;

/**
 * Book model class representing a book in the library system.
 * Uses custom Queue_Imp for reservation queue (FIFO) and tracks borrow count for popularity.
 *
 * Implements Comparable<Book> so utility structures (e.g. MaxHeap_Imp<Book>)
 * can compare books by borrow count (higher borrowCount => "greater").
 */
public class Book implements Comparable<Book> {
    private String id;
    private String title;
    private String author;
    private String category;
    private boolean isAvailable;
    private String currentBorrowerId;
    private String currentBorrowerName;
    private int borrowCount;
    private Queue_Imp<String> reservationQueue; // Queue of member IDs (FIFO)
    private Queue_Imp<String> reservationQueueNames; // Queue of member names for display

    public Book(String id, String title, String author, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.isAvailable = true;
        this.currentBorrowerId = null;
        this.currentBorrowerName = null;
        this.borrowCount = 0;
        this.reservationQueue = new Queue_Imp<>();
        this.reservationQueueNames = new Queue_Imp<>();
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public String getCurrentBorrowerId() { return currentBorrowerId; }
    public String getCurrentBorrowerName() { return currentBorrowerName; }
    public int getBorrowCount() { return borrowCount; }
    public Queue_Imp<String> getReservationQueue() { return reservationQueue; }
    public Queue_Imp<String> getReservationQueueNames() { return reservationQueueNames; }

    // Setters
    public void setAvailable(boolean available) { this.isAvailable = available; }
    public void setCurrentBorrower(String borrowerId, String borrowerName) {
        this.currentBorrowerId = borrowerId;
        this.currentBorrowerName = borrowerName;
    }
    public void clearCurrentBorrower() {
        this.currentBorrowerId = null;
        this.currentBorrowerName = null;
    }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setCategory(String category) { this.category = category; }

    public void incrementBorrowCount() { this.borrowCount++; }

    // Reservation queue operations using Queue

    // Add a member to the reservation queue (enqueue)
    public void addToReservationQueue(String memberId, String memberName) {
        // Note: Queue allows duplicates, but we'll check in service layer if needed
        reservationQueue.enqueue(memberId);
        reservationQueueNames.enqueue(memberName);
    }

    // Remove and return the next member in the reservation queue (dequeue)
    public String pollNextReservation() {
        if (!reservationQueue.isEmpty()) {
            // Dequeue both id and name (keep them in sync)
            String id = reservationQueue.dequeue();
            reservationQueueNames.dequeue(); // discard or use as needed by caller
            return id;
        }
        return null;
    }

    // Check if the reservation queue is not empty
    public boolean hasReservations() {
        return !reservationQueue.isEmpty();
    }

    // Get queue size (need to peek at internal structure or track separately)
    public int getReservationCount() {
        // Since Queue_Imp doesn't have size(), we'll count by converting to string
        // This is a workaround - ideally Queue_Imp should have a size() method
        String queueStr = reservationQueue.toString();
        if (queueStr.equals("[]")) return 0;
        // Count commas + 1 to get element count
        int count = 1;
        for (char c : queueStr.toCharArray()) {
            if (c == ',') count++;
        }
        return count;
    }

    // String representation for file I/O
    @Override
    public String toString() {
        // Format: id,title,author,category,available/borrowed,currentBorrower(ID:Name),borrowCount,queue[(ID:Name);(ID:Name)]
        
        // Build queue string with ID:Name pairs
        String queueStr;
        if (reservationQueue.isEmpty()) {
            queueStr = "[none]";
        } else {
            // We need to iterate through the queue without dequeuing permanently
            Queue_Imp<String> tempIds = new Queue_Imp<>();
            Queue_Imp<String> tempNames = new Queue_Imp<>();
            StringBuilder queueBuilder = new StringBuilder("[");
            boolean first = true;

            while (!reservationQueue.isEmpty()) {
                String id = reservationQueue.dequeue();
                String name = reservationQueueNames.dequeue();
                tempIds.enqueue(id);
                tempNames.enqueue(name);

                if (!first) queueBuilder.append(";");
                queueBuilder.append(id).append(":").append(name);
                first = false;
            }

            // Restore the queues
            while (!tempIds.isEmpty()) {
                reservationQueue.enqueue(tempIds.dequeue());
                reservationQueueNames.enqueue(tempNames.dequeue());
            }

            queueBuilder.append("]");
            queueStr = queueBuilder.toString();
        }
        
        // Build current borrower string
        String borrowerStr = (currentBorrowerId == null || currentBorrowerId.isEmpty()) ? 
            "none" : 
            currentBorrowerId + ":" + currentBorrowerName;
        
        return id + "," + title + "," + author + "," + category + "," +
               (isAvailable ? "available" : "borrowed") + "," + 
               borrowerStr + "," + 
               borrowCount + "," + 
               queueStr;
    }

    /**
     * Compare books by borrowCount so MaxHeap_Imp can use natural ordering.
     * Higher borrowCount => greater book (so heap root will be most-borrowed).
     */
    @Override
    public int compareTo(Book other) {
        if (other == null) return 1;
        return Integer.compare(this.borrowCount, other.borrowCount);
    }
}
