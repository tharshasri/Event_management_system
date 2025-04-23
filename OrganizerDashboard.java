import javax.swing.*;
import java.awt.*;

public class OrganizerDashboard extends JFrame {
    public OrganizerDashboard(String organizerId) {
        setTitle("Organizer Dashboard");
        setSize(700, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton addEventBtn = new JButton("Add Event");
        JButton editEventBtn = new JButton("Edit Events");
        JButton reportsBtn = new JButton("Reports");
        JButton backBtn = new JButton("Back");

        addEventBtn.setBackground(new Color(102, 205, 170));
        editEventBtn.setBackground(new Color(135, 206, 250));
        reportsBtn.setBackground(new Color(255, 160, 122));
        backBtn.setBackground(new Color(255, 182, 193));

        addEventBtn.addActionListener(e -> new AddEventForm(organizerId));
        editEventBtn.addActionListener(e -> new EditEventForm(organizerId));
        reportsBtn.addActionListener(e -> new ReportsFrame(organizerId));
        backBtn.addActionListener(e -> {
            new MainDashboard();
            dispose();
        });

        panel.add(addEventBtn);
        panel.add(editEventBtn);
        panel.add(reportsBtn);
        panel.add(backBtn);
        add(panel);
        setVisible(true);
    }
}