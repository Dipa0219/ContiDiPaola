package it.polimi.SE2.CK.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.SE2.CK.bean.SessionUser;
import it.polimi.SE2.CK.bean.User;

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

    public int createUser(User user) throws SQLException {
        String query1="SELECT * from new_schema.user where username= ? or email = ?";
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
}
