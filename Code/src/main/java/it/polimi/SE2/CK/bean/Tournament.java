package it.polimi.SE2.CK.bean;

import java.sql.Timestamp;

public class Tournament {
    int id;
    String name;
    String description;
    int creatorId;
    String creatorUsername;
    Timestamp regDeadline;
    String phase;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public Timestamp getRegDeadline() {
        return regDeadline;
    }

    public String getPhase() {
        return phase;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public void setRegDeadline(Timestamp regDeadline) {
        this.regDeadline = regDeadline;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }
}
