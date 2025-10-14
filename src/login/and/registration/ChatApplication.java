package login.and.registration;

import javax.swing.JOptionPane;

public class ChatApplication {
    private Login currentUser;
    private int messageLimit;
    private int messagesSent = 0;

    public ChatApplication(Login user) {
        this.currentUser = user;
        startChatApplication();
    }

    private void startChatApplication() {
        // Welcome message
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");
        
        // Get message limit from user
        String limitInput = JOptionPane.showInputDialog("How many messages do you wish to enter?");
        try {
            messageLimit = Integer.parseInt(limitInput);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number. Setting default limit to 5 messages.");
            messageLimit = 5;
        }

        // Main menu loop
        boolean running = true;
        while (running && messagesSent < messageLimit) {
            String[] options = {"Send Messages", "Show recently sent messages", "Quit"};
            int choice = JOptionPane.showOptionDialog(null,
                "Please choose an option:",
                "QuickChat Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

            switch (choice) {
                case 0: // Send Messages
                    if (messagesSent < messageLimit) {
                        sendMessage();
                    } else {
                        JOptionPane.showMessageDialog(null, 
                            "You have reached your message limit of " + messageLimit + " messages.");
                    }
                    break;
                case 1: // Show recently sent messages
                    JOptionPane.showMessageDialog(null, "Coming Soon.");
                    break;
                case 2: // Quit
                case -1: // Closed dialog
                    running = false;
                    JOptionPane.showMessageDialog(null, "Thank you for using QuickChat!");
                    break;
            }
        }
        
        if (messagesSent >= messageLimit) {
            JOptionPane.showMessageDialog(null, 
                "You have reached your message limit of " + messageLimit + " messages. Application closing.");
        }
    }

    private void sendMessage() {
        // Get message details
        String recipient = JOptionPane.showInputDialog("Enter recipient cellphone number (+27XXXXXXXXX):");
        String messageText = JOptionPane.showInputDialog("Enter your message (max 250 characters):");

        if (recipient == null || messageText == null) {
            return; // User cancelled
        }

        // Create and validate message
        Message message = new Message(messageText, recipient);
        
        // Use SentMessage for user choice
        String result = message.SentMessage(currentUser.getFirstName(), currentUser.getLastName());
        
        if (result.equals("Message successfully sent.")) {
            messagesSent++;
            
            // Display message details
            String details = "Message Details:\n" +
                           "MessageID: " + message.getMessageID() + "\n" +
                           "Message Hash: " + message.getMessageHash() + "\n" +
                           "Recipient: " + message.getRecipientCellphone() + "\n" +
                           "Message: " + message.getMessage() + "\n\n" +
                           "Total messages sent: " + message.returnTotalMessages();
            
            JOptionPane.showMessageDialog(null, details);
        } else {
            JOptionPane.showMessageDialog(null, result);
        }
    }

    public static void main(String[] args) {
        // For testing without login
        Login testUser = new Login("test_1", "Password1@", "Test", "User", "+27831234567");
        new ChatApplication(testUser);
    }
}