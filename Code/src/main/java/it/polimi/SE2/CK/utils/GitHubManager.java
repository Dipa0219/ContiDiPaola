package it.polimi.SE2.CK.utils;

import it.polimi.SE2.CK.DAO.BattleDAO;
import it.polimi.SE2.CK.DAO.UserDAO;
import it.polimi.SE2.CK.utils.folder.FolderManager;
import okhttp3.*;
import okhttp3.OkHttpClient;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.errors.UnmergedPathException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


//TODO ssh -R 80:localhost:8085 nokey@localhost.run - tunneling

/**
 * Class that manage the interacting with the GitHub API.
 */
public class GitHubManager {
    /**
     * The URL of the GitHub API.
     */
    private static final String gitHubURL="https://api.github.com";

    /**
     * IThe username of the GitHub account.
     */
    private static final String gitHubUsername="CodeKataCKB";

    /**
     * The token used for authentication with GitHub.
     */
    private static final String gitHubToken ="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";

    /**
     * The URL of the root GitHub repository.
     */
    private static final String repoURL="https://github.com/" + gitHubUsername + "/";


    /**
     * Gets the URL of the root GitHub repository.
     *
     * @return the URL of the root GitHub repository.
     */
    public static String getRepoURL() {
        return repoURL;
    }


    /**
     * It creates an empty GitHub repository.
     *
     * @param repositoryName the name of the repository.
     * @param isPrivate the new repository is private or public (true = private repository).
     */
    public static void createGitHubRepository(String repositoryName, boolean isPrivate) {
        OkHttpClient client = new OkHttpClient();

        //set name repository and the visibility
        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\"name\": \"" + repositoryName + "\", \"private\": " + isPrivate + "}";

        //build the repository
        Request request = new Request.Builder()
                .url(gitHubURL + "/user/repos")
                .post(RequestBody.create(mediaType, requestBody))
                .header("Authorization", "Bearer " + gitHubToken)
                .build();

        //create the repository
        try{
            client.newCall(request).execute();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * It uploads a project on existing GitHub repository.
     *
     * @param projectPath the project path.
     * @param repositoryURL the URL of the repository.
     */
    public static void uploadFolderOnGitHubRepository(String projectPath, String repositoryURL) {
        File projectPathToUpload = new File(projectPath);

        Git git = null;
        //clone the existing repository
        try {
            git = Git.cloneRepository()
                    .setURI(repositoryURL)
                    .setDirectory(projectPathToUpload)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitHubUsername, gitHubToken))
                    .call();
        }
        catch (JGitInternalException e){
            e.printStackTrace();
        }

        //add all file to repository
        try {
            git.add().addFilepattern(".").call();
        } catch (NoFilepatternException e) {
            e.printStackTrace();
        }

        //commit
        try {
            git.commit().setMessage("Add the project").call();
        } catch (NoHeadException | NoMessageException | UnmergedPathException | ConcurrentRefUpdateException |
                 WrongRepositoryStateException e) {
            e.printStackTrace();
        }

        //set up credential provider for autentication on GitHub
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitHubUsername, gitHubToken);

        //push
        try {
            git.push().setCredentialsProvider(credentialsProvider).call();
        } catch (InvalidRemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * It pulls a repository and save it on the disk.
     *
     * @param repositoryURL the url repository path.
     * @param repositoryName the name of the repository.
     */
    private static void pullGitHubRepository(String repositoryURL, String repositoryName) {
        File destinationRepository = new File(FolderManager.getDirectory() + repositoryName + "\\");
        Git git = null;

        try {
            git = Git.cloneRepository()
                    .setURI(repositoryURL)
                    .setDirectory(destinationRepository)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitHubUsername, gitHubToken))
                    .call();
        }
        catch (JGitInternalException e){
            e.printStackTrace();
        }

        PullCommand pullCommand = git.pull();
        try {
            pullCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitHubUsername, gitHubToken))
                    .call();
        } catch (WrongRepositoryStateException | RefNotFoundException | InvalidConfigurationException |
                 DetachedHeadException | InvalidRemoteException | CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * It adds a repository collaborator.
     *
     * @param repositoryName the repository name.
     * @param username the GitHub username.
     */
    private static void addCollaboratorOnGitHubRepository(String repositoryName, String username) {
        String api = gitHubURL + "/repos/" + gitHubUsername + "/" + repositoryName + "/collaborators/" + username;

        OkHttpClient client = new OkHttpClient();
        String requestBody = "{\"permission\": \"admin\"}";

        Request request = new Request.Builder()
                .url(api)
                .put(RequestBody.create(MediaType.parse("application/json"), requestBody))
                .header("Authorization", "Bearer " + gitHubToken)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * It manages all action that occurs in a creation of GitHub repository for a specific team in a battle.
     *
     * @param teamId the specific team.
     */
    public static void createGitHubRepositoryPerTeam(int teamId, Connection connection){
        //get all GitHub username
        UserDAO userDAO = new UserDAO(connection);
        ArrayList<String> ghUsername = null;
        try {
            ghUsername = (ArrayList<String>) userDAO.allStudentBattleGitHub(teamId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //get the battle name and CodeKata
        BattleDAO battleDAO = new BattleDAO(connection);
        String battleName = null;
        String codeKata = null;
        try {
            codeKata = battleDAO.getCodeKataFromTeamId(teamId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (codeKata.length()==0){
            throw new RuntimeException();
        }
        else {
            battleName = codeKata.substring(getRepoURL().length());
        }

        //GitHub pull from CodeKata
        pullGitHubRepository(codeKata, battleName);

        //GitHub repository creation
        createGitHubRepository(battleName + teamId, true);

        //upload folder on GitHub
        uploadFolderOnGitHubRepository(FolderManager.getDirectory() + battleName + FolderManager.getPathWindows(),
                GitHubManager.getRepoURL() + battleName + teamId);

        //add teammate
        for (String s : ghUsername) {
            addCollaboratorOnGitHubRepository(battleName + teamId, s);
        }

        //TODO set automatic notification

        //delete folder pull repository
        FolderManager.deleteDirectory(new File(FolderManager.getDirectory() + battleName + FolderManager.getPathWindows()));
    }
}