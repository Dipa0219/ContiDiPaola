package it.polimi.tiw.jdbctest.bean;

import java.sql.Date;

public class User {
    private int id;
    private String Name;
    private String Surname;
    private Date Birthdate;
    private String username;
    private String email;
    private String password;
    private String gitHubUser;
    private int role;

    public int getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getSurname() {
        return Surname;
    }

    public Date getBirthdate() {
        return Birthdate;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getGitHubUser() {
        return gitHubUser;
    }

    public int getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public void setBirthdate(Date birthdate) {
        Birthdate = birthdate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setGitHubUser(String gitHubUser) {
        this.gitHubUser = gitHubUser;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
