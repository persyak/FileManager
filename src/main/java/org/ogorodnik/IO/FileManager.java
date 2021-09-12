package org.ogorodnik.IO;

import java.awt.geom.IllegalPathStateException;
import java.io.*;
import java.nio.file.DirectoryStream;

public class FileManager {

    public static int countFiles(String path) throws FileNotFoundException {
        File directory = new File(path);
        checkIfFileOrDirectoryExists(directory);
        checkifSourceIsDirectory(directory);
        int filecount = 0;
        File[] array = directory.listFiles();
        for (File element : array) {
            if (!element.isHidden()) {
                if (element.isFile()) {
                    filecount++;
                } else {
                    String innerPath = element.getPath();
                    filecount += countFiles(innerPath);
                }
            }
        }
        return filecount;
    }

    public static int countDirs(String path) throws FileNotFoundException {
        File directory = new File(path);
        checkIfFileOrDirectoryExists(directory);
        checkifSourceIsDirectory(directory);
        int dirCount = 0;
        File[] array = directory.listFiles();
        if (checkIfFolderIsEmpty(array)) {
            return 0;
        } else {
            for (File element : array) {
                if (element.isDirectory()) {
                    dirCount++;
                    String innerPath = element.getPath();
                    dirCount += countDirs(innerPath);
                }
            }
        }
        return dirCount;
    }

    public static void copy(String from, String to) throws IOException {
        File copyFrom = new File(from);
        File copyTo = new File(to);
        checkIfFileOrDirectoryExists(copyFrom);
        checkIfFileOrDirectoryExists(copyTo);
        if (!copyTo.isDirectory()) {
            throw new IllegalPathStateException("Destination path is not a directory");
        } else if (copyFrom.isFile()) {
            copyFileToDirectory(from, to);
        } else if (copyFrom.isDirectory()) {
            copyDirectory(from, to);
        }
    }

    public static void move(String from, String to) throws IOException {
        copy(from, to);
        File itemToDelete = new File(from);
        delete(itemToDelete);
    }

    private static void checkIfFileOrDirectoryExists(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("Directory does not exist");
        }
    }

    private static void checkifSourceIsDirectory(File directory) {
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Path is not a directory. Please choose directory");
        }
    }

    private static boolean checkIfFolderIsEmpty(File[] folder) {
        if (folder.length < 1) {
            return true;
        }
        return false;
    }

    private static void copyFileToDirectory(String from, String to) throws IOException {
        File copyFrom = new File(from);
        File copyTo = new File(to);
        String destinationFileName = copyFrom.getName();
        File copyToFile = new File(copyTo, destinationFileName);
        copyFileToFile(copyFrom, copyToFile);
    }

    private static void copyFileToFile(File from, File to) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(from);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(to);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        byte[] buffer = new byte[32 * 1024];
        int length;
        while ((length = bufferedInputStream.read(buffer)) > 0) {
            bufferedOutputStream.write(buffer, 0, length);
        }
        bufferedInputStream.close();
        bufferedOutputStream.close();
    }


    private static void copyDirectory(String from, String to) throws IOException {
        File copyFrom = new File(from);
        File copyTo = new File(to);
        if (!copyTo.exists()) {
            copyTo.mkdirs();
        }

        for (String element : copyFrom.list()) {
            File newFileFrom = new File(copyFrom, element);
            File newFileTo = new File(copyTo, element);
            if (newFileFrom.isDirectory()) {
                copyDirectory(newFileFrom.getAbsolutePath(), newFileTo.getAbsolutePath());
            } else {
                copyFileToFile(newFileFrom, newFileTo);
            }
        }
    }

    private static boolean delete(File itemToDelete) {
        File[] content = itemToDelete.listFiles();
        if (content != null) {
            for (File file : content) {
                delete(file);
            }
        }
        return itemToDelete.delete();
    }
}
