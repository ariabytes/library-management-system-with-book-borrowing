package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import service_implementation.BookService;
import service_implementation.MemberService;
import service_implementation.BorrowService;
import service_implementation.FileIOService;
import model.Book;
import model.Member;

public class menu {
    private final BookService bookService;
    private final MemberService memberService;
    private final BorrowService borrowService;
    private final FileIOService fileIO;

    private JFrame frame;
    private JTable table;
    private JPanel contentPanel;
    private String currentView = "BOOKS"; // default

    public menu(BookService bookService, MemberService memberService, BorrowService borrowService, FileIOService fileIO) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.borrowService = borrowService;
        this.fileIO = fileIO;
        createAndShow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileIOService fio = new FileIOService();
            BookService bs = new BookService();
            MemberService ms = new MemberService(java.util.Collections.emptyList(), java.util.Collections.emptyList());
            BorrowService br = new BorrowService(java.util.Collections.emptyList(), java.util.Collections.emptyList());
            new menu(bs, ms, br, fio);
        });
    }

    private void createAndShow() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // === Background ===
        JLabel background = new JLabel();
        background.setOpaque(true);
        background.setBackground(new Color(30, 20, 20));
        background.setLayout(new GridBagLayout());
        frame.setContentPane(background);

        // === Content Panel ===
        contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setPreferredSize(new Dimension(1000, 600));
        contentPanel.setBackground(new Color(60, 20, 10, 220));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === Title ===
        JLabel lblTitle = new JLabel("Library Management System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 28));
        lblTitle.setForeground(new Color(255, 215, 0));
        contentPanel.add(lblTitle, BorderLayout.NORTH);

        // === Table ===
        setupTable();

        // === Main Buttons (Bottom) ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton btnReservation = createMenuButton("Reservation");
        JButton btnBooks = createMenuButton("Books");
        JButton btnMembers = createMenuButton("Members");

        JButton btnAdd = createActionButton("Add", new Color(0, 123, 255));
        JButton btnUpdate = createActionButton("Update", new Color(255, 193, 7));
        JButton btnDelete = createActionButton("Delete", new Color(220, 53, 69));

        buttonPanel.add(btnReservation);
        buttonPanel.add(btnBooks);
        buttonPanel.add(btnMembers);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // === Homepage Button (Top Right) ===
        JButton btnBackHome = new JButton("← Home");
        btnBackHome.setBackground(new Color(100, 100, 100));
        btnBackHome.setForeground(Color.WHITE);
        btnBackHome.setFont(new Font("Arial", Font.BOLD, 12));
        btnBackHome.setFocusPainted(false);
        btnBackHome.setPreferredSize(new Dimension(90, 30));

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        topRightPanel.setOpaque(false);
        topRightPanel.add(btnBackHome);

        // === Combine Panels ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        background.add(contentPanel, new GridBagConstraints());

        // === Button Actions ===
        btnReservation.addActionListener(e -> {
            currentView = "RESERVATION";
            showReservationData();
        });

        btnBooks.addActionListener(e -> {
            currentView = "BOOKS";
            showBookData();
        });

        btnMembers.addActionListener(e -> {
            currentView = "MEMBERS";
            showMemberData();
        });

        btnAdd.addActionListener(e -> addEntry());
        btnUpdate.addActionListener(e -> updateEntry());
        btnDelete.addActionListener(e -> deleteEntry());

        btnBackHome.addActionListener(e -> {
            frame.dispose();
            SwingUtilities.invokeLater(() ->
                new homepage(bookService, memberService, borrowService, fileIO)
            );
        });

        // === Default View ===
        showBookData();
        frame.setVisible(true);
    }


    private void setupTable() {
        table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(40, 25, 15));
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(255, 215, 0, 150));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(255, 215, 0, 80));
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(60, 40, 20));
        header.setOpaque(true);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(new Color(40, 25, 15));
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(new Color(40, 25, 15));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));

        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(130, 35));
        return btn;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    // === DATA VIEWS ===
    private void showReservationData() {
        String[] columns = {"Book ID", "Member ID", "Member Name"};
        Object[][] data = bookService.getAllReservations().toArray(new Object[0][]);
        updateTableModel(data, columns);
    }

    private void showBookData() {
        String[] columns = {"Book ID", "Title", "Author", "Category", "Status", "Borrowed", "Reservations"};
        
        // ✅ Reload books from file to get latest data
        List<Book> all = fileIO.loadBooks("books.txt");
        
        // ✅ Sort alphabetically by title
        all.sort((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
        
        // ✅ Update BookService's internal list to stay in sync
        bookService.getAllBooks().clear();
        bookService.getAllBooks().addAll(all);
        
        Object[][] data = new Object[all.size()][columns.length];
        for (int i = 0; i < all.size(); i++) {
            Book b = all.get(i);
            data[i][0] = b.getId();
            data[i][1] = b.getTitle();
            data[i][2] = b.getAuthor();
            data[i][3] = b.getCategory();
            data[i][4] = b.isAvailable() ? "Available" : "Borrowed";
            data[i][5] = b.getBorrowCount();
            data[i][6] = b.getReservationQueue() != null ? b.getReservationQueue().size() : 0;
        }
        updateTableModel(data, columns);
    }

    private void showMemberData() {
        String[] columns = {"Member ID", "Name", "Borrowed Books", "Reservations"};
        
        // ✅ Always reload from file to get the latest data
        List<Member> all = fileIO.loadMembers("members.txt");
        
        Object[][] data = new Object[all.size()][columns.length];
        for (int i = 0; i < all.size(); i++) {
            Member m = all.get(i);
            data[i][0] = m.getId();
            data[i][1] = m.getName();
            data[i][2] = m.getBorrowedBookIds() != null ? m.getBorrowedBookIds().size() : 0;
            data[i][3] = m.getReservationQueue() != null ? m.getReservationQueue().size() : 0;
        }
        updateTableModel(data, columns);
    }

    private void updateTableModel(Object[][] data, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table.setModel(model);
    }

    // === CRUD IMPLEMENTATION ===
    private void addEntry() {
        if(currentView.equals("BOOKS")) {
            JTextField idField = new JTextField();
            JTextField titleField = new JTextField();
            JTextField authorField = new JTextField();
            JTextField categoryField = new JTextField();
            Object[] message = {"Book ID:", idField, "Title:", titleField, "Author:", authorField, "Category:", categoryField};
            int option = JOptionPane.showConfirmDialog(frame, message, "Add New Book", JOptionPane.OK_CANCEL_OPTION);
            if(option == JOptionPane.OK_OPTION) {
                Book newBook = new Book(idField.getText().trim(), titleField.getText().trim(), authorField.getText().trim(), categoryField.getText().trim());
                bookService.addBook(newBook);
                fileIO.saveBooks("books.txt", bookService.getAllBooks());
                showBookData();
            }
        } else if(currentView.equals("MEMBERS")) {
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            Object[] message = {"Member ID:", idField, "Name:", nameField};
            int option = JOptionPane.showConfirmDialog(frame, message, "Add New Member", JOptionPane.OK_CANCEL_OPTION);
            if(option == JOptionPane.OK_OPTION) {
                Member newMember = new Member(idField.getText().trim(), nameField.getText().trim());
                memberService.addMember(newMember);
                fileIO.saveMembers("members.txt", memberService.getAllMembers());
                showMemberData();
            }
        } else if(currentView.equals("RESERVATION")) {
            JTextField bookIdField = new JTextField();
            JTextField memberIdField = new JTextField();
            JTextField memberNameField = new JTextField();
            Object[] message = {"Book ID:", bookIdField, "Member ID:", memberIdField, "Member Name:", memberNameField};
            int option = JOptionPane.showConfirmDialog(frame, message, "Add New Reservation", JOptionPane.OK_CANCEL_OPTION);
            if(option == JOptionPane.OK_OPTION) {
                String bookId = bookIdField.getText().trim();
                String memberId = memberIdField.getText().trim();
                String memberName = memberNameField.getText().trim();
                boolean success = bookService.addReservation(bookId, memberId, memberName);
                if(success) {
                	fileIO.saveBooksAlphabetically("books.txt", bookService.getAllBooks());
                    fileIO.saveMembers("members.txt", memberService.getAllMembers());
                    showReservationData();
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add reservation (duplicate or invalid book).");
                }
            }
        }
    }

    private void updateEntry() {
        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1) { 
            JOptionPane.showMessageDialog(frame, "Select a row first.", "No Selection", JOptionPane.WARNING_MESSAGE); 
            return; 
        }

        if(currentView.equals("BOOKS")) {
            String bookId = table.getValueAt(selectedRow, 0).toString();
            Book book = bookService.searchBookById(bookId);
            if(book == null) { 
                JOptionPane.showMessageDialog(frame, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE); 
                return; 
            }

            JTextField titleField = new JTextField(book.getTitle());
            JTextField authorField = new JTextField(book.getAuthor());
            JTextField categoryField = new JTextField(book.getCategory());
            String[] statusOptions = {"Available", "Borrowed"};
            JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
            statusCombo.setSelectedItem(book.isAvailable() ? "Available" : "Borrowed");

            Object[] message = {
                "Book ID: " + bookId + " (cannot be changed)", 
                "Title:", titleField, 
                "Author:", authorField, 
                "Category:", categoryField, 
                "Status:", statusCombo
            };
            
            int option = JOptionPane.showConfirmDialog(frame, message, "Update Book", JOptionPane.OK_CANCEL_OPTION);
            if(option == JOptionPane.OK_OPTION) {
                String newTitle = titleField.getText().trim();
                String newAuthor = authorField.getText().trim();
                String newCategory = categoryField.getText().trim();
                String newStatus = statusCombo.getSelectedItem().toString();

                // ✅ Handle status change: Borrowed → Available (Return)
                if(newStatus.equals("Available") && !book.isAvailable()) {
                    String borrowerId = book.getCurrentBorrowerId();
                    
                    // ✅ Load ALL members ONCE
                    List<Member> allMembers = fileIO.loadMembers("members.txt");
                    System.out.println("[DEBUG] Loaded " + allMembers.size() + " members from file");
                    
                    if(borrowerId != null) {
                        System.out.println("[DEBUG] Returning book " + bookId + " by member " + borrowerId);
                        
                        // ✅ Find and update member1 (returning borrower)
                        for (int i = 0; i < allMembers.size(); i++) {
                            if (allMembers.get(i).getId().equals(borrowerId)) {
                                Member borrower = allMembers.get(i);
                                borrower.returnBook(bookId);
                                allMembers.set(i, borrower);
                                System.out.println("[DEBUG] Member1 updated - borrowed: " + borrower.getBorrowedBookIds().size());
                                break;
                            }
                        }
                        
                        // ✅ Try to update borrow record
                        borrowService.returnBook(borrowerId, bookId, LocalDate.now());
                    }

                    // ✅ Check if there's a reservation queue
                    if(book.hasReservations()) {
                        String nextMemberId = book.pollNextReservation();
                        System.out.println("[DEBUG] Next member in queue: " + nextMemberId);
                        
                        // ✅ Find and update member2 (next borrower) in the SAME list
                        Member nextMember = null;
                        for (int i = 0; i < allMembers.size(); i++) {
                            if (allMembers.get(i).getId().equals(nextMemberId)) {
                                nextMember = allMembers.get(i);
                                nextMember.removeFromReservationQueue(bookId);
                                nextMember.borrowBook(bookId);
                                allMembers.set(i, nextMember);
                                System.out.println("[DEBUG] Member2 updated - borrowed: " + nextMember.getBorrowedBookIds().size() + 
                                                 ", reservations: " + nextMember.getReservationQueue().size());
                                break;
                            }
                        }
                        
                        if(nextMember != null) {
                            // ✅ Update book for new borrower
                            book.setAvailable(false);
                            book.setCurrentBorrower(nextMember.getId(), nextMember.getName());
                            book.incrementBorrowCount();
                            
                            // ✅ CRITICAL: Create borrow record for member2
                            try {
                                model.BorrowRecord newRecord = new model.BorrowRecord(
                                    bookId, 
                                    nextMember.getId(), 
                                    nextMember.getName(), 
                                    LocalDate.now()
                                );
                                
                                // Load all records, add new one, save all
                                List<model.BorrowRecord> allRecords = fileIO.loadBorrowRecords("borrow_records.txt");
                                allRecords.add(newRecord);
                                fileIO.saveBorrowRecords("borrow_records.txt", allRecords);
                                
                                System.out.println("[DEBUG] ✅ Borrow record created for " + nextMember.getName() + 
                                                 " (ID: " + nextMember.getId() + ") - Book: " + bookId);
                            } catch (Exception e) {
                                System.out.println("[ERROR] ❌ Failed to create borrow record: " + e.getMessage());
                                e.printStackTrace();
                            }
                            
                            JOptionPane.showMessageDialog(frame, 
                                "Book returned by previous borrower and automatically borrowed by " + nextMember.getName() + " (from reservation queue)!");
                        }
                    } else {
                        // No reservations, book is now available
                        book.setAvailable(true);
                        book.clearCurrentBorrower();
                        System.out.println("[DEBUG] No reservations, book is now available");
                    }
                    
                    // ✅ Save ALL members ONCE (includes both member1 and member2 updates)
                    fileIO.saveMembers("members.txt", allMembers);
                    System.out.println("[DEBUG] Saved " + allMembers.size() + " members to file");
                    
                } 
                else if(newStatus.equals("Borrowed") && book.isAvailable()) {
                    book.setAvailable(false);
                }

                // ✅ Update book metadata
                book.setTitle(newTitle);
                book.setAuthor(newAuthor);
                book.setCategory(newCategory);
                
                // ✅ Save the book changes
                fileIO.saveBooks("books.txt", bookService.getAllBooks());
                System.out.println("[DEBUG] Book saved - Status: " + (book.isAvailable() ? "Available" : "Borrowed"));

                // ✅ Refresh
                showBookData();
                
                JOptionPane.showMessageDialog(frame, "Book updated successfully!");
            }

        } else if(currentView.equals("MEMBERS")) {
            String memberId = table.getValueAt(selectedRow, 0).toString();
            
            // ✅ Reload member from file to get fresh data
            List<Member> allMembers = fileIO.loadMembers("members.txt");
            Member member = null;
            for (Member m : allMembers) {
                if (m.getId().equals(memberId)) {
                    member = m;
                    break;
                }
            }
            
            if(member == null) { 
                JOptionPane.showMessageDialog(frame, "Member not found.", "Error", JOptionPane.ERROR_MESSAGE); 
                return; 
            }

            JTextField nameField = new JTextField(member.getName());
            Object[] message = {"Member ID: " + memberId + " (cannot be changed)", "Name:", nameField};
            int opt = JOptionPane.showConfirmDialog(frame, message, "Update Member", JOptionPane.OK_CANCEL_OPTION);
            if(opt == JOptionPane.OK_OPTION) {
                member.setName(nameField.getText().trim());
                
                // Save the updated member
                allMembers = fileIO.loadMembers("members.txt");
                for (int i = 0; i < allMembers.size(); i++) {
                    if (allMembers.get(i).getId().equals(memberId)) {
                        allMembers.set(i, member);
                        break;
                    }
                }
                fileIO.saveMembers("members.txt", allMembers);
                
                showMemberData();
                JOptionPane.showMessageDialog(frame, "Member updated successfully!");
            }

        } else if(currentView.equals("RESERVATION")) {
            JOptionPane.showMessageDialog(frame, 
                "Reservations cannot be updated.\nYou can delete and create a new one if needed.", 
                "Update Not Available", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteEntry() {
        int selectedRow = table.getSelectedRow();
        if(selectedRow == -1) { JOptionPane.showMessageDialog(frame, "Select a row first."); return; }

        if(currentView.equals("BOOKS")) {
            String bookId = table.getValueAt(selectedRow, 0).toString();
            bookService.removeBook(bookId);
            fileIO.saveBooks("books.txt", bookService.getAllBooks());
            showBookData();
        } else if(currentView.equals("MEMBERS")) {
            String memberId = table.getValueAt(selectedRow, 0).toString();
            memberService.removeMember(memberId);
            fileIO.saveMembers("members.txt", memberService.getAllMembers());
            showMemberData();
        } else if(currentView.equals("RESERVATION")) {
            String bookId = table.getValueAt(selectedRow, 0).toString();      
            String memberId = table.getValueAt(selectedRow, 1).toString();
            Book book = bookService.searchBookById(bookId);
            if (book != null) {
                book.getReservationQueue().removeItem(memberId);
                fileIO.saveBooksAlphabetically("books.txt", bookService.getAllBooks());
                showReservationData();
            }
        }
    }
}

