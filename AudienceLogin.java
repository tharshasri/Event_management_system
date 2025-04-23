/*import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AudienceLogin extends JFrame {
    public AudienceLogin() {
        setTitle("Audience Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel phoneLabel = new JLabel("Phone Number:");
        JTextField phoneField = new JTextField();
        JButton loginBtn = new JButton("Login");
        JButton backBtn = new JButton("Back");

        loginBtn.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM audiences WHERE phone_number = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, phoneField.getText().trim());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    new AudienceDashboard(phoneField.getText().trim());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "User not found");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new AudienceAuth();
            dispose();
        });

        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(backBtn);
        panel.add(loginBtn);
        add(panel);
        setVisible(true);
    }
}*/
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AudienceLogin extends JFrame {
    public AudienceLogin() {
        setTitle("Audience Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 255, 250)); 

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setForeground(new Color(34, 34, 34)); 
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField phoneField = new JTextField();
        phoneField.setFont(new Font("Segoe UI", Font.PLAIN, 13)); 

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(0, 123, 255)); 
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14)); 

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(220, 53, 69));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14)); 

        
        loginBtn.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM audiences WHERE phone_number = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, phoneField.getText().trim());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    new AudienceDashboard(phoneField.getText().trim());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "User not found");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new AudienceAuth();
            dispose();
        });

        // Layout components
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(backBtn);
        panel.add(loginBtn);

        add(panel);
        setVisible(true);
    }
}
