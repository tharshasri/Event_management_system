
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CancelTicket extends JFrame {
    public CancelTicket(String phoneNumber) {
        setTitle("Cancel Ticket");
        setSize(450, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 240, 240), getWidth(), getHeight(), new Color(240, 248, 255));
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

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(231, 76, 60)); 
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelBtn.setFocusPainted(false);

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(52, 152, 219)); 
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backBtn.setFocusPainted(false);

        cancelBtn.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String bookingId = bookingIdField.getText().trim();

            
                String verifyQuery = "SELECT event_id FROM bookings WHERE id = ? AND audience_id = ?";
                PreparedStatement verifyStmt = conn.prepareStatement(verifyQuery);
                verifyStmt.setString(1, bookingId);
                verifyStmt.setString(2, phoneNumber);
                ResultSet rs = verifyStmt.executeQuery();

                if (rs.next()) {
                    String eventId = rs.getString("event_id");

                    
                    String deleteQuery = "DELETE FROM bookings WHERE id = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                    deleteStmt.setString(1, bookingId);
                    deleteStmt.executeUpdate();

                    
                    String updateQuery = "UPDATE events SET tickets_booked = tickets_booked - 1 WHERE id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setString(1, eventId);
                    updateStmt.executeUpdate();

                    
                    String attendanceQuery = "DELETE FROM attendance WHERE booking_id = ?";
                    PreparedStatement attendanceStmt = conn.prepareStatement(attendanceQuery);
                    attendanceStmt.setString(1, bookingId);
                    attendanceStmt.executeUpdate();

                    JOptionPane.showMessageDialog(this, "🎟️ Ticket cancelled successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ Invalid booking ID. Please check and try again.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new AudienceDashboard(phoneNumber);
            dispose();
        });

        
        panel.add(bookingIdLabel);
        panel.add(bookingIdField);
        panel.add(backBtn);
        panel.add(cancelBtn);

        add(panel);
        setVisible(true);
    }
}
