package service_implementation;

import service_interface.MemberServiceInterface;
import model.*;

import java.util.*;
import java.util.stream.Collectors;

public class MemberService implements MemberServiceInterface {
    private List<Member> members;
    private List<Book> books;

    public MemberService(List<Member> members, List<Book> books) {
        this.members = members;
        this.books = books;
    }

    @Override
    public boolean addMember(Member member) {
        if (searchMemberByID(member.getId()) == null) {
            members.add(member);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeMember(String memberID) {
        Member member = searchMemberByID(memberID);
        if (member != null) {
            members.remove(member);
            return true;
        }
        return false;
    }

    @Override
    public Member searchMemberByID(String memberID) {
        return members.stream()
                .filter(m -> m.getId().equalsIgnoreCase(memberID))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Member> searchMembersByName(String name) {
        return members.stream()
                .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> getAllMembers() {
        return new ArrayList<>(members);
    }

    @Override
    public boolean borrowBook(String memberId, String bookId) {
        Member member = searchMemberByID(memberId);
        Book book = findBook(bookId);

        if (member == null || book == null) return false;
        if (!book.isAvailable()) return false;

        member.borrowBook(bookId);
        book.setAvailable(false);
        book.incrementBorrowCount();
        return true;
    }

    @Override
    public boolean returnBook(String memberId, String bookId) {
        Member member = searchMemberByID(memberId);
        Book book = findBook(bookId);

        if (member == null || book == null) return false;
        if (!member.getBorrowedBookIds().contains(bookId)) return false;

        member.returnBook(bookId);

        // If reserved, assign to next member instead of marking available
        if (book.hasReservations()) {
            String nextMemberId = book.pollNextReservation();
            Member nextMember = searchMemberByID(nextMemberId);
            if (nextMember != null) {
                nextMember.borrowBook(bookId);
                nextMember.removeFromReservationQueue(bookId);
                book.incrementBorrowCount();
            }
        } else {
            book.setAvailable(true);
        }
        return true;
    }

    @Override
    public boolean addReservation(String memberId, String bookId) {
        Member member = searchMemberByID(memberId);
        Book book = findBook(bookId);

        if (member == null || book == null) return false;

        member.addToReservationQueue(bookId);
        book.addToReservationQueue(memberId);
        return true;
    }

    @Override
    public boolean removeReservation(String memberId, String bookId) {
        Member member = searchMemberByID(memberId);
        Book book = findBook(bookId);

        if (member == null || book == null) return false;

        member.removeFromReservationQueue(bookId);
        book.removeFromReservationQueue(memberId);
        return true;
    }

    @Override
    public void listAllMembers() {
        if (members.isEmpty()) {
            System.out.println("No members found.");
        } else {
            members.forEach(System.out::println);
        }
    }

    // Helper
    private Book findBook(String bookId) {
        return books.stream()
                .filter(b -> b.getId().equalsIgnoreCase(bookId))
                .findFirst()
                .orElse(null);
    }
}
