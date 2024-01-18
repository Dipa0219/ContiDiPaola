package it.polimi.SE2.CK.DAO;


import it.polimi.SE2.CK.bean.Tournament;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class manage the interaction with the database that manage the tournament data.
 */
public class TournamentDAO {
    /**
     * A connection (session) with a specific database.
     */
    private final Connection  con;

    /**
     * TournamentDAO constructor.
     *
     * @param con connection (session) with a specific database.
     */
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
                "where t.idTournament = ?";
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

    /**
     * Check the existence of a tournament with the specified name.
     *
     * @param name the tournament name to search.
     * @return false if there is no result.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean checkTournamentByName (String name) throws SQLException {
        //search query
        String query = "SELECT * " +
                "FROM new_schema.tournament " +
                "WHERE Name = ?";
        //statement
        PreparedStatement preparedStatement = null;
        boolean result;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            result = preparedStatement.execute();
        }
        catch (SQLException e){
            throw new SQLException();
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e){
                return false;
            }
        }
        return result;
    }

    /**
     * Inserts a tournament in the database.
     *
     * @param tournament the tournament to insert.
     * @return true if the tournament has been added to the database.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean createTournament(Tournament tournament) throws SQLException{
        //insert query
        String query = "INSERT INTO `tournament` " +
                "(`Name`, `Description`, `CreatorId`, `RegDeadline`) " +
                "VALUES (?, ?, ?, ?);";
        //statement
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, tournament.getName());
            preparedStatement.setString(2, tournament.getDescription());
            preparedStatement.setInt(3, tournament.getCreatorId());
            preparedStatement.setTimestamp(4, tournament.getRegDeadline());
            preparedStatement.execute();
        }
        catch (SQLException e){
            e.printStackTrace();
            throw new SQLException();
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e){
                return false;
            }
        }
        return true;
    }
}
