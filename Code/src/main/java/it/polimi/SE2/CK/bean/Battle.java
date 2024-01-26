package it.polimi.SE2.CK.bean;

import it.polimi.SE2.CK.utils.enumeration.TournamentState;

import java.sql.Timestamp;

public class Battle {
    int id;
    String name;
    String description;
    Timestamp regDeadline;
    Timestamp subDeadline;
    int minNumStudent;
    int maxNumStudent;
    String tournamentName;
    int tournamentId;
    TournamentState phase;
    String gitHubBattleRepository;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getRegDeadline() {
        return regDeadline;
    }

    public Timestamp getSubDeadline() {
        return subDeadline;
    }

    public int getMinNumStudent() {
        return minNumStudent;
    }

    public int getMaxNumStudent() {
        return maxNumStudent;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public TournamentState getPhase() {
        return phase;
    }

    public String getGitHubBattleRepository() {
        return gitHubBattleRepository;
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

    public void setRegDeadline(Timestamp regDeadline) {
        this.regDeadline = regDeadline;
    }

    public void setSubDeadline(Timestamp subDeadline) {
        this.subDeadline = subDeadline;
    }

    public void setMinNumStudent(int minNumStudent) {
        this.minNumStudent = minNumStudent;
    }

    public void setMaxNumStudent(int maxNumStudent) {
        this.maxNumStudent = maxNumStudent;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public void setTournamentId(int tournamentId) {
        this.tournamentId = tournamentId;
    }

    public void setPhase(TournamentState phase) {
        this.phase = phase;
    }

    public void setGitHubBattleRepository(String gitHubBattleRepository) {
        this.gitHubBattleRepository = gitHubBattleRepository;
    }
}
