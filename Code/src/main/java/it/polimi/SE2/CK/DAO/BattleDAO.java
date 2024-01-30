package it.polimi.SE2.CK.DAO;

import it.polimi.SE2.CK.bean.Battle;
import it.polimi.SE2.CK.bean.Ranking;
import it.polimi.SE2.CK.bean.Tournament;
import it.polimi.SE2.CK.utils.EmailManager;
import it.polimi.SE2.CK.utils.GitHubManager;
import it.polimi.SE2.CK.utils.enumeration.TeamState;
import it.polimi.SE2.CK.utils.enumeration.TournamentState;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BattleDAO {
    private final Connection con;

    public BattleDAO(Connection con) {
        this.con = con;
    }

    public ArrayList<Battle> showBattlesByTournamentId(int tournamentId) throws SQLException {
        ArrayList<Battle> battles = new ArrayList<>();
        String query = "select * " +
                "from battle " +
                "where tournamentId = ?";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, tournamentId);
            result = pstatement.executeQuery();
            while (result.next()) {
                Battle battle = new Battle();
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
        } finally {
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
        Battle battle = null;
        String query = "select idBattle, b.Name, b.Description, b.RegDeadline,b.SubDeadline,b.CodeKata,b.MinNumStudent,b.MaxNumStudent, t.Name as tournamentName, b.Phase " +
                "from battle as b join tournament as t on t.idTournament = b.TournamentId " +
                "where Idbattle = ?";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = con.prepareStatement(query);
            pstatement.setInt(1, battleId);
            result = pstatement.executeQuery();
            while (result.next()) {
                battle = new Battle();
                battle.setId(result.getInt("Idbattle"));
                battle.setName(result.getString("Name"));
                battle.setDescription(result.getString("Description"));
                battle.setRegDeadline(result.getTimestamp("RegDeadline"));
                battle.setSubDeadline(result.getTimestamp("SubDeadline"));
                battle.setMinNumStudent(result.getInt("MinNumStudent"));
                battle.setMaxNumStudent(result.getInt("MaxNumStudent"));
                battle.setTournamentName(result.getString("tournamentName"));
                switch (result.getString("Phase")) {
                    case "Not Started" -> battle.setPhase(TournamentState.NOTSTARTED);
                    case "Ongoing" -> battle.setPhase(TournamentState.ONGOING);
                    case "Closed" -> battle.setPhase(TournamentState.CLOSED);
                }
                System.out.println("phase " + battle.getPhase());
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
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
    public boolean checkBattleByName(String name) throws SQLException {
        //search query
        String query = "SELECT * " +
                "FROM battle " +
                "WHERE Name = ?";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            return preparedStatement.execute();
        } catch (SQLException e) {
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
    }

    /**
     * Gets the battle id.
     *
     * @param name the battle name.
     * @return the battle id.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public int getBattleId(String name) throws SQLException {
        //search query
        String query = "SELECT idBattle " +
                "FROM battle " +
                "WHERE Name = ?";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int result = -1;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.first()){
                result = resultSet.getInt(1);
            }
        }
        catch (SQLException e) {
            throw new SQLException(e);
        } finally {
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
     * Check if the battle is not started and the tournament is in ongoing phase.
     *
     * @param battleId the battle id.
     * @return false if there is no result.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean checkBattleNotStarted(int battleId) throws SQLException {
        //search query
        String query = "SELECT * " +
                "FROM battle as b, tournament as t " +
                "WHERE b.TournamentId = t.idTournament and b.Phase = ? and t.Phase = ? and b.idbattle = ?";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, TournamentState.NOTSTARTED.getValue());
            preparedStatement.setString(2, TournamentState.ONGOING.getValue());
            preparedStatement.setInt(3, battleId);
            return preparedStatement.execute();
        }
        catch (SQLException e) {
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
    }

    /**
     * Check if the battle is ongoing and the tournament is in ongoing phase too.
     *
     * @param battleId the battle id.
     * @return false if there is no result.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean checkBattleOngoing(int battleId) throws SQLException {
        //search query
        String query = "SELECT * " +
                "FROM battle as b, tournament as t " +
                "WHERE b.TournamentId = t.idTournament and b.Phase = ? and t.Phase = ? and b.idbattle = ?";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, TournamentState.ONGOING.getValue());
            preparedStatement.setString(2, TournamentState.ONGOING.getValue());
            preparedStatement.setInt(3, battleId);
            return preparedStatement.execute();
        }
        catch (SQLException e) {
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new SQLException(e);
            }
        }
    }

    /**
     * Check if the user is in battle and also in tournament.
     *
     * @param battleId the specific battle.
     * @param userId the specific user.
     * @return false if there is no result.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public boolean checkEducatorManageBattle(int battleId, int userId) throws SQLException {
        //search query
        String query = "SELECT * " +
                "FROM battle as b, t_subscription as tsub " +
                "WHERE b.TournamentId = tsub.TournamentId " +
                "   and b.idbattle = ? and tsub.UserId = ?";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, battleId);
            preparedStatement.setInt(2, userId);
            return preparedStatement.execute();
        }
        catch (SQLException e) {
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
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
        String query = "INSERT INTO battle " +
                "(`Name`, `Description`, `RegDeadline`, `SubDeadline`, `CodeKata`, `MinNumStudent`, `MaxNumStudent`, `TournamentId`, `Phase`) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        //statement
        PreparedStatement preparedStatement = null;

        try {
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
            preparedStatement.execute();
        } catch (SQLException e) {
            return false;
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * Searches all battle Not Started and verify if they can be started.
     *
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public void startBattle() throws SQLException {
        //search query
        String query = "SELECT idbattle, RegDeadline, CodeKata " +
                "FROM battle as b, tournament as t\n" +
                "WHERE b.TournamentId = t.idTournament and b.Phase = ? and t.Phase = ? " +
                "ORDER BY b.RegDeadline ASC;";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //get the actual date
        Date currentDate = new Date();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, TournamentState.NOTSTARTED.getValue());
            preparedStatement.setString(2, TournamentState.ONGOING.getValue());
            resultSet = preparedStatement.executeQuery();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            while (resultSet.next()) {
                //if submission deadline < now
                if (resultSet.getTimestamp("RegDeadline").before(currentTimestamp)) {
                    int battleId = resultSet.getInt("idbattle");
                    String codeKata = resultSet.getString("CodeKata");
                    //update battle table
                    executor.submit(() ->
                    {
                        try {
                            startBattleUpdateTable(battleId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    //create GitHub repository for every team in battle
                    TeamDAO teamDAO = new TeamDAO(con);
                    executor.submit(() ->
                    {
                        ArrayList<Integer> teamInBattle = null;
                        //get the team in battle
                        try {
                            teamInBattle = (ArrayList<Integer>) teamDAO.getTeamInBattle(battleId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        //create a repository per team
                        ExecutorService executorTeam = Executors.newFixedThreadPool(teamInBattle.size());
                        List<Future<?>> futures = new ArrayList<>();

                        //real creation repository
                        for (Integer teamId : teamInBattle){
                            futures.add(executorTeam.submit(() ->
                                    GitHubManager.createGitHubRepositoryPerTeam(teamId, codeKata, con)));
                        }
                        executorTeam.shutdown();

                    });

                }
            }
            executor.shutdown();
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
    }

    /**
     * Searches all battle Started and verify if they must be closed.
     *
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public void closeBattle() throws SQLException {
        //search query
        String query = "SELECT idbattle, SubDeadline " +
                "FROM battle " +
                "WHERE Phase = ? " +
                "ORDER BY SubDeadline ASC";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        //get the actual date
        Date currentDate = new Date();
        Timestamp currentTimestamp = new Timestamp(currentDate.getTime());

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, TournamentState.ONGOING.getValue());
            resultSet = preparedStatement.executeQuery();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            while (resultSet.next()) {
                //if submission deadline < now
                if (resultSet.getTimestamp("SubDeadline").before(currentTimestamp)) {
                    int battleId = resultSet.getInt("idbattle");
                    //update battle table
                    executor.submit(() ->
                    {
                        try {
                            closeBattleUpdateTable(battleId);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    //send email to all student that enrolled in the battle
                    executor.submit(() ->
                            EmailManager.sendEmailToAllStudentBattleClosed(battleId, con));
                }
            }
            executor.shutdown();
        } catch (SQLException e) {
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


    }

    /**
     * Updates the battle phase from Ongoing to Closed.
     *
     * @param battleId the tournament id to update.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    private void closeBattleUpdateTable(int battleId) throws SQLException {
        //update query
        String query = "UPDATE battle " +
                "SET `Phase` = ? " +
                "WHERE (`idBattle` = ?)";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, TournamentState.CLOSED.getValue());
            preparedStatement.setInt(2, battleId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e2){
                throw new RuntimeException();
            }
        }
    }

    /**
     * Updates the battle phase from Not Started to Ongoing.
     *
     * @param battleId the tournament id to update.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    private void startBattleUpdateTable(int battleId) throws SQLException {
        //update query
        String query = "UPDATE battle " +
                "SET `Phase` = ? " +
                "WHERE (`idBattle` = ?)";
        //statement
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, TournamentState.ONGOING.getValue());
            preparedStatement.setInt(2, battleId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (preparedStatement != null){
                    preparedStatement.close();
                }
            }
            catch (SQLException e2){
                throw new RuntimeException();
            }
        }
    }

    public ArrayList<Ranking> showRanking(int battleId) throws SQLException {
        //search query
        String query = "select teamName, points " +
                "from new_schema.team " +
                "where battleId = ? " +
                "order by points desc";
        //statement
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ArrayList<Ranking> result=new ArrayList<>();
        try{
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, battleId);
            resultSet = preparedStatement.executeQuery();

            int i=1;
            while (resultSet.next()){
                Ranking ranking = new Ranking();
                ranking.setName(resultSet.getString("teamName"));
                ranking.setPoints(resultSet.getInt("points"));
                ranking.setPosition(i);
                i++;
                result.add(ranking);
            }
        }
        catch (SQLException e){
            return null;
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
}