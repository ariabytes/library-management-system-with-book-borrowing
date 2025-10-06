package service_implementation;

import service_interface.FileIOInterface;
import model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class FileIOService implements FileIOInterface {

    // ========== BOOKS ==========
    @Override
    public void saveBooks(String filename, List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (Book b : books) {
                // Use the Book's toString() method which has the complete format
                writer.write(b.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }
 // Add this method to FileIOService.java class (after the existing saveBooks method)

    /**
     * Saves books to file in alphabetical order by title
     */
    public void saveBooksAlphabetically(String filename, List<Book> books) {
        // Create a sorted copy
        List<Book> sortedBooks = new ArrayList<>(books);
        sortedBooks.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        
        // Save the sorted list
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (Book b : sortedBooks) {
                writer.write(b.toString());
                writer.newLine();
            }
            System.out.println("[FileIO] Books saved alphabetically to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    @Override
    public void appendBook(String filename, Book book) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(book.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error appending book: " + e.getMessage());
        }
    }

    @Override
    public List<Book> loadBooks(String filename) {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Format: id,title,author,category,available/borrowed,currentBorrower(ID:Name),borrowCount,queue
                if (parts.length >= 8) {
                    String id = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    String category = parts[3];
                    
                    Book book = new Book(id, title, author, category);
                    
                    // Set availability
                    book.setAvailable(parts[4].equals("available"));
                    
                    // Set current borrower
                    if (!parts[5].equals("none")) {
                        String[] borrowerParts = parts[5].split(":");
                        if (borrowerParts.length == 2) {
                            book.setCurrentBorrower(borrowerParts[0], borrowerParts[1]);
                        }
                    }
                    
                    // Set borrow count
                    int borrowCount = Integer.parseInt(parts[6]);
                    for (int i = 0; i < borrowCount; i++) {
                        book.incrementBorrowCount();
                    }
                    
                    // Parse reservation queue
                    String queueStr = parts[7];
                    if (!queueStr.equals("[none]")) {
                        // Remove brackets
                        queueStr = queueStr.substring(1, queueStr.length() - 1);
                        if (!queueStr.isEmpty()) {
                            String[] reservations = queueStr.split(";");
                            for (String reservation : reservations) {
                                String[] resParts = reservation.split(":");
                                if (resParts.length == 2) {
                                    book.addToReservationQueue(resParts[0], resParts[1]);
                                }
                            }
                        }
                    }
                    
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing book data: " + e.getMessage());
        }
        return books;
    }

    @Override
    public void deleteBook(String filename, String bookId) {
        List<Book> books = loadBooks(filename);
        books.removeIf(b -> b.getId().equals(bookId));
        saveBooks(filename, books);
    }

    // ========== MEMBERS ==========
    @Override
    public void saveMembers(String filename, List<Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (Member m : members) {
                // Format: id,name,borrowedBooks,reservations
                StringBuilder sb = new StringBuilder();
                sb.append(m.getId()).append(",");
                sb.append(m.getName()).append(",");
                
                // Save borrowed book IDs
                utils.SinglyLinkedList_Imp<String> borrowed = m.getBorrowedBookIds();
                if (borrowed.isEmpty()) {
                    sb.append("[none]");
                } else {
                    sb.append("[");
                    for (int i = 0; i < borrowed.size(); i++) {
                        sb.append(borrowed.get(i));
                        if (i < borrowed.size() - 1) sb.append(";");
                    }
                    sb.append("]");
                }
                sb.append(",");
                
                // Save reservation queue
                utils.SinglyLinkedList_Imp<String> reservations = m.getReservationQueue();
                if (reservations.isEmpty()) {
                    sb.append("[none]");
                } else {
                    sb.append("[");
                    for (int i = 0; i < reservations.size(); i++) {
                        sb.append(reservations.get(i));
                        if (i < reservations.size() - 1) sb.append(";");
                    }
                    sb.append("]");
                }
                
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }

    @Override
    public void appendMember(String filename, Member member) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(member.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error appending member: " + e.getMessage());
        }
    }

    public List<Member> loadMembers(String filename) {
        List<Member> members = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Format: id,name,borrowedBooks,reservations
                if (parts.length >= 2) {
                    String id = parts[0];
                    String name = parts[1];
                    Member member = new Member(id, name);
                    
                    // Load borrowed books (if exists)
                    if (parts.length >= 3 && !parts[2].equals("[none]")) {
                        String borrowedStr = parts[2];
                        // Remove brackets
                        borrowedStr = borrowedStr.substring(1, borrowedStr.length() - 1);
                        if (!borrowedStr.isEmpty()) {
                            String[] bookIds = borrowedStr.split(";");
                            for (String bookId : bookIds) {
                                member.borrowBook(bookId);
                            }
                        }
                    }
                    
                    // Load reservations (if exists)
                    if (parts.length >= 4 && !parts[3].equals("[none]")) {
                        String reservationStr = parts[3];
                        // Remove brackets
                        reservationStr = reservationStr.substring(1, reservationStr.length() - 1);
                        if (!reservationStr.isEmpty()) {
                            String[] bookIds = reservationStr.split(";");
                            for (String bookId : bookIds) {
                                member.addToReservationQueue(bookId);
                            }
                        }
                    }
                    
                    members.add(member);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading members: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing member data: " + e.getMessage());
        }
        return members;
    }

    @Override
    public void deleteMember(String filename, String memberId) {
        List<Member> members = loadMembers(filename);
        members.removeIf(m -> m.getId().equals(memberId));
        saveMembers(filename, members);
    }

    // ========== BORROW RECORDS ==========
    @Override
    public void saveBorrowRecords(String filename, List<BorrowRecord> records) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            for (BorrowRecord br : records) {
                // Format: bookId,memberId,memberName,borrowDate,returnDate
                writer.write(br.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving borrow records: " + e.getMessage());
        }
    }

    @Override
    public void appendBorrowRecord(String filename, BorrowRecord record) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(record.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error appending borrow record: " + e.getMessage());
        }
    }

    @Override
    public List<BorrowRecord> loadBorrowRecords(String filename) {
        List<BorrowRecord> records = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                // Format: bookId,memberId,memberName,borrowDate,returnDate
                if (parts.length >= 4) {
                    String bookId = parts[0];
                    String memberId = parts[1];
                    String memberName = parts[2];
                    LocalDate borrowDate = LocalDate.parse(parts[3]);
                    
                    BorrowRecord record = new BorrowRecord(bookId, memberId, memberName, borrowDate);
                    
                    // Set return date if present and not "not returned"
                    if (parts.length == 5 && !parts[4].equals("not returned")) {
                        record.setReturnDate(LocalDate.parse(parts[4]));
                    }
                    
                    records.add(record);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading borrow records: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error parsing borrow record: " + e.getMessage());
        }
        return records;
    }

    @Override
    public void deleteBorrowRecord(String filename, String memberId, String bookId) {
        List<BorrowRecord> records = loadBorrowRecords(filename);
        records.removeIf(r -> r.getMemberId().equals(memberId) && r.getBookId().equals(bookId));
        saveBorrowRecords(filename, records);
    }
}
