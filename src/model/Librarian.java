package model;

public class Librarian {
    private String username;
    private String password; // In a real app, this should be hashed!

    public Librarian(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}