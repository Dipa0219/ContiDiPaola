package it.polimi.SE2.CK.bean;

import it.polimi.SE2.CK.utils.enumeration.TeamState;

public class Team {
    int idTeam;
    int numberStudent;
    int battleId;
    TeamState phase;
    int teamLeader;
    int points;
    String teamName;

    public int getIdTeam() {
        return idTeam;
    }

    public int getNumberStudent() {
        return numberStudent;
    }

    public int getBattleId() {
        return battleId;
    }

    public TeamState getPhase() {
        return phase;
    }

    public int getTeamLeader() {
        return teamLeader;
    }

    public int getPoints() {
        return points;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setIdTeam(int idTeam) {
        this.idTeam = idTeam;
    }

    public void setNumberStudent(int numberStudent) {
        this.numberStudent = numberStudent;
    }

    public void setBattleId(int battleId) {
        this.battleId = battleId;
    }

    public void setPhase(TeamState phase) {
        this.phase = phase;
    }

    public void setTeamLeader(int teamLeader) {
        this.teamLeader = teamLeader;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
