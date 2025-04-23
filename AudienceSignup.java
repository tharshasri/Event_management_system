
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AudienceSignup extends JFrame {
    public AudienceSignup() {
        setTitle("Audience Sign Up");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(255, 250, 240)); 

        
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(new Color(50, 50, 50));

        JTextField nameField = new JTextField();
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        ageLabel.setForeground(new Color(50, 50, 50));

        JTextField ageField = new JTextField();
        ageField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        phoneLabel.setForeground(new Color(50, 50, 50));

        JTextField phoneField = new JTextField();
        phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        
        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBackground(new Color(40, 167, 69));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(255, 99, 71)); 
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        signupBtn.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "INSERT INTO audiences (phone_number, name, age) VALUES (?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, phoneField.getText().trim());
                pstmt.setString(2, nameField.getText().trim());
                pstmt.setInt(3, Integer.parseInt(ageField.getText().trim()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Sign up successful");
                new AudienceDashboard(phoneField.getText().trim());
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new AudienceAuth();
            dispose();
        });

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(ageLabel);
        panel.add(ageField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(backBtn);
        panel.add(signupBtn);

        add(panel);
        setVisible(true);
    }
}
