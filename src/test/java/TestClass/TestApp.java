package TestClass;

import com.mycompany.messenger_app.Messenger_App;
import com.mycompany.messenger_app.Messenger_App.Login;
import com.mycompany.messenger_app.Messenger_App.Message;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Deen
 */
public class TestApp {
    
    @BeforeEach
    public void setUp() {
        // Clear all three arrays and reset the sent counter before every test
        Messenger_App.sentMessages.clear();
        Messenger_App.storedMessages.clear();
        Messenger_App.disregardedMessages.clear();
        Message.resetTotalMessagesSent();
        
        // Populate arrays using sentMessage.
        // First message is sent
        Message msg1 = new Message(1, "+27834557896", "Did you get the cake?");
        msg1.sentMessage(0);
 
        // Second message is stored
        Message msg2 = new Message(2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage(2);
 
        // Third message is disregarded
        Message msg3 = new Message(3, "+27834484567", "Yohoooo, I am at your gate.");
        msg3.sentMessage(1);
 
        // Fourth message is sent
        Message msg4 = new Message(4, "0838884567", "It is dinner time!");
        msg4.sentMessage(0);
 
        // Fifth message is stored
        Message msg5 = new Message(5, "+27838884567", "Ok, I am leaving without you.");
        msg5.sentMessage(2);

    }
    
    // ========== Username format tests ==========

    @Test
    public void testUsernameCorrectFormat() {
        assertTrue(Login.checkUsername("kyl_1"));
    }
    
    @Test
    public void testUsernameIncorrectFormat() {
        assertFalse(Login.checkUsername("kyle!!!!!!!"));
    }
    
    // ========== Password format tests ==========  

    @Test
    public void testPasswordCorrectFormat() {
        assertTrue(Login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }
    
    @Test
    public void testPasswordIncorrectFormat() {
        assertFalse(Login.checkPasswordComplexity("password"));
    }
    
    // ========== Cellphone number format tests
         
    @Test
    public void testCellNoCorrectFormat() {
        assertTrue(Login.checkCellPhoneNumber("+27838968976"));
    }
    
    @Test
    public void testCellNoIncorrectFormat() {
        assertFalse(Login.checkCellPhoneNumber("08966553"));
    }
    
    // ========== Login tests ==========
    
    @Test
    public void testLoginSuccess() {
        Login user = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.loginUser("Ch&&sec@ke99!"));
    }
    
    @Test
    public void testLoginFailure() {
        Login user = new Login("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(user.loginUser("password"));
    }
    
    // ========== Message length tests ==========
    
    @Test
    public void testMessageLengthSuccess() {
        // If message is within the 250 character limit
        String result = Message.checkMessageLength("Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", result);
    }
 
    @Test
    public void testMessageLengthFailure() {
        // If message exceeds 250 character limit
        String longMessage = "A".repeat(260);
        String result = Message.checkMessageLength(longMessage);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", result);
    }
 
    // ========== Recipient cell phone number tests ==========
 
    @Test
    public void testRecipientCellSuccess() {
        // If phone number is correctly formatted
        String result = Message.checkRecipientCell("+27718693002");
        assertEquals("Cell phone number successfully captured.", result);
    }
 
    @Test
    public void testRecipientCellFailure() {
        // If phone number is incorrectly formatted
        String result = Message.checkRecipientCell("08575975889");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an " +
                     "international code. Please correct the number and try again.", result);
    }
 
    // ========== Message hash tests ==========
 
    @Test
    public void testMessageHashCorrectFormat() {
        // If hash is correctly formatted
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        
        String hash = msg.getMessageHash();
 
        // Hash must be all uppercase
        assertEquals(hash.toUpperCase(), hash);
 
        // Hash must end with HITONIGHT
        assertTrue(hash.endsWith(":HITONIGHT"),
            "Expected hash to end with ':HITONIGHT' but got: " + hash);
 
        // Hash must contain the message number
        assertTrue(hash.contains(":1:"),
            "Expected hash to contain ':1:' but got: " + hash);
    }
 
    @Test
    public void testMessageHashAllMessagesInLoop() {
        // Tests hashes for multiple messages in a loop
        String[] messages = {
            "Hi Mike, can you join us for dinner tonight?",
            "Hi Keegan, did you receive the payment?"
        };
        String[] recipients = { "+27718693002", "+27831234567" };
 
        for(int i = 0; i < messages.length; i++) {
            Message msg = new Message(i + 1, recipients[i], messages[i]);
            
            String hash = msg.getMessageHash();
 
            assertEquals(hash.toUpperCase(), hash,
                "Hash for message " + (i + 1) + " should be uppercase.");
 
            // Every hash must have 3 seperated parts
            String[] parts = hash.split(":");
            assertEquals(3, parts.length,
                "Hash for message " + (i + 1) + " should have 3 parts separated by ':'.");
        }
    }
 
    // ========== Message ID test ==========
 
    @Test
    public void testMessageIDIsGenerated() {
        // If message ID is generated
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        
        assertNotNull(msg.getMessageID(), "Message ID should not be null.");
        System.out.println("Message ID generated: " + msg.getMessageID());
    }
 
    // ========== Sent message tests ==========
 
    @Test
    public void testSentMessageSend() {
        Message.resetTotalMessagesSent();
        // If the message is sent
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        
        String result = msg.sentMessage(0);
        assertEquals("Message successfully sent.", result);
        assertEquals(1, Message.returnTotalMessages());
    }
 
    @Test
    public void testSentMessageDisregard() {
        // If the message is deletted 
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        
        String result = msg.sentMessage(1);
        assertEquals("Message disregarded.", result);
    }
 
    @Test
    public void testSentMessageStore() {
        // If the message is stored
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        
        String result = msg.sentMessage(2);
        assertEquals("Message successfully stored.", result);
    }  
    
    // ========== PoE Part 3 unit tests ========== 
    
    @Test
    public void testSentMessagesArrayPopulation() {
        // Checks if the message text matches
        assertEquals("Did you get the cake?", Messenger_App.sentMessages.get(0).getMessageText());
        assertEquals("It is dinner time!", Messenger_App.sentMessages.get(1).getMessageText());
    }
    
    @Test
    public void testLongestStoredMessage() {
        String longestMessage = "";
            
            // Same algorithm used in main program to find the longest message
            for (Message m : Messenger_App.storedMessages) {
                if (m.getMessageText().length() > longestMessage.length()) {
                    longestMessage = m.getMessageText();
                }
            }
        
        assertEquals("Where are you? You are late! I have asked you to be on time.", longestMessage);
    }
    
    @Test
    public void testMessageIDSearch() {
        String searchID = Messenger_App.sentMessages.get(1).getMessageID();
 
        // Test if searched Message ID matches
        Message foundID = null;
        for (Message m : Messenger_App.sentMessages) {
            if (m.getMessageID().equals(searchID)) {
                foundID = m;
                break;
            }
        }
 
        assertNotNull(foundID, "Message should be found by its ID.");
        assertEquals("It is dinner time!", foundID.getMessageText());
    }
    
    @Test
    public void testRecipientSearch() {
        String recipientNum = "+27838884567";
        ArrayList<String> results = new ArrayList<>();
        
        // Test if searched recipient messages exist
        for (Message m : Messenger_App.storedMessages) {
            if (m.getRecipient().equals(recipientNum)) {
                results.add(m.getMessageText());
            }
        }
        
        assertTrue(results.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(results.contains("Ok, I am leaving without you."));
    }
    
    @Test
    public void testDeleteByHash() {
        String searchHash = Messenger_App.storedMessages.get(0).getMessageHash();
        
        // Test if the message with the searched hash was removed
        boolean removed = Messenger_App.storedMessages.removeIf(m -> m.getMessageHash().equals(searchHash));
        
        assertTrue(removed);
    }
    
    @Test
    public void testDisplayReport() {
        Login user = new Login("deen_", "Password1!", "+27712345678");
        String report = Message.printSentMessages(user);
        
        assertTrue(report.contains("Message: Did you get the cake?"));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message Hash: " + Messenger_App.sentMessages.get(0).getMessageHash()));
        
    }
}
