package service_implementation;

import service_interface.MemberServiceInterface;
import model.*;
import utils.SinglyLinkedList_Imp;
import utils.HashMap_Imp;

import java.util.*;

public class MemberService implements MemberServiceInterface {
    private SinglyLinkedList_Imp<Member> members;
    private SinglyLinkedList_Imp<Book> books;
    private HashMap_Imp<String, Member> memberMap; // Fast member lookup
    private HashMap_Imp<String, Book> bookMap; // Fast book lookup
    private FileIOService fileIO;
    private final String membersFile = "members.txt";
    private final String booksFile = "books.txt";

    public MemberService(List<Member> membersList, List<Book> booksList) {
        this.fileIO = new FileIOService();
        
        // Initialize members with dual storage
        this.members = new SinglyLinkedList_Imp<>();
        this.memberMap = new HashMap_Imp<>();
        for (Member member : membersList) {
            members.addLast(member);
            memberMap.put(member.getId(), member);
        }
        
        // Initialize books with dual storage
        this.books = new SinglyLinkedList_Imp<>();
        this.bookMap = new HashMap_Imp<>();
        for (Book book : booksList) {
            books.addLast(book);
            bookMap.put(book.getId(), book);
        }
    }

    @Override
    public boolean addMember(Member member) {
        if (member.getId() == null || member.getId().isEmpty()) {
            member.setId(generateMemberId());
        }

        if (memberMap.containsKey(member.getId())) {
            return false;
        }
        members.addLast(member);
        memberMap.put(member.getId(), member);
        saveMembers();
        return true;
    }

    @Override
    public boolean removeMember(String memberID) {
        Member member = memberMap.get(memberID);
        if (member != null) {
            members.removeItem(member);
            memberMap.remove(memberID);
            saveMembers();
            return true;
        }
        return false;
    }

    @Override
    public Member searchMemberByID(String memberID) {
        return memberMap.get(memberID);
    }

    @Override
    public List<Member> searchMembersByName(String name) {
        List<Member> result = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(member);
            }
        }
        return result;
    }

    @Override
    public List<Member> getAllMembers() {
        List<Member> memberList = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            memberList.add(members.get(i));
        }
        return memberList;
    }

    @Override
    public boolean borrowBook(String memberId, String bookId) {
        Member member = memberMap.get(memberId);
        Book book = bookMap.get(bookId);

        if (member == null || book == null) return false;
        if (!book.isAvailable()) return false;

        member.borrowBook(bookId);
        book.setAvailable(false);
        book.setCurrentBorrower(memberId, member.getName());
        book.incrementBorrowCount();
        
        saveMembers();
        saveBooks();
        
        return true;
    }

    @Override
    public boolean returnBook(String memberId, String bookId) {
        Member member = memberMap.get(memberId);
        Book book = bookMap.get(bookId);

        if (member == null || book == null) return false;
        if (member.getBorrowedBookIds().indexOf(bookId) == -1) return false;

        member.returnBook(bookId);

        if (book.hasReservations()) {
            String nextMemberId = book.pollNextReservation();
            Member nextMember = memberMap.get(nextMemberId);
            if (nextMember != null) {
                nextMember.borrowBook(bookId);
                nextMember.removeFromReservationQueue(bookId);
                book.setCurrentBorrower(nextMemberId, nextMember.getName());
                book.incrementBorrowCount();
            }
        } else {
            book.setAvailable(true);
            book.clearCurrentBorrower();
        }
        
        saveMembers();
        saveBooks();
        
        return true;
    }

    @Override
    public boolean addReservation(String memberId, String bookId) {
        Member member = memberMap.get(memberId);
        Book book = bookMap.get(bookId);

        if (member == null || book == null) return false;

        member.addToReservationQueue(bookId);
        book.addToReservationQueue(memberId, member.getName());
        
        saveMembers();
        saveBooks();
        
        return true;
    }

    @Override
    public boolean removeReservation(String memberId, String bookId) {
        Member member = memberMap.get(memberId);
        Book book = bookMap.get(bookId);

        if (member == null || book == null) return false;

        member.removeFromReservationQueue(bookId);
        
        utils.Queue_Imp<String> tempIds = new utils.Queue_Imp<>();
        utils.Queue_Imp<String> tempNames = new utils.Queue_Imp<>();
        
        while (book.getReservationQueue() != null && !book.getReservationQueue().isEmpty()) {
            String id = book.getReservationQueue().dequeue();
            String name = book.getReservationQueueNames().dequeue();
            if (!id.equals(memberId)) {
                tempIds.enqueue(id); 
                tempNames.enqueue(name);
            }
        }
        
        while (!tempIds.isEmpty()) {
            book.addToReservationQueue(tempIds.dequeue(), tempNames.dequeue());
        }
        
        saveMembers();
        saveBooks();
        
        return true;
    }

    @Override
    public void listAllMembers() {
        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            for (int i = 0; i < members.size(); i++) {
                System.out.println(members.get(i));
            }
        }
    }

    private void saveMembers() {
        List<Member> memberList = new ArrayList<>();
        for (int i = 0; i < members.size(); i++) {
            memberList.add(members.get(i));
        }
        fileIO.saveMembers(membersFile, memberList);
    }

    private void saveBooks() {
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            bookList.add(books.get(i));
        }
        fileIO.saveBooksAlphabetically(booksFile, bookList);
    }

    public Member findMemberByName(String name) {
        List<Member> matches = searchMembersByName(name);
        if (!matches.isEmpty()) {
            return matches.get(0);
        }
        return null;
    }

    // ✅ Auto-generate M001, M002, etc.
    private String generateMemberId() {
        int maxId = 0;
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            String id = member.getId();
            if (id != null && id.startsWith("M")) {
                try {
                    int num = Integer.parseInt(id.substring(1));
                    if (num > maxId) maxId = num;
                } catch (NumberFormatException ignored) {}
            }
        }
        return String.format("M%03d", maxId + 1);
    }

    // ✅ Create member automatically if not existing
    public Member createMemberIfNotExists(String name) {
        Member existing = findMemberByName(name);
        if (existing != null) {
            return existing;
        }

        String newId = generateMemberId();
        Member newMember = new Member(newId, name);
        members.addLast(newMember);
        memberMap.put(newId, newMember);
        saveMembers();

        System.out.println("✅ New member created: " + name + " (ID: " + newId + ")");
        return newMember;
    }
}
