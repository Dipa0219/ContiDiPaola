package it.polimi.SE2.CK.DAO;

import it.polimi.SE2.CK.bean.Battle;
import it.polimi.SE2.CK.bean.Tournament;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;

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

    /**
     * Check the existence of a battle with the specified name.
     *
     * @param name the tournament name to search.
     * @return false if there is no result.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean checkBattleByName (String name) throws SQLException {
        //search query
        String query = "SELECT * " +
                "FROM new_schema.battle " +
                "WHERE Name = ?";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            return preparedStatement.execute();
        }
        catch (SQLException e){
            return false;
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e){
                throw new SQLException(e);
            }
        }
    }

    /**
     * Inserts a battle in the database.
     *
     * @param battle the battle to insert.
     * @return true if the battle has been added to the database.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean createBattle(Battle battle) throws SQLException {
        //insert query
        String query = "INSERT INTO `new_schema`.`battle` " +
                "(`Name`, `Description`, `RegDeadline`, `SubDeadline`, `CodeKata`, `MinNumStudent`, `MaxNumStudent`, `TournamentId`, `Phase`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        //statement
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, battle.getName());
            preparedStatement.setString(2, battle.getDescription());
            preparedStatement.setTimestamp(3, battle.getRegDeadline());
            preparedStatement.setTimestamp(4, battle.getSubDeadline());
            preparedStatement.setString(5, battle.getGitHubBattleRepository());
            preparedStatement.setInt(6, battle.getMinNumStudent());
            preparedStatement.setInt(7, battle.getMaxNumStudent());
            preparedStatement.setInt(8, battle.getTournamentId());
            preparedStatement.setString(9, battle.getPhase().getValue());
            System.out.println(preparedStatement);
            preparedStatement.execute();
        }
        catch (SQLException e){
            System.out.println("sono qui");
            return false;
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        return true;
    }
}
