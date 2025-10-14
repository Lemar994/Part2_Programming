/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package login.and.registration;

import javax.swing.*;
import java.awt.*;

public class LoginAndRegistration extends JFrame {
    private JPanel loginPanel, registerPanel;
    private JTextField txtLoginUsername, txtRegFirstName, txtRegLastName, txtRegUsername, txtRegCell;
    private JPasswordField txtLoginPassword, txtRegPassword;
    private JButton btnLogin, btnGoToRegister, btnRegister, btnBackToLogin;
    private Login registeredUser;

    public LoginAndRegistration() {
        setTitle("Login & Registration");
        setSize(300, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new CardLayout());

        // ================== LOGIN PANEL ==================
        loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        txtLoginUsername = new JTextField(10);
        txtLoginPassword = new JPasswordField(10);

        btnLogin = new JButton("Login");
        btnGoToRegister = new JButton("Register");

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(txtLoginUsername);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(txtLoginPassword);
        loginPanel.add(btnLogin);
        loginPanel.add(btnGoToRegister);

        // ================== REGISTER PANEL ==================
        registerPanel = new JPanel();
        registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));

        txtRegFirstName = new JTextField(10);
        txtRegLastName = new JTextField(10);
        txtRegUsername = new JTextField(10);
        txtRegPassword = new JPasswordField(10);
        txtRegCell = new JTextField(10);

        btnRegister = new JButton("Register");
        btnBackToLogin = new JButton("Back to Login");

        registerPanel.add(new JLabel("First Name:"));
        registerPanel.add(txtRegFirstName);
        registerPanel.add(new JLabel("Last Name:"));
        registerPanel.add(txtRegLastName);
        registerPanel.add(new JLabel("Username:"));
        registerPanel.add(txtRegUsername);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(txtRegPassword);
        registerPanel.add(new JLabel("Cellphone (+27XXXXXXXXX):"));
        registerPanel.add(txtRegCell);
        registerPanel.add(btnRegister);
        registerPanel.add(btnBackToLogin);

        // ================== ACTIONS ==================
        btnLogin.addActionListener(e -> {
            String enteredUser = txtLoginUsername.getText();
            String enteredPass = new String(txtLoginPassword.getPassword());
            
            // Create a new Login instance to check credentials
            Login loginChecker = new Login();
            boolean loginSuccess = loginChecker.loginUser(enteredUser, enteredPass);
            
            String status = loginChecker.returnLoginStatus(loginSuccess);
            JOptionPane.showMessageDialog(this, status);
            
            // If login successful, launch ChatApplication
            if (loginSuccess) {
                // Launch the chat application
                ChatApplication.main(new String[]{});
                this.dispose(); // Close login window
            }
        });

        btnGoToRegister.addActionListener(e -> {
            switchToPanel(registerPanel);
        });

        btnBackToLogin.addActionListener(e -> {
            switchToPanel(loginPanel);
        });

        btnRegister.addActionListener(e -> {
            Login user = new Login(
                    txtRegFirstName.getText(),
                    txtRegLastName.getText(),
                    txtRegUsername.getText(),
                    new String(txtRegPassword.getPassword()),
                    txtRegCell.getText()
            );
            String result = user.registerUser();
            JOptionPane.showMessageDialog(this, result);

            if (result.equals("User registered successfully!")) {
                registeredUser = user;
                switchToPanel(loginPanel);
                
                // Clear registration fields
                txtRegFirstName.setText("");
                txtRegLastName.setText("");
                txtRegUsername.setText("");
                txtRegPassword.setText("");
                txtRegCell.setText("");
            }
        });

        // Add panels to frame
        add(loginPanel, "Login");
        add(registerPanel, "Register");

        switchToPanel(loginPanel); // Start with login
    }

    private void switchToPanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginAndRegistration().setVisible(true));
    }
}