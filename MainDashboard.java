import javax.swing.*;
import java.awt.*;

public class MainDashboard extends JFrame {
    public MainDashboard() {
        setTitle("Event Management System");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(2, 1, 15, 15));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        ImageIcon organiserIcon = new ImageIcon("resources/organiser.png");
        Image image = organiserIcon.getImage(); 
        Image newimg = image.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH);
        organiserIcon = new ImageIcon(newimg);  
        ImageIcon audienceIcon = new ImageIcon("resources/audience.png");
        Image i = audienceIcon.getImage(); 
        Image n = i.getScaledInstance(80, 80,  java.awt.Image.SCALE_SMOOTH);
        audienceIcon = new ImageIcon(n);  
        JButton organizerBtn = new JButton("Organizer",organiserIcon);
        JButton audienceBtn = new JButton("Audience",audienceIcon);

        Font font = new Font("Arial", Font.BOLD, 16);
        organizerBtn.setFont(font);
        audienceBtn.setFont(font);
        organizerBtn.setBackground(new Color(135, 206, 250));
        audienceBtn.setBackground(new Color(144, 238, 144));

        organizerBtn.addActionListener(e -> {
            new OrganizerLogin();
            dispose();
        });

        audienceBtn.addActionListener(e -> {
            new AudienceAuth();
            dispose();
        });

        panel.add(organizerBtn);
        panel.add(audienceBtn);
        add(panel);
        setVisible(true);
    }
}