package it.polimi.SE2.CK.utils;

import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

import java.io.*;

/**
 * Class that manage the folder.
 */
public class FolderManager {
    /**
     * Folder path where saving all files.
     */
    private static String directory = ""; //TODO you have to add your directory - DISK:\\path\\to\\your\\directory\\


    /**
     * Gets the path where saving all files.
     *
     * @return the direcotry path.
     */
    public static String getDirectory() {
        return directory;
    }

    /**
     * Get the file name.
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
     * Remove the extension from a file name.
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
     * Get the file extension.
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
     * Save the file on disk.
     *
     * @param part the file to save.
     */
    public static void saveFile(Part part){
        String fileName = getFileName(part);

        String filePath = directory + fileName;

        try (InputStream is = part.getInputStream();
             OutputStream os = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("fatto");
    }

    /**
     * It deletes a directory.
     *
     * @param directoryToBeDeleted the directory path.
     */
    public void deleteDirectory(File directoryToBeDeleted) {
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