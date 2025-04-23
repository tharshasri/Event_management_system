import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.UUID;

public class BudgetForm extends JFrame {
    public BudgetForm(String organizerId) {
        setTitle("Edit Budget");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel budgetLabel = new JLabel("Total Budget:");
        JTextField budgetField = new JTextField();
        JButton submitBtn = new JButton("Submit");
        JButton backBtn = new JButton("Back");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT total_budget FROM budget WHERE organizer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, organizerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                budgetField.setText(String.valueOf(rs.getDouble("total_budget")));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }

        submitBtn.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO budget (id, organizer_id, total_budget) VALUES (?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE total_budget = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, UUID.randomUUID().toString());
                pstmt.setString(2, organizerId);
                pstmt.setDouble(3, Double.parseDouble(budgetField.getText().trim()));
                pstmt.setDouble(4, Double.parseDouble(budgetField.getText().trim()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Budget updated successfully");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new ReportsFrame(organizerId);
            dispose();
        });

        panel.add(budgetLabel);
        panel.add(budgetField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(backBtn);
        panel.add(submitBtn);
        add(panel);
        setVisible(true);
    }
}