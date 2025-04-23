import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ReportsFrame extends JFrame {
    public ReportsFrame(String organizerId) {
        setTitle("Reports");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton ticketInfoBtn = new JButton("Ticket Info");
        JButton revenueBtn = new JButton("Revenue Details");
        JButton audienceBtn = new JButton("Audience Info");
        JButton budgetBtn = new JButton("Edit Budget");
        JButton backBtn = new JButton("Back");

        ticketInfoBtn.setBackground(new Color(173, 216, 230));
        revenueBtn.setBackground(new Color(144, 238, 144));
        audienceBtn.setBackground(new Color(255, 182, 193));
        budgetBtn.setBackground(new Color(255, 228, 181));
        backBtn.setBackground(new Color(240, 128, 128));

        ticketInfoBtn.addActionListener(e -> showTicketInfo(organizerId));
        revenueBtn.addActionListener(e -> showRevenueDetails(organizerId));
        audienceBtn.addActionListener(e -> showAudienceInfo(organizerId));
        budgetBtn.addActionListener(e -> new BudgetForm(organizerId));
        backBtn.addActionListener(e -> {
            new OrganizerDashboard(organizerId);
            dispose();
        });

        panel.add(ticketInfoBtn);
        panel.add(revenueBtn);
        panel.add(audienceBtn);
        panel.add(budgetBtn);
        panel.add(backBtn);
        add(panel);
        setVisible(true);
    }

    private void showTicketInfo(String organizerId) {
        JFrame reportFrame = new JFrame("Ticket Info");
        reportFrame.setSize(800, 600);
        reportFrame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String[] categories = {"Cultural", "Entertainment", "Business"};
            StringBuilder report = new StringBuilder();
            for (String category : categories) {
                report.append(category).append(" Events:\n");
                report.append(String.format("%-30s %-15s %-15s %-15s %-15s\n", 
                    "Name", "Max Tickets", "Tickets Booked", "Attendance", "Attendance %"));
                String query = "SELECT name, max_seats, tickets_booked, attendance FROM events WHERE category = ? AND organizer_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, category);
                pstmt.setString(2, organizerId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    double attendancePercent = rs.getInt("max_seats") > 0 ? 
                        (rs.getInt("attendance") * 100.0 / rs.getInt("max_seats")) : 0;
                    report.append(String.format("%-30s %-15d %-15d %-15d %-15.2f%%\n",
                        rs.getString("name"),
                        rs.getInt("max_seats"),
                        rs.getInt("tickets_booked"),
                        rs.getInt("attendance"),
                        attendancePercent));
                }
                report.append("\n");
            }
            textArea.setText(report.toString());
        } catch (SQLException ex) {
            textArea.setText("Error: " + ex.getMessage());
        }

        reportFrame.add(scrollPane);
        reportFrame.setVisible(true);
    }

    private void showRevenueDetails(String organizerId) {
        JFrame reportFrame = new JFrame("Revenue Details");
        reportFrame.setSize(800, 600);
        reportFrame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String[] categories = {"Cultural", "Entertainment", "Business"};
            StringBuilder report = new StringBuilder();
            double totalRevenue = 0;
            double budget = 0;

            String budgetQuery = "SELECT total_budget FROM budget WHERE organizer_id = ?";
            PreparedStatement budgetStmt = conn.prepareStatement(budgetQuery);
            budgetStmt.setString(1, organizerId);
            ResultSet budgetRs = budgetStmt.executeQuery();
            if (budgetRs.next()) {
                budget = budgetRs.getDouble("total_budget");
            }

            for (String category : categories) {
                report.append(category).append(" Events:\n");
                report.append(String.format("%-30s %-15s %-15s %-15s\n", 
                    "Name", "Tickets Sold", "Ticket Price", "Revenue"));
                String query = "SELECT name, tickets_booked, ticket_price FROM events WHERE category = ? AND organizer_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, category);
                pstmt.setString(2, organizerId);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    double revenue = rs.getInt("tickets_booked") * rs.getDouble("ticket_price");
                    totalRevenue += revenue;
                    report.append(String.format("%-30s %-15d $%-14.2f $%-14.2f\n",
                        rs.getString("name"),
                        rs.getInt("tickets_booked"),
                        rs.getDouble("ticket_price"),
                        revenue));
                }
                report.append("\n");
            }

            report.append(String.format("%-30s $%-14.2f\n", "Total Revenue:", totalRevenue));
            report.append(String.format("%-30s $%-14.2f\n", "Total Budget:", budget));
            report.append(String.format("%-30s $%-14.2f\n", "Profit/Loss:", totalRevenue - budget));
            textArea.setText(report.toString());
        } catch (SQLException ex) {
            textArea.setText("Error: " + ex.getMessage());
        }

        reportFrame.add(scrollPane);
        reportFrame.setVisible(true);
    }

    private void showAudienceInfo(String organizerId) {
        JFrame reportFrame = new JFrame("Audience Info");
        reportFrame.setSize(800, 600);
        reportFrame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT a.name, a.phone_number, a.age, b.id, e.name as event_name " +
                         "FROM audiences a JOIN bookings b ON a.phone_number = b.audience_id " +
                         "JOIN events e ON b.event_id = e.id WHERE e.organizer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, organizerId);
            ResultSet rs = pstmt.executeQuery();
            
            StringBuilder report = new StringBuilder();
            report.append(String.format("%-20s %-15s %-10s %-20s %-30s\n", 
                "Name", "Phone", "Age", "Booking ID", "Event"));
            while (rs.next()) {
                report.append(String.format("%-20s %-15s %-10d %-20s %-30s\n",
                    rs.getString("name"),
                    rs.getString("phone_number"),
                    rs.getInt("age"),
                    rs.getString("id"),
                    rs.getString("event_name")));
            }
            textArea.setText(report.toString());
        } catch (SQLException ex) {
            textArea.setText("Error: " + ex.getMessage());
        }

        reportFrame.add(scrollPane);
        reportFrame.setVisible(true);
    }
}