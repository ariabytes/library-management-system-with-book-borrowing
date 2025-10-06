package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import service_implementation.BookService;
import service_implementation.MemberService;
import service_implementation.BorrowService;
import service_implementation.FileIOService;

public class loginn {
    private final BookService bookService;
    private final MemberService memberService;
    private final BorrowService borrowService;
    private final FileIOService fileIO;

    // Path relative to classpath (src/)
    private static final String BACKGROUND_RESOURCE = "/GUI_BG.jpeg";

    // Constructor used when opening login from homepage (with services passed)
    public loginn(BookService bookService, MemberService memberService, BorrowService borrowService, FileIOService fileIO) {
        this.bookService = bookService;
        this.memberService = memberService;
        this.borrowService = borrowService;
        this.fileIO = fileIO;
        createLoginScreen();
    }

    // Convenience main for direct run (will create empty default services)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FileIOService fio = new FileIOService();
            BookService bs = new BookService();
            MemberService ms = new MemberService(java.util.Collections.emptyList(), java.util.Collections.emptyList());
            BorrowService br = new BorrowService(java.util.Collections.emptyList(), java.util.Collections.emptyList());
            new loginn(bs, ms, br, fio);
        });
    }

    public void createLoginScreen() {
        JFrame frame = new JFrame("Log in");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen

        // --- Load background safely ---
        ImageIcon bgIcon = null;
        try {
            URL res = getClass().getResource(BACKGROUND_RESOURCE);
            if (res != null) {
                bgIcon = new ImageIcon(res);
            } else {
                // fallback to file system paths (helpful while running from IDE)
                bgIcon = new ImageIcon("src/GUI_BG.jpeg");
                if (bgIcon.getIconWidth() <= 0) {
                    bgIcon = new ImageIcon("GUI_BG.jpeg");
                }
            }
        } catch (Exception ex) {
            // final fallback to empty icon to avoid NPE
            bgIcon = new ImageIcon();
            System.err.println("Warning: failed to load background image: " + ex.getMessage());
        }

        // Scale background to screen size (keeps aspect stretching simple and predictable)
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Image scaledBg = null;
        if (bgIcon.getIconWidth() > 0 && bgIcon.getIconHeight() > 0) {
            scaledBg = bgIcon.getImage().getScaledInstance(screen.width, screen.height, Image.SCALE_SMOOTH);
        } else {
            // create a placeholder colored image if no bg available
            BufferedImage placeholder = new BufferedImage(screen.width, screen.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setPaint(new Color(40, 30, 20));
            g2.fillRect(0, 0, screen.width, screen.height);
            g2.dispose();
            scaledBg = placeholder;
        }

        JLabel backgroundLabel = new JLabel(new ImageIcon(scaledBg));
        backgroundLabel.setLayout(new GridBagLayout()); // place login panel centered
        frame.setContentPane(backgroundLabel);

        // --- Login panel ---
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setPreferredSize(new Dimension(420, 320));
        loginPanel.setBackground(new Color(0, 0, 0, 150)); // semi-transparent overlay
        loginPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(218, 165, 32), 2),
                "Library Login",
                0, 0,
                new Font("Georgia", Font.BOLD, 20),
                new Color(218, 165, 32)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Welcome to Library Reservation", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(218, 165, 32)); // Soft Gold
        titleLabel.setFont(new Font("Century Schoolbook", Font.BOLD, 20));

        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(new Color(245, 222, 179));
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(new Color(245, 222, 179));
        JPasswordField passField = new JPasswordField(15);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(60, 90, 150));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);

        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(new Color(220, 60, 60));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setFocusPainted(false);

        // Action listener for login - connects to menu with services
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.equals("admin") && password.equals("1234")) {
                frame.dispose();
                SwingUtilities.invokeLater(() -> new menu(bookService, memberService, borrowService, fileIO));
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Invalid Username or Password.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Enter key handlers
        userField.addActionListener(ev -> passField.requestFocusInWindow());
        passField.addActionListener(ev -> loginButton.doClick());

        // Layout inside login panel
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        loginPanel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        loginPanel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        loginPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        loginPanel.add(exitButton, gbc);

        // Add login panel centered on background
        backgroundLabel.add(loginPanel, new GridBagConstraints());

        frame.setVisible(true);
    }
}
