import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class EditEventForm extends JFrame {
    public EditEventForm(String organizerId) {
        setTitle("Edit Event");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JComboBox<String> categoryField = new JComboBox<>(new String[]{"Cultural", "Entertainment", "Business"});
        JComboBox<String> eventField = new JComboBox<>();
        JTextField nameField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField venueField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField seatsField = new JTextField();
        JButton submitBtn = new JButton("Update");
        JButton backBtn = new JButton("Back");

        categoryField.addActionListener(e -> {
            eventField.removeAllItems();
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT id, name FROM events WHERE category = ? AND organizer_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, (String) categoryField.getSelectedItem());
                pstmt.setString(2, organizerId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    eventField.addItem(rs.getString("id") + ": " + rs.getString("name"));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        eventField.addActionListener(e -> {
            if (eventField.getSelectedItem() != null) {
                String eventId = eventField.getSelectedItem().toString().split(":")[0];
                try (Connection conn = DatabaseConnection.getConnection()) {
                    String query = "SELECT * FROM events WHERE id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, eventId);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        nameField.setText(rs.getString("name"));
                        dateField.setText(rs.getString("date"));
                        timeField.setText(rs.getString("time"));
                        venueField.setText(rs.getString("venue"));
                        priceField.setText(String.valueOf(rs.getDouble("ticket_price")));
                        seatsField.setText(String.valueOf(rs.getInt("max_seats")));
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        submitBtn.addActionListener(e -> {
            try {
                String dateText = dateField.getText().trim();
                String timeText = timeField.getText().trim();

                if (!dateText.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format");
                    return;
                }
                if (!timeText.matches("\\d{2}:\\d{2}:\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Time must be in HH:MM:SS format");
                    return;
                }

                if (eventField.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(this, "Please select an event");
                    return;
                }

                String eventId = eventField.getSelectedItem().toString().split(":")[0];
                Event event = new Event(
                    nameField.getText().trim(),
                    (String) categoryField.getSelectedItem(),
                    venueField.getText().trim(),
                    dateText,
                    timeText,
                    Integer.parseInt(seatsField.getText().trim()),
                    Double.parseDouble(priceField.getText().trim()),
                    organizerId
                );
                event.update();
                JOptionPane.showMessageDialog(this, "Event updated successfully");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new OrganizerDashboard(organizerId);
            dispose();
        });

        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Event:"));
        panel.add(eventField);
        panel.add(new JLabel("Event Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:MM:SS):"));
        panel.add(timeField);
        panel.add(new JLabel("Venue:"));
        panel.add(venueField);
        panel.add(new JLabel("Ticket Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Max Seats:"));
        panel.add(seatsField);
        panel.add(backBtn);
        panel.add(submitBtn);
        add(panel);
        setVisible(true);
    }
}