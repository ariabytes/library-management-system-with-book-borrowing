package service_implementation;

import service_interface.FileIOInterface;
import model.*;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class FileIOService implements FileIOInterface {

    @Override
    public List<Book> loadBooks(String filePath) {
        List<Book> books = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    Book book = new Book(parts[0], parts[1], parts[2], parts[3]);
                    book.setAvailable(parts[4].equalsIgnoreCase("available"));
                    // parts[5] is borrowCount
                    for (int i = 0; i < Integer.parseInt(parts[5]); i++) {
                        book.incrementBorrowCount();
                    }
                    books.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
        return books;
    }

    @Override
    public void saveBooks(String filePath, List<Book> books) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                bw.write(book.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    @Override
    public List<Member> loadMembers(String filePath) {
        List<Member> members = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    members.add(new Member(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading members: " + e.getMessage());
        }
        return members;
    }

    @Override
    public void saveMembers(String filePath, List<Member> members) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Member member : members) {
                bw.write(member.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }

    @Override
    public List<BorrowRecord> loadBorrowRecords(String filePath) {
        List<BorrowRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String bookId = parts[0];
                    String memberId = parts[1];
                    LocalDate borrowDate = LocalDate.parse(parts[2]);
                    BorrowRecord record = new BorrowRecord(bookId, memberId, borrowDate);

                    if (!parts[3].equalsIgnoreCase("not returned")) {
                        record.setReturnDate(LocalDate.parse(parts[3]));
                    }
                    records.add(record);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading borrow records: " + e.getMessage());
        }
        return records;
    }

    @Override
    public void saveBorrowRecords(String filePath, List<BorrowRecord> borrowRecords) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (BorrowRecord record : borrowRecords) {
                bw.write(record.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving borrow records: " + e.getMessage());
        }
    }
}
