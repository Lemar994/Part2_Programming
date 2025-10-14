package login.and.registration;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {

    @Test
    public void testCheckUserName_Valid() {
        Login login = new Login("John", "Doe", "jd_", "Password1@", "+27821234567");
        assertTrue(login.checkUserName());
    }

    @Test
    public void testCheckUserName_Invalid() {
        Login login = new Login("John", "Doe", "johnDoe", "Password1@", "+27821234567");
        assertFalse(login.checkUserName());
    }

    @Test
    public void testCheckPasswordComplexity_Valid() {
        Login login = new Login("John", "Doe", "jd_", "Strong1@", "+27821234567");
        assertTrue(login.checkPasswordComplexity());
    }

    @Test
    public void testCheckPasswordComplexity_Invalid() {
        Login login = new Login("John", "Doe", "jd_", "weakpass", "+27821234567");
        assertFalse(login.checkPasswordComplexity());
    }

    @Test
    public void testCheckCellPhoneNumber_Valid() {
        Login login = new Login("John", "Doe", "jd_", "Password1@", "+27821234567");
        assertTrue(login.checkCellPhoneNumber());
    }

    @Test
    public void testCheckCellPhoneNumber_Invalid() {
        Login login = new Login("John", "Doe", "jd_", "Password1@", "0821234567");
        assertFalse(login.checkCellPhoneNumber());
    }

    @Test
    public void testRegisterUser_Success() {
        Login login = new Login("Test", "User", "te_st", "Password1@", "+27821234567");
        String result = login.registerUser();
        assertEquals("User registered successfully!", result);
    }

    @Test
    public void testRegisterUser_InvalidUsername() {
        Login login = new Login("John", "Doe", "johnDoe", "Password1@", "+27821234567");
        String result = login.registerUser();
        assertTrue("Should contain username error", result.contains("Username is not correctly formatted"));
    }

    @Test
    public void testRegisterUser_InvalidPassword() {
        Login login = new Login("John", "Doe", "jd_", "weakpass", "+27821234567");
        String result = login.registerUser();
        assertTrue("Should contain password error", result.contains("Password is not correctly formatted"));
    }

    @Test
    public void testRegisterUser_InvalidCellphone() {
        Login login = new Login("John", "Doe", "jd_", "Password1@", "0821234567");
        String result = login.registerUser();
        assertTrue("Should contain cellphone error", result.contains("Cellphone number is not correctly formatted"));
    }

    @Test
    public void testLoginUser_CorrectCredentials() {
        // Create and register a user - FIXED: Changed "test_u" to "te_st" (5 characters)
        Login registerLogin = new Login("Test", "User", "te_st", "Password1@", "+27821234567");
        String registerResult = registerLogin.registerUser();
        assertEquals("User registered successfully!", registerResult);
        
        // Now test login with a NEW instance (simulating real login scenario)
        Login login = new Login();
        boolean result = login.loginUser("te_st", "Password1@");
        assertTrue("Login should succeed with correct credentials", result);
    }

    @Test
    public void testLoginUser_WrongCredentials() {
        Login login = new Login();
        boolean result = login.loginUser("wrong", "wrong");
        assertFalse("Login should fail with wrong credentials", result);
    }

    @Test
    public void testReturnLoginStatus_Success() {
        Login login = new Login("John", "Doe", "jd_", "Password1@", "+27821234567");
        String status = login.returnLoginStatus(true);
        assertEquals("Welcome John Doe, it is great to see you again!", status);
    }

    @Test
    public void testReturnLoginStatus_Failure() {
        Login login = new Login("John", "Doe", "jd_", "Password1@", "+27821234567");
        String status = login.returnLoginStatus(false);
        assertEquals("Username or password incorrect, please try again.", status);
    }
}