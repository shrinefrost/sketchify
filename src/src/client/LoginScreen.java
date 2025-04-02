package src.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JFrame {
    private JTextField usernameField;
    private JTextField serverAddressField;

    public LoginScreen() {
        setTitle("Scribble Game - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1, 10, 10));

        JLabel titleLabel = new JLabel("🎨 Welcome to Scribble Game!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel);

        // Username input
        JPanel userPanel = new JPanel();
        userPanel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        userPanel.add(usernameField);
        add(userPanel);

        // Server Address input
        JPanel serverPanel = new JPanel();
        serverPanel.add(new JLabel("Server IP:"));
        serverAddressField = new JTextField("localhost", 15);
        serverPanel.add(serverAddressField);
        add(serverPanel);

        // Login button
        JButton loginButton = new JButton("Join Game");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText().trim();
                String serverAddress = serverAddressField.getText().trim();

                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a username!");
                    return;
                }

                // Launch GameWindow with user details
                new GameWindow(username, serverAddress);
                dispose(); // Close login window
            }
        });
        add(loginButton);

        setLocationRelativeTo(null); // Center window
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginScreen();
    }
}
