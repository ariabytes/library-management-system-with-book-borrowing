package main;

import service_implementation.*;
import model.*;
import java.time.LocalDate;
import java.util.*;

public class Test_Main {
    public static void main(String[] args) {
        // Initialize empty lists
        List<Book> books = new ArrayList<>();
        List<Member> members = new ArrayList<>();

        // Initialize services
        BookService bookService = new BookService();
        MemberService memberService = new MemberService(members, books);
        BorrowService borrowService = new BorrowService(books, members);
        FileIOService fileIOService = new FileIOService();

        // Add some sample data
        Book b1 = new Book("B001", "The Hobbit", "J.R.R. Tolkien", "Fantasy");
        Book b2 = new Book("B002", "1984", "George Orwell", "Dystopian");
        Book b3 = new Book("B003", "Clean Code", "Robert C. Martin", "Programming");

        bookService.addBook(b1);
        bookService.addBook(b2);
        bookService.addBook(b3);

        books.addAll(bookService.getAllBooks()); // sync with borrow/member service

        Member m1 = new Member("M001", "Alice");
        Member m2 = new Member("M002", "Bob");

        memberService.addMember(m1);
        memberService.addMember(m2);

        members.addAll(memberService.getAllMembers()); // sync with borrow service

        System.out.println("📚 Initial Books:");
        bookService.listAllBooks();

        System.out.println("\n👥 Members:");
        memberService.listAllMembers();

        // Borrow a book
        System.out.println("\n➡ Alice borrows 'The Hobbit'");
        borrowService.borrowBook("M001", "B001", LocalDate.now());

        System.out.println("\n📚 Books after borrow:");
        bookService.listAllBooks();

        System.out.println("\n📖 Borrow Records:");
        borrowService.listAllBorrowRecords();

        // Bob tries to borrow same book (will be reserved)
        System.out.println("\n➡ Bob tries to borrow 'The Hobbit' (already borrowed)");
        borrowService.borrowBook("M002", "B001", LocalDate.now());

        // Alice returns the book
        System.out.println("\n➡ Alice returns 'The Hobbit'");
        borrowService.returnBook("M001", "B001", LocalDate.now());

        System.out.println("\n📖 Borrow Records after return:");
        borrowService.listAllBorrowRecords();

        // Show most borrowed books
        System.out.println("\n🔥 Most Borrowed Books:");
        bookService.getMostBorrowedBooks(2).forEach(System.out::println);

        // Save data to files (in project root for demo)
        fileIOService.saveBooks("books.txt", books);
        fileIOService.saveMembers("members.txt", members);
        fileIOService.saveBorrowRecords("borrows.txt", borrowService.getAllBorrowRecords());

        System.out.println("\n💾 Data saved to files (books.txt, members.txt, borrows.txt)");
    }
}
