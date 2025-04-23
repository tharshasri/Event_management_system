import javax.swing.*;
import java.awt.*;

public class AddEventForm extends JFrame {
    public AddEventForm(String organizerId) {
        setTitle("Add Event");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField();
        JComboBox<String> categoryField = new JComboBox<>(new String[]{"Cultural", "Entertainment", "Business"});
        JTextField dateField = new JTextField();
        JTextField timeField = new JTextField();
        JTextField venueField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField seatsField = new JTextField();
        JButton submitBtn = new JButton("Submit");
        JButton backBtn = new JButton("Back");

        submitBtn.addActionListener(e -> {
            try {
                String dateText = dateField.getText().trim();
                String timeText = timeField.getText().trim();

                if (!dateText.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format");
                    return;
                }
                if (!timeText.matches("\\d{2}:\\d{2}:\\d{2}")) {
                    JOptionPane.showMessageDialog(this, "Time must be in HH:MM:SS format");
                    return;
                }

                Event event = new Event(
                    nameField.getText().trim(),
                    (String) categoryField.getSelectedItem(),
                    venueField.getText().trim(),
                    dateText,
                    timeText,
                    Integer.parseInt(seatsField.getText().trim()),
                    Double.parseDouble(priceField.getText().trim()),
                    organizerId
                );
                event.save();
                JOptionPane.showMessageDialog(this, "Event added successfully");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> {
            new OrganizerDashboard(organizerId);
            dispose();
        });

        panel.add(new JLabel("Event Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:MM:SS):"));
        panel.add(timeField);
        panel.add(new JLabel("Venue:"));
        panel.add(venueField);
        panel.add(new JLabel("Ticket Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Max Seats:"));
        panel.add(seatsField);
        panel.add(backBtn);
        panel.add(submitBtn);
        add(panel);
        setVisible(true);
    }
}