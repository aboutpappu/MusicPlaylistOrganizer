package Model;

/**
 * User Model Class Represents a user account
 */
public class User {

    private String email;
    private String name;
    private String phone;
    private String gender;
    private String password;

    // Constructor
    public User(String email, String name, String phone, String gender, String password) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.password = password;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getPassword() {
        return password;
    }

    // Setters
    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User: " + name + " (" + email + ")";
    }
}
