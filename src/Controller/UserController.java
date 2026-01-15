package Controller;

import Model.User;
import java.util.HashMap;

/**
 * Controller for User authentication and management
 */
public class UserController {
    private HashMap<String, User> users;
    private User currentUser;

    public UserController() {
        users = new HashMap<>();
        currentUser = null;
        // Add a default test user
        registerUser("test@example.com", "Test User", "1234567890", "Male", "password");
    }

    // Register new user
    public boolean registerUser(String email, String name, String phone, 
                               String gender, String password) {
        if (users.containsKey(email)) {
            return false; // Email already exists
        }

        if (!isValidEmail(email) || password.length() < 6) {
            return false; // Invalid email or weak password
        }

        User newUser = new User(email, name, phone, gender, password);
        users.put(email, newUser);
        return true;
    }

    // Login user
    public boolean loginUser(String email, String password) {
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    // Logout
    public void logout() {
        currentUser = null;
    }

    // Get current logged-in user
    public User getCurrentUser() {
        return currentUser;
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // Email validation
    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    // Change password
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser != null && currentUser.getPassword().equals(oldPassword)) {
            if (newPassword.length() >= 6) {
                currentUser.setPassword(newPassword);
                return true;
            }
        }
        return false;
    }

    // Update user profile
    public boolean updateProfile(String name, String phone) {
        if (currentUser != null) {
            currentUser.setName(name);
            currentUser.setPhone(phone);
            return true;
        }
        return false;
    }
}