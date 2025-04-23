import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewBookings extends JFrame {
    public ViewBookings(String phoneNumber) {
        setTitle("View Bookings");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String[] categories = {"Cultural", "Entertainment", "Business"};
            StringBuilder report = new StringBuilder();
            for (String category : categories) {
                report.append(category).append(" Bookings:\n");
                report.append(String.format("%-20s %-10s %-30s %-30s %-15s %-15s\n", 
                    "Booking ID", "Event ID", "Event Name", "Venue", "Date", "Time"));
                String query = "SELECT b.id, e.id as event_id, e.name, e.venue, e.date, e.time " +
                             "FROM bookings b JOIN events e ON b.event_id = e.id " +
                             "WHERE b.audience_id = ? AND e.category = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, phoneNumber);
                pstmt.setString(2, category);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    report.append(String.format("%-20s %-10s %-30s %-30s %-15s %-15s\n",
                        rs.getString("id"),
                        rs.getString("event_id"),
                        rs.getString("name"),
                        rs.getString("venue"),
                        rs.getString("date"),
                        rs.getString("time")));
                }
                report.append("\n");
            }
            textArea.setText(report.toString());
        } catch (SQLException ex) {
            textArea.setText("Error: " + ex.getMessage());
        }

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            new AudienceDashboard(phoneNumber);
            dispose();
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backBtn, BorderLayout.SOUTH);
        add(panel);
        setVisible(true);
    }
}