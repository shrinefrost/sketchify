package src.server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class Server {
    private static final int PORT = 5000;
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static ClientHandler drawer;
    private static Connection connection;
    protected static String selectedWord = ""; // Stores the chosen word

    public static void main(String[] args) {
        System.out.println("🎨 Scribble Server started on port " + PORT);

        // Initialize database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/scribble_db", "root", "Suraj2#pandey");
            System.out.println("✅ Database connected!");
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            return; // Stop the server if DB connection fails
        }

        // Start server socket
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler client = new ClientHandler(clientSocket);

                synchronized (clients) {
                    clients.add(client);
                }

                new Thread(client).start();

                // Assign a drawer if none exists
                synchronized (clients) {
                    if (drawer == null && !clients.isEmpty()) {
                        assignDrawer(client);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }

    // Assigns a drawer and sends word selection (kept secret from guessers)
    private static void assignDrawer(ClientHandler client) {
        drawer = client;
        List<String> words = getRandomWords();
        if (!words.isEmpty()) {
            String wordSelectionMessage = "WORD_SELECTION:" + String.join(",", words);
            drawer.sendMessage("ROLE:DRAWER");
            drawer.sendMessage(wordSelectionMessage); // Sent only to drawer
        }
    }

    // Fetch 3 random words from the database
    private static List<String> getRandomWords() {
        List<String> words = new ArrayList<>();
        String query = "SELECT word FROM words ORDER BY RAND() LIMIT 3";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                words.add(rs.getString("word"));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching words: " + e.getMessage());
        }
        return words;
    }

    // ✅ Broadcasts messages to ALL clients properly
    public static void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // ✅ Broadcasts drawing data separately
    public static void broadcastDraw(String drawData, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage("DRAW:" + drawData);
                }
            }
        }
    }

    // ✅ Checks if a player's guess is correct
    public static void checkGuess(String guessedWord, String sender) {
        System.out.println("🔎 Checking guess: " + guessedWord);

        if (guessedWord.equalsIgnoreCase(selectedWord)) {
        	System.out.println("matched");
            broadcastMessage("CHAT:🎉 " + sender + " has guessed the word correctly!");
            broadcastMessage("CHAT:📢 The correct word was: " + selectedWord);

            // Select a new random drawer
            selectNewDrawer();
        } 
    }

    // ✅ Selects a new drawer randomly
    private static void selectNewDrawer() {
        if (clients.isEmpty()) return;

        Random rand = new Random();
        ClientHandler newDrawer = clients.get(rand.nextInt(clients.size()));

        drawer = newDrawer;
        broadcastMessage("CHAT:🖌️ " + drawer.getUsername() + " is the new drawer!");

        // Send word selection to new drawer
        List<String> words = getRandomWords();
        if (!words.isEmpty()) {
            drawer.sendMessage("ROLE:DRAWER");
            drawer.sendMessage("WORD_SELECTION:" + String.join(",", words));
        }
    }

    // ✅ Remove disconnected clients safely
    public static void removeClient(ClientHandler client) {
        synchronized (clients) {
            clients.remove(client);
        }

        if (client == drawer) {
            drawer = null; // Reset drawer if they disconnect
            synchronized (clients) {
                if (!clients.isEmpty()) {
                    assignDrawer(clients.get(0)); // Assign new drawer if players remain
                }
            }
        }
    }
}

// ============================
// Client Handler Class
// ============================
class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Read username from client
//            out.println("Enter your username:");
//            username = in.readLine();

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("SELECTED_WORD:")) {
                    Server.selectedWord = message.substring(14);
                    System.out.println("📌 Drawer chose the word (SECRET): " + Server.selectedWord);
                } 
//                else if (message.startsWith("CHAT:")) {
//                	System.out.println("📢 Broadcasting chat: " + message + "chat");
//                    Server.checkGuess(message.substring(6).trim(), this);}
                
                 else if (message.startsWith("DRAW:") || message.equals("CLEAR")) {
                    Server.broadcastDraw(message, this); // ✅ Send drawing data
                } else if (message.startsWith("GUESS:")) {
                    System.out.println("📢 Broadcasting chat: " + message + "from guess");
                    String[] parts = message.split(":", 4);
                    if (parts.length == 4) {
                        String chatMessage = parts[3].trim();
                        Server.checkGuess(chatMessage,parts[1]);
                    }
                    // ✅ Ensure chat messages are broadcast correctly
                    Server.broadcastMessage("CHAT:" + username + ": " + message.substring(5));
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Client disconnected: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    private void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
            Server.removeClient(this);
        } catch (IOException e) {
            System.err.println("❌ Error closing connection: " + e.getMessage());
        }
    }
}
