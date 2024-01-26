package it.polimi.SE2.CK.utils.folder;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

import java.io.*;

//TODO if you use Windows the path is formed by \\
//     if you use Linux   the path is formed by /
//     if you use MacOS   the path is formed by /

/**
 * Class that manage the folder.
 */
public class FolderManager {
    /**
     * A private static field that holds the directory path where files are saved.
     */
    private static String directory = ""; //TODO you have to add your directory - DISK:/path/to/your//directory/

    /**
     * A private static field that holds the path separator for Windows operating system.
     */
    private static String pathWindows = "\\";

    /**
     * A private static field that holds the path separator for Unix operating system.
     */
    private static String pathUnix = "/";


    /**
     * Gets the path separator for Windows operating system.
     *
     * @return the path separator for Windows operating system - \.
     */
    public static String getPathWindows() {
        return pathWindows;
    }

    /**
     * Gets the path separator for Unix operating system.
     *
     * @return the path separator for Unix operating system - /.
     */
    public static String getPathUnix() {
        return pathUnix;
    }

    /**
     * Gets the path where saving all files.
     *
     * @return the directory path.
     */
    public static String getDirectory() {
        return directory;
    }

    /**
     * Gets the file name.
     *
     * @param part the file request.
     * @return the file name.
     */
    public static String getFileName(Part part) {
        String partHeader = part.getHeader("content-disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                String fullFileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                return removeFileExtension(fullFileName);
            }
        }
        return null;
    }

    /**
     * Removes the extension from a file name.
     *
     * @param fileName the file name with extension requests.
     * @return the file name without the extension.
     */
    private static String removeFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }

    /**
     * Gets the file extension.
     *
     * @param part the file request.
     * @return the extension name.
     */
    public static String getFileExtension(Part part) {
        String partHeader = part.getHeader("content-disposition");
        for (String content : partHeader.split(";")) {
            if (content.trim().startsWith("filename")) {
                String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
                return fileName.substring(fileName.lastIndexOf('.') + 1);
            }
        }
        return null;
    }


    /**
     * Saves the file to disk.
     *
     * @param part the file to save.
     */
    public static void saveFile(Part part){
        String fileName = getFileName(part) + "." + getFileExtension(part);

        String filePath = directory + fileName;

        try (InputStream is = part.getInputStream();
             OutputStream os = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the file on disk.
     *
     * @param part the file to save.
     * @param directoryToSave the directory where save the file.
     */
    public static void saveFile(Part part, String directoryToSave){
        String fileName = getFileName(part) + "." + getFileExtension(part);

        String filePath = directoryToSave + FolderManager.getPathUnix() + fileName; //TODO select your OS

        File directoryFinal = new File(directoryToSave);
        if (!directoryFinal.exists()){
            directoryFinal.mkdirs();
        }

        try (InputStream is = part.getInputStream();
             OutputStream os = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a directory and its contents recursively.
     *
     * @param directoryToBeDeleted the directory path.
     */
    public static void deleteDirectory(File directoryToBeDeleted) {
        //directory tree
        File[] allContents = directoryToBeDeleted.listFiles();

        //delete tree, from leaves to root
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }

        //delete the root
        directoryToBeDeleted.delete();
    }
}