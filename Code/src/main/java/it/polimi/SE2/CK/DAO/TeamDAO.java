package it.polimi.SE2.CK.DAO;

import it.polimi.SE2.CK.utils.enumeration.TeamState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    List<Integer> getTeamInBattle(int battleId) throws SQLException {
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
}
