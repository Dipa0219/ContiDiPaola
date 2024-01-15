package it.polimi.SE2.CK.DAO;


import it.polimi.SE2.CK.bean.Tournament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TournamentDAO {

    private final Connection  con;

    public TournamentDAO (Connection con) {
        this.con=con;
    }

    public ArrayList<Tournament> showTournamentByUserId (int userId) throws SQLException {
        ArrayList<Tournament> tournaments = new ArrayList<>();
        String query="select t.idTournament, t.Name, t.Description, t.CreatorId, t.RegDeadline, u.username\n" +
                "from tournament as t join t_subscription on idTournament=TournamentId join user as u on t.CreatorId= u.idUser\n" +
                "where UserId = ?;";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, userId);
            result = pstatement.executeQuery();
            while (result.next()) {
                Tournament tournament= new Tournament();
                tournament.setId(result.getInt("IdTournament"));
                tournament.setName(result.getString("Name"));
                tournament.setDescription(result.getString("Description"));
                tournament.setCreatorId(result.getInt("CreatorId"));
                tournament.setCreatorUsername(result.getString("Username"));
                tournament.setRegDeadline(result.getTimestamp("RegDeadline"));
                tournaments.add(tournament);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }
        return tournaments;
    }

    public Tournament showTournamentById (int id) throws SQLException {
        Tournament tournament = null;
        ArrayList<Tournament> tournaments = new ArrayList<>();
        String query="select t.idTournament, t.Name, t.Description, t.CreatorId, t.RegDeadline, u.username\n" +
                "from tournament as t join user as u on t.CreatorId= u.idUser\n" +
                "where t.idTournament = ?;";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, id);
            result = pstatement.executeQuery();
            while (result.next()) {
                tournament= new Tournament();
                tournament.setId(result.getInt("IdTournament"));
                tournament.setName(result.getString("Name"));
                tournament.setDescription(result.getString("Description"));
                tournament.setCreatorId(result.getInt("CreatorId"));
                tournament.setCreatorUsername(result.getString("Username"));
                tournament.setRegDeadline(result.getTimestamp("RegDeadline"));
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }
        return tournament;
    }
}
