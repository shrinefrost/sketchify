package src.client;

import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private GameWindow gameWindow;
    private boolean isDrawer = false;

    public Client(String serverAddress, GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        try {
            socket = new Socket(serverAddress, 5000);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("ROLE:DRAWER")) {
                    isDrawer = true;
                    gameWindow.updateTurn(true);
                } else if (message.startsWith("ROLE:GUESSER")) {
                    isDrawer = false;
                    gameWindow.updateTurn(false);
                } 
                
                // ✅ Immediately request word selection when assigned as drawer
                if (isDrawer && message.startsWith("WORD_SELECTION:")) {
                    String[] words = message.replace("WORD_SELECTION:", "").split(",");
                    gameWindow.showWordSelection(words);
                } 
                
                else if (message.startsWith("DRAW:")) {
                    // Handle drawing data from other clients
                    gameWindow.processDrawingData(message.replace("DRAW:", ""));
                } else if (message.equals("CLEAR")) {
                    // Clear drawing board when the game resets
                    gameWindow.clearDrawingBoard();
                } else if (message.contains("has guessed the word!")) {
                    // Display when a player guesses correctly
                    gameWindow.displayMessage(message);
                    gameWindow.resetBoard(); // Reset board when a round ends
                } else {
                    gameWindow.processServerMessage(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }


    public void sendMessage(String message, String username) {
        if (!isDrawer) {
            out.println("GUESS:" + username + ":" + message.trim()); // Send guess to server properly
        } else {
            out.println("CHAT:" + username + ":" + message.trim()); // Ensure it's a regular chat message
        }
    }

    public void sendSelectedWord(String word) {
        if (isDrawer) {
            out.println("SELECTED_WORD:" + word.trim()); // Send chosen word to server (kept secret)
            gameWindow.startDrawing(); // Allow drawing after selection
        }
    }

    public void sendDrawingData(String drawData) {
        if (isDrawer) {
            out.println("DRAW:" + drawData); // Send drawing data to server
        }
    }

    public void clearBoard() {
        if (isDrawer) {
            out.println("CLEAR"); // Send clear command to server
        }
    }

    private void closeConnection() {
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
