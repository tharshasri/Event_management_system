
        
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AttendEventForm extends JFrame {
    public AttendEventForm(String phoneNumber) {
        setTitle("Attend Event");
        setSize(450, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
        JPanel panel = new JPanel(new GridLayout(4, 2, 15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(245, 243, 255), getWidth(), getHeight(), new Color(230, 255, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Color labelColor = new Color(60, 60, 60);

        JLabel bookingIdLabel = new JLabel("Booking ID:");
        bookingIdLabel.setFont(labelFont);
        bookingIdLabel.setForeground(labelColor);

        JTextField bookingIdField = new JTextField();
        bookingIdField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel eventIdLabel = new JLabel("Event ID:");
        eventIdLabel.setFont(labelFont);
        eventIdLabel.setForeground(labelColor);

        JTextField eventIdField = new JTextField();
        eventIdField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JButton attendBtn = new JButton("Attend");
        attendBtn.setBackground(new Color(46, 204, 113)); 
        attendBtn.setForeground(Color.WHITE);
        attendBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        attendBtn.setFocusPainted(false);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(52, 152, 219)); 
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setFocusPainted(false);

        attendBtn.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String bookingId = bookingIdField.getText().trim();
                String eventId = eventIdField.getText().trim();

                
                String checkQuery = "SELECT * FROM attendance WHERE booking_id = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
                checkStmt.setString(1, bookingId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, "⚠️ This booking has already attended the event");
                    return;
                }

                
                String verifyQuery = "SELECT * FROM bookings WHERE id = ? AND event_id = ? AND audience_id = ?";
                PreparedStatement verifyStmt = conn.prepareStatement(verifyQuery);
                verifyStmt.setString(1, bookingId);
                verifyStmt.setString(2, eventId);
                verifyStmt.setString(3, phoneNumber);
                rs = verifyStmt.executeQuery();
                if (rs.next()) {
                    
                    String attendanceQuery = "INSERT INTO attendance (booking_id, event_id, attended) VALUES (?, ?, ?)";
                    PreparedStatement attendanceStmt = conn.prepareStatement(attendanceQuery);
                    attendanceStmt.setString(1, bookingId);
                    attendanceStmt.setString(2, eventId);
                    attendanceStmt.setBoolean(3, true);
                    attendanceStmt.executeUpdate();

                
                    String updateQuery = "UPDATE events SET attendance = attendance + 1 WHERE id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setString(1, eventId);
                    updateStmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "🎉 Attendance recorded successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Invalid booking or event ID");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new AudienceDashboard(phoneNumber);
            dispose();
        });

        panel.add(bookingIdLabel);
        panel.add(bookingIdField);
        panel.add(eventIdLabel);
        panel.add(eventIdField);
        panel.add(backBtn);
        panel.add(attendBtn);
        add(panel);
        setVisible(true);
    }
}
