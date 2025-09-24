package service_interface;

import java.util.*;
import model.Member;

public interface MemberServiceInterface {

    boolean addMember(Member member);

    boolean removeMember(String memberID);

    Member searchMemberByID(String memberID);

    List<Member> searchMembersByName(String name);

    List<Member> getAllMembers(); // returns a list of all members

    // Borrow and return management
    boolean borrowBook(String memberId, String bookId);

    boolean returnBook(String memberId, String bookId);

    // Reservation management
    boolean addReservation(String memberId, String bookId);

    boolean removeReservation(String memberId, String bookId);

    void listAllMembers(); // prints all members to console

}
