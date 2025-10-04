package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class loginn {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new loginn().createLoginScreen());
    }
    
    public void createLoginScreen() {
        // Create main frame
        JFrame frame = new JFrame("Log in");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen

        // Background image
        frame.setContentPane(new JLabel(new ImageIcon("C:\\Users\\User\\Downloads\\lib.jpg")));
        frame.setLayout(new GridBagLayout()); // keep layout for centering

        // Center login panel
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setPreferredSize(new Dimension(420, 320));
        loginPanel.setBackground(new Color(80, 30, 20));
        loginPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(90, 60, 30), 2),
                "Library Login",
                0, 0,
                new Font("Georgia", Font.BOLD, 20),
                new Color(218, 165, 32)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Welcome to Library Reservation");
        titleLabel.setForeground(new Color(218, 165, 32)); // Soft Gold
        titleLabel.setFont(new Font("Century Schoolbook", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Username & password labels with icons
        JLabel userLabel = new JLabel("Username:", new ImageIcon("user_icon.png"), JLabel.LEFT);
        userLabel.setForeground(new Color(245, 222, 179)); // Light Beige
        JTextField userField = new JTextField(15);

        JLabel passLabel = new JLabel("Password:", new ImageIcon("lock_icon.png"), JLabel.LEFT);
        passLabel.setForeground(new Color(245, 222, 179)); // Light Beige
        JPasswordField passField = new JPasswordField(15);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(60, 90, 150));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Action listener for login - CONNECTS TO MENU
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.equals("admin") && password.equals("1234")) {
                // Close login window
                frame.dispose();
                
                // Open menu window
                SwingUtilities.invokeLater(() -> new menu());
                
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Invalid Username or Password.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action listener for exit
        exitButton.addActionListener(e -> System.exit(0));

        // Enter key on username field moves to password field
        userField.addActionListener(e -> passField.requestFocusInWindow());

        // Enter key on password field submits login
        passField.addActionListener(e -> loginButton.doClick());

        // Add components to login panel
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

        // Add login panel to frame
        frame.add(loginPanel);
        frame.setVisible(true);
    }
}