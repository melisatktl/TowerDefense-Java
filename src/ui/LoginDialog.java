package ui;

import manager.UserManager;
import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private boolean loggedIn = false;
    
    public LoginDialog(JFrame parent) {
        super(parent, "Login", true);  
        setSize(300, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
       
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        formPanel.add(new JLabel("Name:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (UserManager.login(username, password)) {
            loggedIn = true;
            JOptionPane.showMessageDialog(this, "Welcome, " + username + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (UserManager.register(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now login.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public boolean isLoggedIn() {
        return loggedIn;
    }
}