package it.polimi.SE2.CK.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.SE2.CK.bean.SessionUser;
import it.polimi.SE2.CK.bean.User;
import it.polimi.SE2.CK.utils.enumeration.TeamState;
import it.polimi.SE2.CK.utils.enumeration.TeamStudentState;
import it.polimi.SE2.CK.utils.enumeration.UserRole;

public class UserDAO {
    private final Connection con;

    public UserDAO (Connection con) {
        this.con=con;
    }

    public SessionUser checkUsername( String username, String password) throws SQLException {
        SessionUser user = null;
        String query="SELECT * from user where username= ? and password = ?";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setString(1, username);
            pstatement.setString(2, password);
            result = pstatement.executeQuery();
            while (result.next()) {
                if (result.getString("username").equals(username)&& result.getString("password").equals(password)) {
                    user = new SessionUser();
                    user.setId(result.getInt("idUser"));
                    user.setUsername(result.getString("Username"));
                    user.setRole(result.getInt("Role"));
                }
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
        return user;
    }

    /**
     * Database search for all student emails.
     *
     * @return the student emails.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public List<String> allStudentEmail() throws SQLException {
        //search query
        String query="Select Email " +
                "FROM user " +
                "WHERE Role = ?";
        //statemente
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> result = new ArrayList<>();

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, UserRole.STUDENT.getValue());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                result.add(resultSet.getString("Email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }

        return result;
    }

    /**
     * Database search for all student emails enrolled in a specific tournament.
     *
     * @param tournamentID the specific tournament.
     * @return the student emails.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public List<String> allStudentTournamentEmail(int tournamentID) throws SQLException {
        //search query
        String query="Select Email" +
                "FROM user as u join t_subscription as ts on u.idUser=ts.UserId\n" +
                "WHERE u.Role = ? and ts.TournamentId = ?;";
        //statemente
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> result = new ArrayList<>();

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, UserRole.STUDENT.getValue());
            preparedStatement.setInt(2, tournamentID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                result.add(resultSet.getString("Email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }

        return result;
    }

    /**
     * Database search for all student emails enrolled in a specific tournament.
     *
     * @param tournamentID the specific tournament.
     * @return the student emails.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public List<String> allEducatorTournamentEmail(int tournamentID) throws SQLException {
        //search query
        String query="Select Email" +
                "FROM user as u join t_subscription as ts on u.idUser=ts.UserId " +
                "WHERE u.Role = ? and ts.TournamentId = ?;";
        //statemente
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> result = new ArrayList<>();

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, UserRole.EDUCATOR.getValue());
            preparedStatement.setInt(2, tournamentID);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                result.add(resultSet.getString("Email"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }

        return result;
    }

    /**
     * Database search for all student emails enrolled in a specific battle.
     *
     * @param battleId the specific battle.
     * @return the student emails.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public List<String> allStudentBattleEmail(int battleId) throws SQLException {
        //search query
        String query = " SELECT u.Email " +
                " FROM user as u, team as t, team_student as ts, battle as b " +
                " WHERE u.idUser = ts.studentId and b.idbattle = t.battleId and t.idteam = ts.teamId " +
                "and u.Role = ? and b.idbattle = ? and ts.phase = ? and (not t.phase = ?)";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<String> result = new ArrayList<>();

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, UserRole.STUDENT.getValue());
            preparedStatement.setInt(2, battleId);
            preparedStatement.setString(3, TeamStudentState.ACCEPT.getValue());
            preparedStatement.setString(4, TeamState.INCOMPLETE.getValue());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                result.add(resultSet.getString("Email"));
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }

        return result;
    }

    /**
     * Database search for all student GitHub username enrolled in a specific team.
     *
     * @param teamId the specific team.
     * @return the student GitHub username.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public List<String> allStudentBattleGitHub(int teamId) throws SQLException {
        //search query
        String query = "SELECT u.GitHubUser " +
                "FROM team_student as ts, user as u " +
                "WHERE ts.studentId = u.idUser and ts.teamId = ? and ts.phase = ?";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<String> result = new ArrayList<>();

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, teamId);
            preparedStatement.setString(2, TeamStudentState.ACCEPT.getValue());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                result.add(resultSet.getString("GitHubUser"));
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }

        return result;
    }

    /**
     * Retrieves the user's ID from the database.
     *
     * @param username the username to search.
     * @return the user ID or -1 if the username is not in the database.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public int getUserID(String username) throws SQLException {
        //search query
        String query = "SELECT idUser " +
                "FROM user " +
                "WHERE username = ?";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int result = -1;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                result = resultSet.getInt("idUser");
            }
        }
        catch (SQLException e){
            return -1;
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }

        return result;
    }

    /**
     * Retrieves the user's role from the database.
     *
     * @param userID the interested user id.
     * @return the user role.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public UserRole getUserRole(int userID) throws SQLException {
        //select query
        String query = "SELECT Role " +
                "FROM new_schema.user " +
                "WHERE idUser = ?";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet result = null;

        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            result = preparedStatement.executeQuery();

            while (result.next()){
                switch (result.getInt("Role")) {
                    case 0 -> {
                        return UserRole.EDUCATOR;
                    }
                    case 1 -> {
                        return UserRole.STUDENT;
                    }
                }
            }
        }
        catch (SQLException e){
            throw new SQLException();
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
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }
        return null;
    }

    public int createUser(User user) throws SQLException {
        String query1="SELECT * " +
                "from new_schema.user " +
                "where username= ? or email = ?";
        ResultSet result;
        PreparedStatement pstatement;
        try {
            pstatement = con.prepareStatement(query1);
            pstatement.setString(1, user.getUsername());
            pstatement.setString(2, user.getEmail());
            result = pstatement.executeQuery();
            while (result.next()) {
                if (result.getString("username").equals(user.getUsername())) {
                    return 1;
                } else if (result.getString("email").equals(user.getEmail())) {
                    return 2;
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        }
        String query2="INSERT INTO `new_schema`.`user` " +
                "(`Name`, `Surname`, `BirthDate`, `Username`, `Email`, `Password`, `GitHubUser`, `Role`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            pstatement = con.prepareStatement(query2);
            pstatement.setString(1, user.getName());
            pstatement.setString(2, user.getSurname());
            pstatement.setDate(3, user.getBirthdate());
            pstatement.setString(4, user.getUsername());
            pstatement.setString(5,user.getEmail());
            pstatement.setString(6,user.getPassword());
            pstatement.setString(7,user.getGitHubUser());
            pstatement.setInt(8,user.getRole());
            pstatement.executeUpdate();
        }
        catch(SQLException e){
            throw new SQLException(e);
        } finally {
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
        }
        return 0;
    }

    public User getUserById(int id) throws SQLException {
        User user = null;
        String query="SELECT * from user where idUser=?";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, id);
            result = pstatement.executeQuery();
            while (result.next()) {
                user = new User();
                user.setId(result.getInt("idUser"));
                user.setName(result.getString("name"));
                user.setSurname(result.getString("surname"));
                user.setBirthdate(result.getDate("birthdate"));
                user.setUsername(result.getString("Username"));
                user.setEmail(result.getString("email"));
                user.setRole(result.getInt("Role"));
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
        return user;
    }
}
