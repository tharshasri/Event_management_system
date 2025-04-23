import java.sql.*;
import java.util.UUID;

public class Event {
    private String id;
    private String name;
    private String category;
    private String venue;
    private String date;
    private String time;
    private int maxSeats;
    private double ticketPrice;
    private String organizerId;
    private int ticketsBooked;
    private int attendance;

    public Event(String name, String category, String venue, String date, String time,
                 int maxSeats, double ticketPrice, String organizerId) {
        this.name = name;
        this.category = category;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.maxSeats = maxSeats;
        this.ticketPrice = ticketPrice;
        this.organizerId = organizerId;
        this.ticketsBooked = 0;
        this.attendance = 0;
        this.id = generateEventId(category);
    }

    private String generateEventId(String category) {
        String prefix = switch (category.toLowerCase()) {
            case "cultural" -> "ec";
            case "entertainment" -> "ee";
            case "business" -> "eb";
            default -> "e";
        };
        try (Connection conn = DatabaseConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM events WHERE category = '" + category + "'");
            rs.next();
            int count = rs.getInt(1) + 1;
            return prefix + count;
        } catch (SQLException e) {
            e.printStackTrace();
            return prefix + UUID.randomUUID().toString().substring(0, 8);
        }
    }

    public void save() throws SQLException {
        String query = "INSERT INTO events (id, name, category, venue, date, time, max_seats, ticket_price, organizer_id, tickets_booked, attendance) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, category);
            pstmt.setString(4, venue);
            pstmt.setString(5, date);
            pstmt.setString(6, time);
            pstmt.setInt(7, maxSeats);
            pstmt.setDouble(8, ticketPrice);
            pstmt.setString(9, organizerId);
            pstmt.setInt(10, ticketsBooked);
            pstmt.setInt(11, attendance);
            pstmt.executeUpdate();
        }
    }

    public void update() throws SQLException {
        String query = "UPDATE events SET name = ?, venue = ?, date = ?, time = ?, max_seats = ?, ticket_price = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, venue);
            pstmt.setString(3, date);
            pstmt.setString(4, time);
            pstmt.setInt(5, maxSeats);
            pstmt.setDouble(6, ticketPrice);
            pstmt.setString(7, id);
            pstmt.executeUpdate();
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getMaxSeats() {
        return maxSeats;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }
}