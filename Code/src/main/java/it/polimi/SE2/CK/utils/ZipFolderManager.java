package it.polimi.SE2.CK.utils;

import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;

import java.io.*;
import java.util.Enumeration;

/**
 * Class that manage the zip files.
 */
public class ZipFolderManager {
    /**
     * Unzip a zip file.
     *
     * @param zipFileName the zip file.
     */
    public static void unzip(String zipFileName){

        try (ZipFile zipFile = new ZipFile(FolderManager.getDirectory() + zipFileName + ".zip\\")) {
            Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
            System.out.println("qui");
            while (entries.hasMoreElements()) {
                System.out.println("viva");
                ZipArchiveEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    System.out.println("la");
                    File entryFile = new File(FolderManager.getDirectory() + zipFileName + "\\", entry.getName());
                    if (!entryFile.getParentFile().exists()) {
                        System.out.println("figa");
                        entryFile.getParentFile().mkdirs();
                    }
                    try (
                        InputStream is = zipFile.getInputStream(entry);
                        OutputStream os = new FileOutputStream(entryFile)
                    ){
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        System.out.println("e");
                        while ((bytesRead = is.read(buffer)) != -1) {
                            os.write(buffer, 0, bytesRead);
                            System.out.println("chi");
                        }
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println("la");
            throw new RuntimeException(e);
        }
        System.out.println("castiga");
    }
}
