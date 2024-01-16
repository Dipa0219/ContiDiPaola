package it.polimi.SE2.CK.bean;

public class SessionUser {
    private int id;
    private String username;
    private int role;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
