package it.polimi.SE2.CK.utils;

import okhttp3.*;
import okhttp3.OkHttpClient;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.*;
import org.eclipse.jgit.errors.UnmergedPathException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;


//TODO ssh -R 80:localhost:8085 nokey@localhost.run

/**
 * Class that manage the interacting with the GitHub API.
 */
public class GitHubManager {
    /**
     * The URL of the GitHub API.
     */
    private static final String gitHubURL="https://api.github.com/user/repos";

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
        System.out.println(repositoryName);
        OkHttpClient client = new OkHttpClient();

        //set name repository and the visibility
        MediaType mediaType = MediaType.parse("application/json");
        String requestBody = "{\"name\": \"" + repositoryName + "\", \"private\": " + isPrivate + "}";

        //build the repository
        Request request = new Request.Builder()
                .url(gitHubURL)
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
     * @param ghRepoURL the URL of the repository.
     */
    public static void uploadFolderOnGitHubRepository(String projectPath, String ghRepoURL) {
        System.out.println(projectPath);
        System.out.println(ghRepoURL);
        File projectPathToUpload = new File(projectPath);

        Git git = null;
        //clone the existing repository
        try {
            git = Git.cloneRepository()
                    .setURI(ghRepoURL)
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
}