package main;

import service_implementation.BookService;
import service_implementation.MemberService;
import service_implementation.BorrowService;
import service_implementation.FileIOService;
import model.Book;
import model.Member;

import gui.homepage;

import java.util.List;

public class Test_Main {
    public static void main(String[] args) {
        // Initialize file IO and load data
        FileIOService fileIO = new FileIOService();
        String membersFile = "members.txt";
        String booksFile = "books.txt";

        List<Member> members = fileIO.loadMembers(membersFile);
        List<Book> books = fileIO.loadBooks(booksFile);

        // Construct service layer objects used across the GUI
        BookService bookService = new BookService(books);
        // if BookService has a constructor that accepts existing books, you can adapt accordingly.
        // We'll add loaded books into the bookService list (works with the BookService provided earlier)
        for (Book b : books) {
            bookService.addBook(b);
        }

        MemberService memberService = new MemberService(members, books);
        BorrowService borrowService = new BorrowService(members, books);

        // Launch homepage GUI and pass services
        javax.swing.SwingUtilities.invokeLater(() -> new homepage(bookService, memberService, borrowService, fileIO));
    }
}
