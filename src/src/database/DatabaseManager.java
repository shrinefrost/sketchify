package src.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/scribble_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Suraj2#pandey";

    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected!");
        } catch (SQLException e) {
        	System.out.println("not working properly");
            e.printStackTrace();
        }
    }

    public List<String> getRandomWords() {
        List<String> words = new ArrayList<>();
        String query = "SELECT word FROM words ORDER BY RAND() LIMIT 3";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                words.add(rs.getString("word"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }
}
