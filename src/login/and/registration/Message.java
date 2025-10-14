package login.and.registration;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.JOptionPane;

public class Message {
    private String messageID;
    private String messageHash;
    private String message;
    private String recipientCellphone;
    private static int totalMessages = 0;
    private static int messageNumber = 0;
    private static List<Message> sentMessages = new ArrayList<>();
    
    private static final String MESSAGES_FILE = "messages.json";
    private static final Random random = new Random();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Message() {
        initializeMessagesFile();
    }

    public Message(String message, String recipientCellphone) {
        this.message = message;
        this.recipientCellphone = recipientCellphone;
        initializeMessagesFile();
    }

    private void initializeMessagesFile() {
        try {
            File file = new File(MESSAGES_FILE);
            if (!file.exists()) {
                Files.write(Paths.get(MESSAGES_FILE), "{\"messages\":[]}".getBytes());
            }
        } catch (IOException e) {
            System.out.println("Error initializing messages file: " + e.getMessage());
        }
    }

    // ========== REQUIRED METHODS ==========

    /**
     * This method ensures that the message ID is not more than ten characters.
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() == 10;
    }

    /**
     * This method ensures that the recipient cell number is correctly formatted
     */
    public int checkRecipientCell() {
        if (recipientCellphone == null) return 0;
        if (recipientCellphone.startsWith("+27") && recipientCellphone.length() == 12) {
            return 1; // Valid
        } else {
            return 0; // Invalid
        }
    }

    /**
     * This method creates and returns the Message Hash in format: 00:0:HITHANKS
     */
    public String createMessageHash() {
        if (messageID == null) {
            createMessageID();
        }
        
        // Get first two numbers of message ID
        String firstTwo = messageID.substring(0, 2);
        
        // Get message number (incremented for each message)
        messageNumber++;
        
        // Get first and last words of message
        String[] words = message.split(" ");
        String firstWord = words.length > 0 ? words[0].toUpperCase() : "";
        String lastWord = words.length > 1 ? words[words.length - 1].toUpperCase() : firstWord;
        
        this.messageHash = firstTwo + ":" + messageNumber + ":" + firstWord + lastWord;
        return this.messageHash;
    }

    /**
     * This method should allow the user to choose if they want to send, store, or disregard the message.
     */
    public String SentMessage(String firstName, String lastName) {
        String[] options = {"Send Message", "Disregard Message", "Store Message to send later"};
        int choice = JOptionPane.showOptionDialog(null, 
            "Choose action for message:\n" + message, 
            "Message Action", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, options, options[0]);
        
        switch (choice) {
            case 0: // Send Message
                return sendMessage(firstName, lastName);
            case 1: // Disregard Message
                return "Press 0 to delete message.";
            case 2: // Store Message
                storeMessage();
                return "Message successfully stored.";
            default:
                return "No action taken.";
        }
    }

    /**
     * This method returns a list of all the messages sent while the program is running.
     */
    public String printMessages() {
        StringBuilder result = new StringBuilder("=== ALL SENT MESSAGES ===\n");
        for (int i = 0; i < sentMessages.size(); i++) {
            Message msg = sentMessages.get(i);
            result.append("Message ").append(i + 1).append(":\n")
                  .append("MessageID: ").append(msg.getMessageID()).append("\n")
                  .append("Message Hash: ").append(msg.getMessageHash()).append("\n")
                  .append("Recipient: ").append(msg.getRecipientCellphone()).append("\n")
                  .append("Message: ").append(msg.getMessage()).append("\n")
                  .append("---\n");
        }
        return result.toString();
    }

    /**
     * This method returns the total number of messages sent.
     */
    public int returnTotalMessages() {
        return totalMessages;
    }

    // ========== SUPPORTING METHODS ==========

    public String createMessageID() {
        long id = 1000000000L + random.nextInt(900000000);
        this.messageID = String.valueOf(id);
        return this.messageID;
    }

    public String checkMessageLength() {
        if (message == null || message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }

    public String checkRecipientCellWithMessage() {
        int result = checkRecipientCell();
        if (result == 1) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String sendMessage(String firstName, String lastName) {
        // Check message length
        String lengthCheck = checkMessageLength();
        if (!lengthCheck.equals("Message ready to send.")) {
            return lengthCheck;
        }
        
        // Check recipient cell
        String recipientCheck = checkRecipientCellWithMessage();
        if (!recipientCheck.equals("Cell phone number successfully captured.")) {
            return recipientCheck;
        }
        
        createMessageID();
        createMessageHash();
        
        if (saveMessageToJSON(firstName, lastName)) {
            totalMessages++;
            sentMessages.add(this);
            return "Message successfully sent.";
        } else {
            return "Failed to save message.";
        }
    }

    public void storeMessage() {
        // Store message in JSON file
        saveMessageToJSON("Stored", "User");
        JOptionPane.showMessageDialog(null, "Message stored: " + message);
    }

    private boolean saveMessageToJSON(String firstName, String lastName) {
        try {
            // Read existing JSON
            String content = new String(Files.readAllBytes(Paths.get(MESSAGES_FILE)));
            String jsonContent = content.trim().isEmpty() ? "{\"messages\":[]}" : content;
            StringBuilder jsonBuilder = new StringBuilder(jsonContent);
            
            // Remove closing bracket to add new message
            if (jsonBuilder.toString().contains("]")) {
                jsonBuilder.deleteCharAt(jsonBuilder.lastIndexOf("]"));
            }
            
            // Add comma if not first message
            if (!jsonBuilder.toString().endsWith("[")) {
                jsonBuilder.append(",");
            }
            
            // Create new message object
            String messageJson = String.format(
                "{\"messageID\":\"%s\",\"messageHash\":\"%s\",\"recipient\":\"%s\",\"sender\":\"%s %s\",\"timestamp\":\"%s\",\"message\":\"%s\"}",
                messageID, messageHash, recipientCellphone, firstName, lastName, 
                LocalDateTime.now().format(formatter), message.replace("\"", "\\\"")
            );
            
            jsonBuilder.append(messageJson).append("]}");
            Files.write(Paths.get(MESSAGES_FILE), jsonBuilder.toString().getBytes());
            return true;
            
        } catch (IOException e) {
            System.out.println("Error saving message to JSON: " + e.getMessage());
            return false;
        }
    }

    public static String viewAllMessages() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(MESSAGES_FILE)));
            return content;
        } catch (IOException e) {
            return "Error reading messages: " + e.getMessage();
        }
    }

    // Getters
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getMessage() { return message; }
    public String getRecipientCellphone() { return recipientCellphone; }
    public void setMessage(String message) { this.message = message; }
    public void setRecipientCellphone(String recipientCellphone) { this.recipientCellphone = recipientCellphone; }
} 