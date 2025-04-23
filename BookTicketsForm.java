
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.UUID;

public class BookTicketsForm extends JFrame {
    public BookTicketsForm(String phoneNumber) {
        setTitle("Book Tickets");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
        JPanel panel = new JPanel(new GridLayout(5, 2, 15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 243, 204), getWidth(), getHeight(), new Color(204, 229, 255));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = new Color(60, 60, 60);

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(labelFont);
        categoryLabel.setForeground(labelColor);

        JComboBox<String> categoryField = new JComboBox<>(new String[]{"Cultural", "Entertainment", "Business"});
        categoryField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel eventLabel = new JLabel("Event:");
        eventLabel.setFont(labelFont);
        eventLabel.setForeground(labelColor);

        JComboBox<String> eventField = new JComboBox<>();
        eventField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel ticketsLabel = new JLabel("Number of Tickets:");
        ticketsLabel.setFont(labelFont);
        ticketsLabel.setForeground(labelColor);

        JTextField ticketsField = new JTextField();
        ticketsField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton submitBtn = new JButton("Book");
        submitBtn.setBackground(new Color(46, 204, 113)); 
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.setFocusPainted(false);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(231, 76, 60)); 
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setFocusPainted(false);

        
        categoryField.addActionListener(e -> {
            eventField.removeAllItems();
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT id, name FROM events WHERE category = ? AND max_seats > tickets_booked";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, (String) categoryField.getSelectedItem());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    eventField.addItem(rs.getString("id") + ": " + rs.getString("name"));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        
        submitBtn.addActionListener(e -> {
            try {
                if (eventField.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Please select an event.");
                    return;
                }
                String eventId = eventField.getSelectedItem().toString().split(":")[0];
                int numTickets = Integer.parseInt(ticketsField.getText().trim());
                String category = (String) categoryField.getSelectedItem();

                try (Connection conn = DatabaseConnection.getConnection()) {
                    String checkQuery = "SELECT max_seats, tickets_booked FROM events WHERE id = ?";
                    PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                    checkStmt.setString(1, eventId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        int available = rs.getInt("max_seats") - rs.getInt("tickets_booked");
                        if (numTickets > available) {
                            JOptionPane.showMessageDialog(this, "Not enough seats available.");
                            return;
                        }
                    }

                    // Update event ticket count
                    String updateQuery = "UPDATE events SET tickets_booked = tickets_booked + ? WHERE id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setInt(1, numTickets);
                    updateStmt.setString(2, eventId);
                    updateStmt.executeUpdate();

                    // Insert booking records
                    String prefix = switch (category.toLowerCase()) {
                        case "cultural" -> "bc";
                        case "entertainment" -> "be";
                        case "business" -> "bb";
                        default -> "b";
                    };
                    for (int i = 0; i < numTickets; i++) {
                        String bookingId = prefix + UUID.randomUUID().toString().substring(0, 8);
                        String bookingQuery = "INSERT INTO bookings (id, event_id, audience_id) VALUES (?, ?, ?)";
                        PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery);
                        bookingStmt.setString(1, bookingId);
                        bookingStmt.setString(2, eventId);
                        bookingStmt.setString(3, phoneNumber);
                        bookingStmt.executeUpdate();
                    }
                    JOptionPane.showMessageDialog(this, "Tickets booked successfully!");
                    dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        
        backBtn.addActionListener(e -> {
            new AudienceDashboard(phoneNumber);
            dispose();
        });

        
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(eventLabel);
        panel.add(eventField);
        panel.add(ticketsLabel);
        panel.add(ticketsField);
        panel.add(backBtn);
        panel.add(submitBtn);

        add(panel);
        setVisible(true);
    }
}
