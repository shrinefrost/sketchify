package src.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame {
    private DrawPanel canvas;
    private Client client;
    public JTextArea chatArea;
    private JTextField chatInput;
    public String username;
    public boolean isDrawer = false;
    private JLabel currentTurnLabel;

    public GameWindow(String username, String serverAddress) {
        this.username = username;
        setTitle("Scribble Game - " + username);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ✅ Initialize chat area to prevent NullPointerException
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        chatInput = new JTextField();

        // ✅ Initialize the client after UI elements
        client = new Client(serverAddress, this);

        // Chat panel setup
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(new JLabel("Chat"), BorderLayout.NORTH);
        chatPanel.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        chatPanel.add(chatInput, BorderLayout.SOUTH);
        chatPanel.setPreferredSize(new Dimension(200, 0));

        // ✅ Prevent Drawer from Sending Chat Messages
        chatInput.addActionListener(e -> {
            String message = chatInput.getText().trim();
            if (!message.isEmpty()) {
                if (!isDrawer) {
                    client.sendMessage("GUESS:" + message, username); // ✅ Corrected guess message format
                } else {
                    displayMessage("⚠️ You are the drawer! You cannot send chat messages.");
                }
                chatInput.setText("");
            }
        });

        // Toolbar for drawing tools
        JPanel toolPanel = new JPanel(new FlowLayout());

        JButton brushButton = new JButton("Brush");
        JButton eraserButton = new JButton("Eraser");
        JButton clearButton = new JButton("Clear");
        JSlider sizeSlider = new JSlider(1, 20, 5);

        String[] colorNames = {"Black", "Red", "Blue", "Green", "Yellow"};
        Color[] colors = {Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW};

        JComboBox<String> colorDropdown = new JComboBox<>(colorNames);
        colorDropdown.addActionListener(e -> {
            int selectedIndex = colorDropdown.getSelectedIndex();
            canvas.setBrushColor(colors[selectedIndex]);
        });

        brushButton.addActionListener(e -> canvas.setBrushMode());
        eraserButton.addActionListener(e -> canvas.setEraserMode());
        clearButton.addActionListener(e -> {
            canvas.clearCanvas();
            client.clearBoard(); // ✅ Send clear command to server
        });

        sizeSlider.addChangeListener(e -> canvas.setBrushSize(sizeSlider.getValue()));

        toolPanel.add(brushButton);
        toolPanel.add(eraserButton);
        toolPanel.add(clearButton);
        toolPanel.add(new JLabel("Size:"));
        toolPanel.add(sizeSlider);
        toolPanel.add(new JLabel("Color:"));
        toolPanel.add(colorDropdown);

        currentTurnLabel = new JLabel("Waiting for the game to start...", SwingConstants.CENTER);

        canvas = new DrawPanel(this); // Initialize the canvas

        add(currentTurnLabel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(chatPanel, BorderLayout.EAST);
        add(toolPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void processServerMessage(String message) {
        SwingUtilities.invokeLater(() -> { // Ensures UI updates safely
            try {
                if (message.startsWith("DRAW:")) {
                    String drawData = message.substring(5);
                    if (canvas != null) {
                        canvas.receiveDrawing(drawData);
                    }
                } else if (message.equals("CLEAR")) {
                    if (canvas != null) {
                        canvas.clearCanvas();
                    }
                } else if (message.startsWith("ROLE:DRAWER")) {
                    isDrawer = true;
                    updateTurn(true);
                } else if (message.startsWith("ROLE:GUESSER")) {
                    isDrawer = false;
                    updateTurn(false);
                } else if (message.startsWith("WORD_SELECTION:")) {
                    if (isDrawer) {
                        String[] words = message.substring(14).split(",");
                        showWordSelection(words);
                    }
                } else if (message.startsWith("SELECTED_WORD:")) {
                    if (isDrawer) {
                        String selectedWord = message.substring(14);
                        if (chatArea != null) {
                            chatArea.append("🎨 You selected: " + selectedWord + "\n");
                        }
                    }
                } else if (message.contains("has guessed the word correctly!")) {
                    displayMessage(message);
                    resetBoard(); // ✅ Reset board when a round ends
                } else if (message.startsWith("CHAT:")) {
                	System.out.println(message + "GameWindow");
                    String[] parts = message.split(":", 6);
                    if (parts.length == 6) {
                        String sender = parts[3];
                        String chatMessage = parts[5];
                        chatArea.append(sender + ": " + chatMessage + "\n");
                    }
                } else {
                    if (chatArea != null) {
                        chatArea.append("[Unknown]: " + message + "\n");
                    }
                }
            } catch (Exception e) {
                displayMessage("⚠️ Error processing server message: " + e.getMessage());
            }
        });
    }

    public void sendDrawing(String data) {
        client.sendDrawingData(data);
    }

    public void updateTurn(boolean drawer) {
        isDrawer = drawer;
        currentTurnLabel.setText(drawer ? "🎨 You are the drawer! Select a word." : "🤔 You are guessing!");
    }

    public void showWordSelection(String[] words) {
        if (!isDrawer) return;

        SwingUtilities.invokeLater(() -> {
            String chosenWord = (String) JOptionPane.showInputDialog(
                    this,
                    "Select a word to draw:",
                    "Word Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    words,
                    words[0]
            );

            if (chosenWord != null) {
                client.sendSelectedWord(chosenWord);
            }
        });
    }

    public void startDrawing() {
        currentTurnLabel.setText("🖌️ You are drawing...");
    }

    public void clearDrawingBoard() {
        canvas.clearCanvas();
    }

    public void resetBoard() {
        clearDrawingBoard();
        displayMessage("🛑 Round over! Waiting for the next round...");
    }

    public void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            if (chatArea != null) {
                chatArea.append(message + "\n");
            }
        });
    }

    public static void main(String[] args) {
        new GameWindow("Player1", "localhost");
    }

    public void processDrawingData(String drawData) {
        if (canvas != null) {
            canvas.receiveDrawing(drawData);
        }
    }
}
