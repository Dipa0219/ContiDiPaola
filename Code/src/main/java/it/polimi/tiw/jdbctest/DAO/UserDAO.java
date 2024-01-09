package it.polimi.tiw.jdbctest.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polimi.tiw.jdbctest.bean.User;

public class UserDAO {
    private Connection con;

    public UserDAO (Connection con) {
        this.con=con;
    }

    public User checkUsername( String username, String password) throws SQLException {
        User user = null;
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
                    user= new User();
                    user.setId(result.getInt("idUser"));
                    user.setUsername(result.getString("username"));
                    user.setPassword(result.getString("password"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
