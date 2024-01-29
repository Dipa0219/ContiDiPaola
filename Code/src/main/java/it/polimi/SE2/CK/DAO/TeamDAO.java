package it.polimi.SE2.CK.DAO;

import it.polimi.SE2.CK.utils.enumeration.TeamState;
import it.polimi.SE2.CK.utils.enumeration.TeamStudentState;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamDAO {
    private final Connection con;

    public TeamDAO(Connection con) {
        this.con = con;
    }

    /**
     * Gets the teams in a specific battle.
     *
     * @param battleId the specific battle.
     * @return the list of team in a specific battle.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public List<Integer> getTeamInBattle(int battleId) throws SQLException {
        //search query
        String query = "SELECT idteam " +
                "FROM team " +
                "WHERE battleId = ? and (not phase = ?)";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Integer> result = new ArrayList<>();

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, battleId);
            preparedStatement.setString(2, TeamState.INCOMPLETE.getValue());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                result.add(resultSet.getInt("idteam"));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new RuntimeException();
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e2) {
                throw new RuntimeException();
            }
        }

        return result;
    }

    /**
     * Insert a single student in a battle.
     *
     * @param userId the student to join in the battle.
     * @param battleId the battle to join.
     * @return true if the student joins in the battle.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean joinBattleAlone(int userId, int battleId) throws SQLException {
        //insert query
        String query = "INSERT INTO `new_schema`.`team` " +
                "(`numberStudent`, `battleId`, `phase`, `teamLeader`) " +
                "VALUES (?, ?, ?, ?)";
        //statement
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1,1);
            preparedStatement.setInt(2, battleId);
            preparedStatement.setString(3, TeamState.COMPLETE.getValue());
            preparedStatement.setInt(4, userId);
            preparedStatement.execute();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e2) {
                throw new RuntimeException();
            }
        }

        return true;
    }

    /**
     * Check if student is in another team for the same battle.
     *
     * @param userId the student id.
     * @param battleId the battle id.
     * @return true if student is in another team for the same battle.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean checkStudentInOtherTeam(int userId, int battleId) throws SQLException {
        //search query
        String query = "SELECT * " +
                "FROM team_student as ts, team as t " +
                "WHERE t.idteam = ts.teamId and ts.studentId = ? and ts.Phase = ? and t.battleId = ?";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, TeamStudentState.ACCEPT.getValue());
            preparedStatement.setInt(3, battleId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.first()){
                result = true;
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e2) {
                throw new RuntimeException();
            }
        }

        return result;
    }
}
