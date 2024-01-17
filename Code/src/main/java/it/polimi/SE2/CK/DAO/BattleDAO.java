package it.polimi.SE2.CK.DAO;

import it.polimi.SE2.CK.bean.Battle;
import it.polimi.SE2.CK.bean.Tournament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BattleDAO {
    private final Connection con;

    public BattleDAO (Connection con) {
        this.con=con;
    }

    public ArrayList<Battle> showBattlesByTournamentId (int tournamentId) throws SQLException {
        ArrayList<Battle> battles = new ArrayList<>();
        String query="select *\n" +
                "from new_schema.battle\n" +
                "where tournamentId=?;";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, tournamentId);
            result = pstatement.executeQuery();
            while (result.next()) {
                Battle battle= new Battle();
                battle.setId(result.getInt("Idbattle"));
                battle.setName(result.getString("Name"));
                battle.setDescription(result.getString("Description"));
                battle.setRegDeadline(result.getTimestamp("RegDeadline"));
                battle.setSubDeadline(result.getTimestamp("SubDeadline"));
                battle.setMinNumStudent(result.getInt("MinNumStudent"));
                battle.setMaxNumStudent(result.getInt("MaxNumStudent"));
                battles.add(battle);
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
        return battles;
    }

    public Battle showBattleById(int battleId) throws SQLException {
        Battle battle =null;
        String query="select idBattle, b.Name, b.Description, b.RegDeadline,b.SubDeadline,b.CodeKata,b.MinNumStudent,b.MaxNumStudent, t.Name as tournamentName\n" +
                "from new_schema.battle as b join new_schema.tournament as t on t.idTournament=b.TournamentId\n" +
                "where Idbattle=?;";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, battleId);
            result = pstatement.executeQuery();
            while (result.next()) {
                battle= new Battle();
                battle.setId(result.getInt("Idbattle"));
                battle.setName(result.getString("Name"));
                battle.setDescription(result.getString("Description"));
                battle.setRegDeadline(result.getTimestamp("RegDeadline"));
                battle.setSubDeadline(result.getTimestamp("SubDeadline"));
                battle.setMinNumStudent(result.getInt("MinNumStudent"));
                battle.setMaxNumStudent(result.getInt("MaxNumStudent"));
                battle.setTournamentName(result.getString("tournamentName"));
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
        return battle;
    }
}
