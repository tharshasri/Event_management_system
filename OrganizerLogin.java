
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class OrganizerLogin extends JFrame {
    public OrganizerLogin() {
        setTitle("Organizer Login");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(240, 248, 255)); // 🎨 NEW - Light pastel blue background

        JLabel idLabel = new JLabel("Login ID:");
        idLabel.setForeground(Color.DARK_GRAY); // 🎨 NEW - Dark gray label text
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 14)); // 🎨 NEW

        JTextField idField = new JTextField();
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // 🎨 NEW

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.DARK_GRAY); // 🎨 NEW
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14)); // 🎨 NEW

        JPasswordField passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 13)); // 🎨 NEW

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(0, 120, 215)); // 🎨 NEW - Blue background
        loginBtn.setForeground(Color.WHITE); // 🎨 NEW - White text
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14)); // 🎨 NEW

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(220, 53, 69)); // 🎨 NEW - Red background
        backBtn.setForeground(Color.WHITE); // 🎨 NEW - White text
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14)); // 🎨 NEW

        loginBtn.addActionListener(e -> {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String query = "SELECT * FROM organizers WHERE id = ? AND password = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, idField.getText().trim());
                pstmt.setString(2, new String(passField.getPassword()));
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    new OrganizerDashboard(idField.getText().trim());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new MainDashboard();
            dispose();
        });

        panel.add(idLabel);
        panel.add(idField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(backBtn);
        panel.add(loginBtn);
        add(panel);
        setVisible(true);
    }
}
