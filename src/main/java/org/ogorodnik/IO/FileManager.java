package org.ogorodnik.IO;

import java.io.*;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;

public class FileManager {

    private static String pathParser(String path) {
        byte[] pathArray = path.getBytes();
        for (int i=0; i< pathArray.length; i++) {
            if ((char) pathArray[i] == '\\') {
                pathArray[i] = (byte)'/';
            }
        }
        return Arrays.toString(pathArray);
    }

    public static int countFiles(String path) throws FileNotFoundException {
        File[] array = listDirectory(path);
        int filecount = 0;
        if (checkIfFolderIsEmpty(array)) {
            return 0;
        } else {
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
    }

    public static int countDirs(String path) throws FileNotFoundException {
        File[] array = listDirectory(path);
        int dirCount = 0;
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
        if (copyFrom.isFile()) {
            copyFileToDirectory(from, to);
        } else if (copyFrom.isDirectory()) {
            copyDirectory(copyFrom, copyTo);
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
        return folder.length < 1;
    }

    private static void copyFileToDirectory(String from, String to) throws IOException {
        File copyFrom = new File(from);
        File copyTo = new File(to);
        if (!copyTo.exists()) {
            copyTo.mkdirs();
        }
        String destinationFileName = copyFrom.getName();
        File copyToFile = new File(copyTo, destinationFileName);
        copyFileToFile(copyFrom, copyToFile);
    }

    private static void copyFileToFile(File from, File to) throws IOException {
        if (!from.canRead()) {
            throw new AccessDeniedException("Please check " + from + " file, it can't be read");
        }
        try (FileInputStream fileInputStream = new FileInputStream(from);
             FileOutputStream fileOutputStream = new FileOutputStream(to)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, length);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Exception while copying the file", exception);
        }
    }


    private static void copyDirectory(File copyFrom, File copyTo) throws IOException {
        if (!copyTo.exists()) {
            copyTo.mkdirs();
        }
        for (String element : copyFrom.list()) {
            File newFileFrom = new File(copyFrom, element);
            File newFileTo = new File(copyTo, element);
            if (newFileFrom.isDirectory()) {
                copyDirectory(newFileFrom, newFileTo);
            } else {
                copyFileToFile(newFileFrom, newFileTo);
            }
        }
    }

    private static void delete(File itemToDelete) {
        File[] content = itemToDelete.listFiles();
        if (content != null) {
            for (File file : content) {
                delete(file);
            }
        }
        itemToDelete.delete();
    }

    private static File[] listDirectory(String path) throws FileNotFoundException {
        File directory = new File(path);
        checkIfFileOrDirectoryExists(directory);
        checkifSourceIsDirectory(directory);
        return directory.listFiles();
    }
}
