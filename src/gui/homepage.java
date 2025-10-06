package gui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

import service_implementation.BookService;
import service_implementation.MemberService;
import service_implementation.BorrowService;
import service_implementation.FileIOService;
import model.Book;
import model.Member;

public class homepage {
    private final BookService bookService;
    private final MemberService memberService;
    private final BorrowService borrowService;
    private final FileIOService fileIO;

    private DefaultListModel<String> listModel;
    private JList<String> resultList;
    private JButton borrowButton;
    private JButton reserveButton;

    private static final String BACKGROUND_PATH = "/GUI_BG.jpeg";
    private static final String ADMIN_ICON_PATH = "C://Users//Student//Downloads//admin_icon.png";

    public homepage(BookService bookService, MemberService memberService, BorrowService borrowService, FileIOService fileIO) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.borrowService = borrowService;
        this.fileIO = fileIO;
        createAndShow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileIOService fileIO = new FileIOService();

            // Load data from files
            List<Book> books = fileIO.loadBooks("books.txt");
            List<Member> members = fileIO.loadMembers("members.txt");

            BookService bookService = new BookService(books);
            MemberService memberService = new MemberService(members, books);
            BorrowService borrowService = new BorrowService(members, books);

            new homepage(bookService, memberService, borrowService, fileIO);
        });
    }

    private void createAndShow() {
        JFrame frame = new JFrame("Bookkeeper - Home");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Background
        ImageIcon bgIcon = new ImageIcon(getClass().getResource(BACKGROUND_PATH));
        Image bgImg = bgIcon.getImage().getScaledInstance(
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height,
                Image.SCALE_SMOOTH
        );
        JLabel bg = new JLabel(new ImageIcon(bgImg));
        bg.setLayout(new GridBagLayout());
        frame.setContentPane(bg);

        GridBagConstraints gbcFrame = new GridBagConstraints();

        // Admin button
        JButton adminButton;
        ImageIcon adminIcon = new ImageIcon(ADMIN_ICON_PATH);
        if (adminIcon.getIconWidth() > 0) {
            Image img = adminIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            adminButton = new JButton(new ImageIcon(img));
        } else {
            adminButton = new JButton("Admin");
            adminButton.setFont(new Font("Arial", Font.BOLD, 14));
        }

        adminButton.setToolTipText("Admin Login");
        adminButton.setFocusPainted(false);
        adminButton.addActionListener(e -> {
            frame.dispose();
            SwingUtilities.invokeLater(() -> new loginn(bookService, memberService, borrowService, fileIO));
        });

        JPanel adminPanel = new JPanel(new BorderLayout());
        adminPanel.setOpaque(false);
        adminPanel.add(adminButton, BorderLayout.CENTER);

        gbcFrame.gridx = 0;
        gbcFrame.gridy = 0;
        gbcFrame.weightx = 1.0;
        gbcFrame.anchor = GridBagConstraints.NORTHEAST;
        gbcFrame.insets = new Insets(10, 10, 0, 20);
        bg.add(adminPanel, gbcFrame);

        // Central panel
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setPreferredSize(new Dimension(800, 520));
        homePanel.setBackground(new Color(0, 0, 0, 120));
        homePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(218, 165, 32), 2),
                "Homepage",
                0, 0,
                new Font("Century Schoolbook", Font.BOLD, 22),
                new Color(218, 165, 32)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome to Bookkeeper", JLabel.CENTER);
        titleLabel.setForeground(new Color(218, 165, 32));
        titleLabel.setFont(new Font("Century Schoolbook", Font.BOLD, 26));

        JLabel subtitleLabel = new JLabel("Tracking Books Made Easy", JLabel.CENTER);
        subtitleLabel.setForeground(new Color(245, 222, 179));
        subtitleLabel.setFont(new Font("Century Schoolbook", Font.ITALIC, 18));

        JPanel titleFrame = new JPanel(new GridLayout(2, 1));
        titleFrame.setPreferredSize(new Dimension(720, 90));
        titleFrame.setOpaque(false);
        titleFrame.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(218, 165, 32), 2),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        titleFrame.add(titleLabel);
        titleFrame.add(subtitleLabel);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        homePanel.add(titleFrame, gbc);

        // Search
        JTextField searchField = new JTextField(28);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(60, 90, 150));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Arial", Font.BOLD, 16));
        searchButton.setPreferredSize(new Dimension(160, 40));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        homePanel.add(searchPanel, gbc);

        // Results
        listModel = new DefaultListModel<>();
        resultList = new JList<>(listModel);
        resultList.setFont(new Font("Arial", Font.PLAIN, 14));
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(resultList);
        scrollPane.setPreferredSize(new Dimension(700, 150));

        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setOpaque(false);
        resultsPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(218, 165, 32), 1),
                "Search Results",
                0, 0,
                new Font("Century Schoolbook", Font.BOLD, 18),
                new Color(218, 165, 32)
        ));
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        homePanel.add(resultsPanel, gbc);

        // Buttons
        borrowButton = new JButton("Borrow");
        reserveButton = new JButton("Reserve");
        JButton exitButton = new JButton("Exit");

        borrowButton.setBackground(new Color(0, 128, 0));
        borrowButton.setForeground(Color.WHITE);
        reserveButton.setBackground(new Color(218, 165, 32));
        reserveButton.setForeground(Color.BLACK);
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);

        borrowButton.setFont(new Font("Arial", Font.BOLD, 18));
        reserveButton.setFont(new Font("Arial", Font.BOLD, 18));
        exitButton.setFont(new Font("Arial", Font.BOLD, 18));

        borrowButton.setPreferredSize(new Dimension(160, 40));
        reserveButton.setPreferredSize(new Dimension(160, 40));
        exitButton.setPreferredSize(new Dimension(160, 40));

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.add(borrowButton);
        buttonPanel.add(reserveButton);
        buttonPanel.add(exitButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        homePanel.add(buttonPanel, gbc);

        gbcFrame = new GridBagConstraints();
        gbcFrame.gridx = 0;
        gbcFrame.gridy = 1;
        gbcFrame.weightx = 1.0;
        gbcFrame.weighty = 1.0;
        gbcFrame.anchor = GridBagConstraints.CENTER;
        gbcFrame.insets = new Insets(20, 20, 20, 20);
        bg.add(homePanel, gbcFrame);

        // Action listeners
        searchButton.addActionListener(e -> doSearch(searchField));
        searchField.addActionListener(e -> doSearch(searchField));

        resultList.addListSelectionListener(e -> updateReserveButtonState());
        borrowButton.addActionListener(e -> handleBorrow(frame));
        reserveButton.addActionListener(e -> handleReserve(frame));
        exitButton.addActionListener(e -> System.exit(0));

        reserveButton.setEnabled(false);
        frame.setVisible(true);
    }

    private void doSearch(JTextField searchField) {
        String query = searchField.getText().trim().toLowerCase();
        listModel.clear();

        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a search term.", "Empty Search", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Book> found = bookService.searchBooksByTitle(query);
        for (Book b : found) {
            String status = b.isAvailable() ? "Available" : "Borrowed";
            listModel.addElement(b.getTitle() + " — " + b.getAuthor() + " (ID: " + b.getId() + ") | Status: " + status);
        }

        if (listModel.isEmpty()) {
            listModel.addElement("No books found matching \"" + query + "\".");
        }
    }

    private void updateReserveButtonState() {
        int index = resultList.getSelectedIndex();
        if (index == -1) {
            reserveButton.setEnabled(false);
            return;
        }

        String selected = resultList.getSelectedValue();
        reserveButton.setEnabled(selected.contains("Borrowed"));
    }

    private void handleBorrow(JFrame parent) {
        int index = resultList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(parent, "Please select a book first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selected = resultList.getSelectedValue();
        String bookId = extractBookId(selected);
        Book book = bookService.searchBookById(bookId);

        if (book == null) {
            JOptionPane.showMessageDialog(parent, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!book.isAvailable()) {
            JOptionPane.showMessageDialog(parent, "This book is already borrowed. You may reserve it instead.", "Not Available", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String name = JOptionPane.showInputDialog(parent, "Enter your name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Member member = findMemberByName(name.trim());
        if (member == null) {
            member = new Member("M" + System.currentTimeMillis(), name.trim());
            boolean added = memberService.addMember(member);
            if (added) {
                fileIO.saveMembers("members.txt", memberService.getAllMembers());
                JOptionPane.showMessageDialog(parent, "New member added successfully.");
            }
        }

        boolean success = borrowService.borrowBook(member.getId(), bookId, LocalDate.now());
        if (success) {
            // ✅ IMPORTANT: update the member's borrowed books
            member.borrowBook(bookId);

            // Save updated files
            fileIO.saveMembers("members.txt", memberService.getAllMembers());
            fileIO.saveBooksAlphabetically("books.txt", bookService.getAllBooks());
            fileIO.saveBorrowRecords("borrow_records.txt", borrowService.getAllBorrowRecords());

            JOptionPane.showMessageDialog(parent, "Book successfully borrowed by " + member.getName() + " on " + LocalDate.now() + "!");
            doSearch(new JTextField(book.getTitle())); // refresh
        } else {
            JOptionPane.showMessageDialog(parent, "Borrow failed. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleReserve(JFrame parent) {
        int index = resultList.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(parent, "Please select a book first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String selected = resultList.getSelectedValue();
        String bookId = extractBookId(selected);
        Book book = bookService.searchBookById(bookId);

        if (book == null) {
            JOptionPane.showMessageDialog(parent, "Book not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = JOptionPane.showInputDialog(parent, "Enter your name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Name is required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Member member = findMemberByName(name.trim());
        if (member == null) {
            member = new Member("M" + System.currentTimeMillis(), name.trim());
            boolean added = memberService.addMember(member);
            if (added) {
                fileIO.saveMembers("members.txt", memberService.getAllMembers());
                JOptionPane.showMessageDialog(parent, "New member added successfully.");
            }
        }

        boolean ok = memberService.addReservation(member.getId(), bookId);
        if (ok) {
            fileIO.saveMembers("members.txt", memberService.getAllMembers());
            JOptionPane.showMessageDialog(parent, "You have been added to the reservation queue for this book!");
            doSearch(new JTextField(book.getTitle()));
        } else {
            JOptionPane.showMessageDialog(parent, "Failed to add reservation (maybe duplicate).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Member findMemberByName(String name) {
        if (name == null || name.trim().isEmpty()) return null;
        List<Member> matches = memberService.searchMembersByName(name.trim());
        if (matches == null || matches.isEmpty()) return null;
        for (Member m : matches) {
            if (m.getName().equalsIgnoreCase(name.trim())) return m;
        }
        return matches.get(0);
    }

    private String extractBookId(String entry) {
        try {
            int start = entry.indexOf("(ID: ") + 5;
            int end = entry.indexOf(")", start);
            return entry.substring(start, end);
        } catch (Exception e) {
            return "";
        }
    }
}
