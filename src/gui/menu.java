package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class menu {
    private JFrame frame;
    private JTable table;
    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private String currentView = "BOOKS"; // default

    public menu() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // === BACKGROUND ===
        ImageIcon bgIcon = new ImageIcon("C:\\Users\\User\\Downloads\\lib.jpg");
        Image scaledImg = bgIcon.getImage().getScaledInstance(
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height,
                Image.SCALE_SMOOTH
        );
        JLabel background = new JLabel(new ImageIcon(scaledImg));
        background.setLayout(new GridBagLayout());
        frame.setContentPane(background);

        // === MAIN CONTENT PANEL ===
        contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setPreferredSize(new Dimension(1000, 600));
        contentPanel.setBackground(new Color(60, 20, 10, 220));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === TITLE ===
        JLabel lblTitle = new JLabel("Library Management System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 28));
        lblTitle.setForeground(new Color(255, 215, 0));
        contentPanel.add(lblTitle, BorderLayout.NORTH);

        // === TABLE AREA ===
        setupTable();

        // === BUTTONS PANEL ===
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

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Center the panel in background
        background.add(contentPanel, new GridBagConstraints());

        // === BUTTON ACTIONS ===
        btnReservation.addActionListener(e -> { currentView = "RESERVATION"; showReservationData(); });
        btnBooks.addActionListener(e -> { currentView = "BOOKS"; showBookData(); });
        btnMembers.addActionListener(e -> { currentView = "MEMBERS"; showMemberData(); });

        btnAdd.addActionListener(e -> addEntry());
        btnUpdate.addActionListener(e -> updateEntry());
        btnDelete.addActionListener(e -> deleteEntry());

        // Default view
        showBookData();

        frame.setVisible(true);
    }

    private void setupTable() {
        table = new JTable() {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isRowSelected(row)) {
                    c.setBackground(new Color(40, 25, 15));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(new Color(255, 215, 0, 150));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setForeground(Color.WHITE);
        table.setBackground(new Color(40, 25, 15));
        table.setOpaque(true);
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(255, 215, 0, 150));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(255, 215, 0, 80));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(2, 2));
        table.setFillsViewportHeight(true);

        // Custom cell renderer - OPAQUE
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                if (isSelected) {
                    label.setBackground(new Color(255, 215, 0, 150));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(new Color(40, 25, 15));
                    label.setForeground(Color.WHITE);
                }
                return label;
            }
        };
        
        table.setDefaultRenderer(Object.class, cellRenderer);

        // Configure table header - OPAQUE
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 15));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(60, 40, 20));
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        // Setup scroll pane - OPAQUE
        scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(new Color(40, 25, 15));
        scrollPane.getViewport().setOpaque(true);
        scrollPane.getViewport().setBackground(new Color(40, 25, 15));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 2));
        
        contentPanel.add(scrollPane, BorderLayout.CENTER);
    }

    // === BUTTON STYLES ===
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

    // === SAMPLE DATA MODELS ===
    private void showReservationData() {
        String[] columns = {"Reservation ID", "Book ID", "Member ID", "Date"};
        Object[][] data = {
                {"R001", "B101", "M501", "2025-10-01"},
                {"R002", "B102", "M502", "2025-10-02"}
        };
        updateTableModel(data, columns);
    }

    private void showBookData() {
        String[] columns = {"Book ID", "Title", "Author", "Category"};
        Object[][] data = {
                {"B101", "Java Programming", "John Doe", "Programming"},
                {"B102", "Data Structures", "Jane Smith", "Computer Science"}
        };
        updateTableModel(data, columns);
    }

    private void showMemberData() {
        String[] columns = {"Member ID", "Name", "Contact", "Address"};
        Object[][] data = {
                {"M501", "Alice", "09123456789", "Central City"},
                {"M502", "Bob", "09987654321", "Star City"}
        };
        updateTableModel(data, columns);
    }

    private void updateTableModel(Object[][] data, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        
        // Reapply renderer to all columns
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                
                if (isSelected) {
                    label.setBackground(new Color(255, 215, 0, 150));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setBackground(new Color(40, 25, 15));
                    label.setForeground(Color.WHITE);
                }
                return label;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        
        table.repaint();
    }

    // === CRUD ACTIONS ===
    private void addEntry() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int cols = model.getColumnCount();
        
        // Create add dialog
        JDialog addDialog = new JDialog(frame, "Add New Entry", true);
        addDialog.setSize(700, 500);
        addDialog.setLocationRelativeTo(frame);
        addDialog.setUndecorated(false);
        
        // Main container panel - fully opaque
        JPanel mainContainer = new JPanel(new BorderLayout(0, 0));
        mainContainer.setOpaque(true);
        mainContainer.setBackground(new Color(50, 35, 25));
        mainContainer.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(true);
        titlePanel.setBackground(new Color(60, 40, 20));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Add New Entry");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 215, 0));
        titlePanel.add(titleLabel);
        mainContainer.add(titlePanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(true);
        formPanel.setBackground(new Color(50, 35, 25));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);
        
        JTextField[] fields = new JTextField[cols];
        
        // Create form fields
        for (int i = 0; i < cols; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            
            JLabel label = new JLabel(model.getColumnName(i) + ":");
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setForeground(new Color(210, 190, 170));
            label.setOpaque(false);
            formPanel.add(label, gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            
            fields[i] = new JTextField();
            fields[i].setFont(new Font("Arial", Font.PLAIN, 16));
            fields[i].setBackground(new Color(90, 70, 55));
            fields[i].setForeground(Color.WHITE);
            fields[i].setCaretColor(new Color(255, 215, 0));
            fields[i].setOpaque(true);
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            formPanel.add(fields[i], gbc);
        }
        
        mainContainer.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(50, 35, 25));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 25, 15));
        
        JButton btnAdd = new JButton("Add");
        btnAdd.setBackground(new Color(0, 123, 255));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFont(new Font("Arial", Font.BOLD, 18));
        btnAdd.setFocusPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.setPreferredSize(new Dimension(150, 45));
        btnAdd.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 123, 255).darker(), 3),
            BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(108, 117, 125));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 18));
        btnCancel.setFocusPainted(false);
        btnCancel.setOpaque(true);
        btnCancel.setPreferredSize(new Dimension(150, 45));
        btnCancel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 117, 125).darker(), 3),
            BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));
        
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnCancel);
        mainContainer.add(buttonPanel, BorderLayout.SOUTH);
        
        addDialog.getContentPane().add(mainContainer);
        
        // Button actions
        btnAdd.addActionListener(e -> {
            Object[] newRow = new Object[cols];
            boolean allFilled = true;
            
            for (int i = 0; i < cols; i++) {
                if (fields[i].getText().trim().isEmpty()) {
                    allFilled = false;
                    JOptionPane.showMessageDialog(addDialog, 
                        "Please fill in all fields!", 
                        "Validation Error", 
                        JOptionPane.WARNING_MESSAGE);
                    break;
                }
                newRow[i] = fields[i].getText().trim();
            }
            
            if (allFilled) {
                model.addRow(newRow);
                table.repaint();
                addDialog.dispose();
            }
        });
        
        btnCancel.addActionListener(e -> addDialog.dispose());
        
        addDialog.setVisible(true);
    }

    private void updateEntry() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Select a row to update!");
            return;
        }
        
        // Create update dialog
        JDialog updateDialog = new JDialog(frame, "Update Entry", true);
        updateDialog.setSize(700, 500);
        updateDialog.setLocationRelativeTo(frame);
        updateDialog.setUndecorated(false);
        
        // Main container panel - fully opaque
        JPanel mainContainer = new JPanel(new BorderLayout(0, 0));
        mainContainer.setOpaque(true);
        mainContainer.setBackground(new Color(50, 35, 25));
        mainContainer.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));
        
        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(true);
        titlePanel.setBackground(new Color(60, 40, 20));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel("Update Entry");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(255, 215, 0));
        titlePanel.add(titleLabel);
        mainContainer.add(titlePanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(true);
        formPanel.setBackground(new Color(50, 35, 25));
        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);
        
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        JTextField[] fields = new JTextField[model.getColumnCount()];
        
        // Create form fields
        for (int i = 0; i < model.getColumnCount(); i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;
            
            JLabel label = new JLabel(model.getColumnName(i) + ":");
            label.setFont(new Font("Arial", Font.BOLD, 16));
            label.setForeground(new Color(210, 190, 170));
            label.setOpaque(false);
            formPanel.add(label, gbc);
            
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            
            fields[i] = new JTextField(model.getValueAt(row, i).toString());
            fields[i].setFont(new Font("Arial", Font.PLAIN, 16));
            fields[i].setBackground(new Color(90, 70, 55));
            fields[i].setForeground(Color.WHITE);
            fields[i].setCaretColor(new Color(255, 215, 0));
            fields[i].setOpaque(true);
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));
            formPanel.add(fields[i], gbc);
        }
        
        mainContainer.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(50, 35, 25));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 25, 15));
        
        JButton btnSave = new JButton("Save");
        btnSave.setBackground(new Color(40, 167, 69));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Arial", Font.BOLD, 18));
        btnSave.setFocusPainted(false);
        btnSave.setOpaque(true);
        btnSave.setPreferredSize(new Dimension(150, 45));
        btnSave.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 167, 69).darker(), 3),
            BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(108, 117, 125));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 18));
        btnCancel.setFocusPainted(false);
        btnCancel.setOpaque(true);
        btnCancel.setPreferredSize(new Dimension(150, 45));
        btnCancel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(108, 117, 125).darker(), 3),
            BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        mainContainer.add(buttonPanel, BorderLayout.SOUTH);
        
        updateDialog.getContentPane().add(mainContainer);
        
        // Button actions
        btnSave.addActionListener(e -> {
            for (int i = 0; i < fields.length; i++) {
                model.setValueAt(fields[i].getText(), row, i);
            }
            table.clearSelection();
            table.repaint();
            updateDialog.dispose();
        });
        
        btnCancel.addActionListener(e -> updateDialog.dispose());
        
        updateDialog.setVisible(true);
    }

    private void deleteEntry() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(frame, "Select a row to delete!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(frame,
                "Are you sure you want to delete this entry?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(row);
            table.repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new menu());
    }
}