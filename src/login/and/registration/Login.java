package login.and.registration;

import java.io.*;
import java.util.*;

/**
 *
 * @author RC_Student_lab
 */
public class Login {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellphone;
    
    private static final String USERS_FILE = "users.txt";

    // Default constructor
    public Login() {
        // Default constructor
    }
    
    // Constructor
    public Login(String firstName, String lastName, String username, String password, String cellphone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellphone = cellphone;
    }

    // ============== VALIDATION METHODS =================
    public boolean checkUserName() {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpperCase = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (!Character.isLetterOrDigit(c)) hasSpecialChar = true;
        }
        
        return hasUpperCase && hasDigit && hasSpecialChar;
    }

    public boolean checkCellPhoneNumber() {
        return cellphone != null && cellphone.matches("^\\+27\\d{9}$");
    }

    // ============== FILE STORAGE METHODS =================
    private void saveUserToFile() {
        try {
            File file = new File(USERS_FILE);
            FileWriter writer = new FileWriter(file, true); // append mode
            writer.write(firstName + "|" + lastName + "|" + username + "|" + password + "|" + cellphone + "\n");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }
    
    private boolean findUserInFile(String username, String password) {
        try {
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return false;
            }
            
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String storedUsername = parts[2];
                    String storedPassword = parts[3];
                    
                    if (storedUsername.equals(username) && storedPassword.equals(password)) {
                        // Set the user data for this instance
                        this.firstName = parts[0];
                        this.lastName = parts[1];
                        this.username = storedUsername;
                        this.password = storedPassword;
                        this.cellphone = parts[4];
                        scanner.close();
                        return true;
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
            System.err.println("Error reading users: " + e.getMessage());
        }
        return false;
    }

    // ============== REGISTRATION =================
    public String registerUser() {
        StringBuilder errorMessage = new StringBuilder();
        
        if (!checkUserName()) {
            errorMessage.append("Username is not correctly formatted, please ensure that your username contains an underscore and is no more than 5 characters in length.\n");
        }
        if (!checkPasswordComplexity()) {
            errorMessage.append("Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.\n");
        }
        if (!checkCellPhoneNumber()) {
            errorMessage.append("Cellphone number is not correctly formatted, please ensure that the cellphone number starts with +27 and is 12 characters long (including the +27).\n");
        }
        
        if (errorMessage.length() == 0) {
            saveUserToFile();
            return "User registered successfully!";
        } else {
            return errorMessage.toString().trim();
        }
    }

    // ============== LOGIN =================
    public boolean loginUser(String enteredUser, String enteredPass) {
        return findUserInFile(enteredUser, enteredPass);
    }

    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "Welcome " + firstName + " " + lastName + ", it is great to see you again!";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // ============== GETTER METHODS =================
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCellphone() {
        return cellphone;
    }
}