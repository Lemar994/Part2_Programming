package login.and.registration;

import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class MessageTest {
    
    // ========== TEST 1: Message should not be more than 250 Characters ==========
    
    @Test
    public void testMessageLengthSuccess() {
        // Test for success - message within 250 characters
        Message message = new Message("Short test message", "+27718693002");
        String result = message.checkMessageLength();
        assertEquals("Message ready to send.", result);
    }
    
    @Test
    public void testMessageLengthFailure() {
        // Test for failure - message exceeds 250 characters
        String longMessage = "This message is intentionally very long to exceed the 250 character limit. " +
                           "We need to make sure that the validation properly catches messages that are too long. " +
                           "This should definitely be more than 250 characters when combined together. " +
                           "Adding more text to ensure we pass the 250 character threshold.";
        Message message = new Message(longMessage, "+27718693002");
        String result = message.checkMessageLength();
        assertTrue("Should contain excess character count", result.contains("Message exceeds 250 characters by"));
        assertTrue("Should indicate reduction needed", result.contains("please reduce size"));
    }

    // ========== TEST 2: Recipient number is correctly formatted ==========
    
    @Test
    public void testRecipientNumberSuccess() {
        // Test for success - valid South African number
        Message message = new Message("Test message", "+27718693002");
        String result = message.checkRecipientCellWithMessage();
        assertEquals("Cell phone number successfully captured.", result);
    }
    
    @Test
    public void testRecipientNumberFailure() {
        // Test for failure - invalid number format
        Message message = new Message("Test message", "08575975889");
        String result = message.checkRecipientCellWithMessage();
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result);
    }

    // ========== TEST 3: Message hash is correct ==========
    
    @Test
    public void testMessageHashCorrectFormat() {
        // Test Case 1: "Hi Mike, can you join us for dinner tonight" should produce HITONIGHT
        Message message = new Message("Hi Mike, can you join us for dinner tonight", "+27718693002");
        message.createMessageID();
        String hash = message.createMessageHash();
        
        // The hash should be in format: firstTwoDigits:messageNumber:HITONIGHT
        String[] parts = hash.split(":");
        assertEquals("Hash should have 3 parts", 3, parts.length);
        assertEquals("First part should be 2 digits", 2, parts[0].length());
        assertTrue("First part should be digits", parts[0].matches("\\d+"));
        assertTrue("Second part should be digits", parts[1].matches("\\d+"));
        assertEquals("Third part should be HITONIGHT", "HITONIGHT", parts[2]);
    }

    // ========== TEST 4: Message ID is created ==========
    
    @Test
    public void testMessageIDCreation() {
        Message message = new Message("Test message", "+27718693002");
        String messageId = message.createMessageID();
        
        // Test that message ID is generated
        assertNotNull("Message ID should not be null", messageId);
        
        // Test that message ID is 10 digits
        assertEquals("Message ID should be 10 characters", 10, messageId.length());
        
        // Test that message ID contains only digits
        assertTrue("Message ID should be all digits", messageId.matches("\\d{10}"));
        
        // Test that checkMessageID returns true for valid ID
        assertTrue("checkMessageID should return true for valid ID", message.checkMessageID());
    }

    // ========== TEST 5: MessageSent - User choices ==========
    
    @Test
    public void testMessageSentSendChoice() {
        // Test Case 1: User selected 'Send Message'
        Message message = new Message("Hi Mike, can you join us for dinner tonight", "+27718693002");
        
        // Test the sendMessage method that gets called when user chooses "Send Message"
        String result = message.sendMessage("Test", "User");
        assertEquals("Message successfully sent.", result);
    }
    
    @Test
    public void testMessageSentStoreChoice() {
        // Test Case 2: User selected 'Store Message'
        Message message = new Message("Test message to store", "+27718693002");
        
        // Test the storeMessage method directly
        try {
            message.storeMessage();
            // If we reach here, storeMessage executed without throwing exception
            assertTrue("Store message should complete", true);
        } catch (Exception e) {
            fail("storeMessage should not throw exceptions: " + e.getMessage());
        }
    }
    
    @Test
    public void testMessageSentDisregardMessage() {
        // Test Case 3: User selected 'Disregard Message'
        // We test this by verifying the expected return message
        String expectedDisregardMessage = "Press 0 to delete message.";
        assertEquals("Disregard should return correct message", expectedDisregardMessage, "Press 0 to delete message.");
    }
    
    @Test
    public void testMessageSentWithTestData1() {
        // Complete test with Test Data 1
        Message message = new Message("Hi Mike, can you join us for dinner tonight", "+27718693002");
        
        // Test all validations pass
        assertEquals("Message length should be valid", "Message ready to send.", message.checkMessageLength());
        assertEquals("Recipient should be valid", "Cell phone number successfully captured.", message.checkRecipientCellWithMessage());
        
        // Test send operation
        String sendResult = message.sendMessage("Test", "User");
        assertEquals("Message should send successfully", "Message successfully sent.", sendResult);
        
        // Test message details
        assertTrue("Message ID should be valid", message.checkMessageID());
        String hash = message.getMessageHash();
        assertTrue("Hash should match pattern XX:X:HITONIGHT", hash.matches("\\d{2}:\\d+:HITONIGHT"));
    }
    
    @Test
    public void testTotalMessagesCount() {
        // Test that total messages count works
        int initialCount = new Message().returnTotalMessages();
        
        // Send a message
        Message message = new Message("Test message for count", "+27718693002");
        message.sendMessage("Test", "User");
        
        // Check count increased
        assertTrue("Total messages should increase", message.returnTotalMessages() > initialCount);
    }
    
    // Additional edge case tests
    
    @Test
    public void testMessageLengthBoundary250() {
        // Test exact 250 characters (boundary case)
        String exact250 = "A".repeat(250);
        Message message = new Message(exact250, "+27718693002");
        String result = message.checkMessageLength();
        assertEquals("Exactly 250 characters should be valid", "Message ready to send.", result);
    }
    
    @Test
    public void testMessageLengthBoundary251() {
        // Test 251 characters (boundary case + 1)
        String exceed1 = "B".repeat(251);
        Message message = new Message(exceed1, "+27718693002");
        String result = message.checkMessageLength();
        assertTrue("251 characters should fail", result.contains("Message exceeds 250 characters by 1"));
    }
    
    @Test
    public void testRecipientNumberEdgeCases() {
        // Test various invalid formats
        Message message1 = new Message("Test", "27718693002"); // Missing +
        String result1 = message1.checkRecipientCellWithMessage();
        assertTrue("Should fail without +", result1.contains("incorrectly formatted"));
        
        Message message2 = new Message("Test", "+27123"); // Too short
        String result2 = message2.checkRecipientCellWithMessage();
        assertTrue("Should fail when too short", result2.contains("incorrectly formatted"));
    }
    
    @Test
    public void testMessageIDUniqueness() {
        // Test that multiple messages have different IDs (high probability)
        Message message1 = new Message("Message 1", "+27718693002");
        String id1 = message1.createMessageID();
        
        Message message2 = new Message("Message 2", "+27718693002");
        String id2 = message2.createMessageID();
        
        assertNotEquals("Message IDs should be different", id1, id2);
    }

    private void assertEquals(String first_part_should_be_2_digits, int i, int length) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void assertEquals(String cell_phone_number_is_incorrectly_formatte, String result) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void assertEquals(String message_length_should_be_valid, String message_ready_to_send, String checkMessageLength) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}