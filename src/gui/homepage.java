package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

	public class homepage {
	

		    private final String[] books = {
		            "The Great Gatsby",
		            "To Kill a Mockingbird",
		            "1984",
		            "Pride and Prejudice",
		            "The Catcher in the Rye",
		            "The Hobbit",
		            "Moby Dick",
		            "War and Peace",
		            "The Odyssey",
		            "Crime and Punishment"
		    };

		    private DefaultListModel<String> listModel;
		    private static final String ADMIN_ICON_PATH = "C://Users//Student//Downloads//admin_icon.png";
		    private static final String BACKGROUND_PATH = "C:\\Users\\User\\Downloads\\lib.jpg";

		    public static void main(String[] args) {
		        SwingUtilities.invokeLater(homepage::new);
		    }

		    public homepage() {
		        JFrame frame = new JFrame("Bookkeeper - Home");
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		        JLabel bg = new JLabel(new ImageIcon(BACKGROUND_PATH));
		        bg.setLayout(new GridBagLayout());   // background with layout
		        frame.setContentPane(bg);

		        GridBagConstraints gbcFrame = new GridBagConstraints();

		        // -------------------------
		        // Admin button (top-right)
		        // -------------------------
		        JButton adminButton;
		        ImageIcon adminIcon = new ImageIcon(ADMIN_ICON_PATH);

		        if (adminIcon.getIconWidth() > 0) {  // valid image
		            Image img = adminIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		            adminButton = new JButton(new ImageIcon(img));
		        } else {  // fallback if image not found
		            adminButton = new JButton("Admin");
		            adminButton.setFont(new Font("Arial", Font.BOLD, 14));
		        }

		        adminButton.setToolTipText("Admin Login");
		        adminButton.setFocusPainted(false);

		        adminButton.addActionListener(e -> {
		            frame.dispose();
		            try {
		                loginn.main(new String[0]);
		            } catch (Throwable ex) {
		                JOptionPane.showMessageDialog(null,
		                        "Admin/login window could not be opened.",
		                        "Error", JOptionPane.ERROR_MESSAGE);
		            }
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

		        // -------------------------
		        // Central home panel
		        // -------------------------
		        JPanel homePanel = new JPanel(new GridBagLayout());
		        homePanel.setPreferredSize(new Dimension(800, 520));
		        homePanel.setBackground(new Color(80, 30, 20));
		        homePanel.setBorder(BorderFactory.createTitledBorder(
		                BorderFactory.createLineBorder(new Color(90, 60, 30), 2),
		                "Homepage",
		                0, 0,
		                new Font("Century Schoolbook", Font.BOLD, 22),
		                new Color(218, 165, 32)
		        ));

		        GridBagConstraints gbc = new GridBagConstraints();
		        gbc.insets = new Insets(12, 12, 12, 12);
		        gbc.fill = GridBagConstraints.HORIZONTAL;

		        // ---- Title with subtitle ----
		        JLabel titleLabel = new JLabel("Welcome to Bookkeeper", JLabel.CENTER);
		        titleLabel.setForeground(new Color(218, 165, 32));
		        titleLabel.setFont(new Font("Century Schoolbook", Font.BOLD, 26));

		        JLabel subtitleLabel = new JLabel("Tracking Books Made Easy", JLabel.CENTER);
		        subtitleLabel.setForeground(new Color(245, 222, 179));
		        subtitleLabel.setFont(new Font("Century Schoolbook", Font.ITALIC, 18));

		        JPanel titleFrame = new JPanel(new GridLayout(2, 1));
		        titleFrame.setPreferredSize(new Dimension(720, 90));
		        titleFrame.setOpaque(true);
		        titleFrame.setBackground(new Color(0, 0, 0, 120));
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

		        // ---- Search bar ----
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

		        // ---- Results area ----
		        listModel = new DefaultListModel<>();
		        JList<String> resultList = new JList<>(listModel);
		        resultList.setFont(new Font("Arial", Font.PLAIN, 14));
		        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		        JScrollPane scrollPane = new JScrollPane(resultList);
		        scrollPane.setPreferredSize(new Dimension(700, 150));

		        JPanel resultsPanel = new JPanel(new BorderLayout());
		        resultsPanel.setOpaque(false);
		        resultsPanel.setBorder(BorderFactory.createTitledBorder(
		                BorderFactory.createLineBorder(new Color(90, 60, 30), 1),
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

		        // ---- Exit button ----
		        JButton exitButton = new JButton("Exit");
		        exitButton.setBackground(Color.RED);
		        exitButton.setForeground(Color.WHITE);
		        exitButton.setFont(new Font("Arial", Font.BOLD, 18));
		        exitButton.setPreferredSize(new Dimension(160, 40));
		        exitButton.addActionListener(e -> System.exit(0));

		        gbc.gridx = 0;
		        gbc.gridy = 3;
		        gbc.gridwidth = 2;
		        homePanel.add(exitButton, gbc);

		        // Add home panel to frame
		        gbcFrame = new GridBagConstraints();
		        gbcFrame.gridx = 0;
		        gbcFrame.gridy = 1;
		        gbcFrame.weightx = 1.0;
		        gbcFrame.weighty = 1.0;
		        gbcFrame.anchor = GridBagConstraints.CENTER;
		        gbcFrame.insets = new Insets(20, 20, 20, 20);
		        bg.add(homePanel, gbcFrame);

		        // Actions
		        searchButton.addActionListener(e -> doSearch(frame, searchField));
		        searchField.addActionListener(e -> doSearch(frame, searchField));

		        frame.setVisible(true);
		    }

		    private void doSearch(JFrame parent, JTextField searchField) {
		        String query = searchField.getText().trim().toLowerCase();
		        listModel.clear();

		        if (query.isEmpty()) {
		            JOptionPane.showMessageDialog(parent, "Please enter a search term.", "Empty Search", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        for (String book : books) {
		            if (book.toLowerCase().contains(query)) {
		                listModel.addElement(book);
		            }
		        }

		        if (listModel.isEmpty()) {
		            listModel.addElement("No books found matching \"" + query + "\".");
		        }
		    }
		}
