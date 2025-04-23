
import javax.swing.*;
import java.awt.*;

public class AudienceAuth extends JFrame {
    public AudienceAuth() {
        setTitle("Audience Authentication");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(230, 255, 250)); 

        
        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(60, 179, 113)); 
        loginBtn.setForeground(Color.WHITE); 
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBackground(new Color(100, 149, 237)); 
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(255, 99, 71));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // 🎯 Actions remain the same
        loginBtn.addActionListener(e -> new AudienceLogin());
        signupBtn.addActionListener(e -> new AudienceSignup());
        backBtn.addActionListener(e -> {
            new MainDashboard();
            dispose();
        });

        
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        panel.add(loginBtn);
        panel.add(signupBtn);
        panel.add(backBtn);
        panel.add(new JLabel(""));

        add(panel);
        setVisible(true);
    }
}
