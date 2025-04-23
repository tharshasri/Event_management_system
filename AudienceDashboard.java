import javax.swing.*;
import java.awt.*;

public class AudienceDashboard extends JFrame {
    public AudienceDashboard(String phoneNumber) {
        setTitle("Audience Panel");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 15, 15));
        panel.setBackground(new Color(255, 250, 205));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton bookBtn = new JButton("Book Tickets");
        JButton cancelBtn = new JButton("Cancel Tickets");
        JButton viewBtn = new JButton("View Bookings");
        JButton attendBtn = new JButton("Attend Event");
        JButton backBtn = new JButton("Back");

        Font font = new Font("Arial", Font.BOLD, 14);
        bookBtn.setFont(font);
        cancelBtn.setFont(font);
        viewBtn.setFont(font);
        attendBtn.setFont(font);
        backBtn.setFont(font);

        bookBtn.setBackground(new Color(173, 216, 230));
        cancelBtn.setBackground(new Color(255, 182, 193));
        viewBtn.setBackground(new Color(144, 238, 144));
        attendBtn.setBackground(new Color(255, 228, 181));
        backBtn.setBackground(new Color(240, 128, 128));

        bookBtn.addActionListener(e -> new BookTicketsForm(phoneNumber));
        cancelBtn.addActionListener(e -> new CancelTicket(phoneNumber));
        viewBtn.addActionListener(e -> new ViewBookings(phoneNumber));
        attendBtn.addActionListener(e -> new AttendEventForm(phoneNumber));
        backBtn.addActionListener(e -> {
            new AudienceAuth();
            dispose();
        });

        panel.add(bookBtn);
        panel.add(cancelBtn);
        panel.add(viewBtn);
        panel.add(attendBtn);
        panel.add(backBtn);
        add(panel);
        setVisible(true);
    }
}