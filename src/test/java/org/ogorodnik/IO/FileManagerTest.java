package org.ogorodnik.IO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class FileManagerTest {

    @BeforeEach
    public void before() throws IOException {
        new File("TestData").mkdir();
        new File("TestData/DataForCount").mkdir();
        new File("TestData/DataForCount/InnerDirectory1").mkdir();
        new File("TestData/DataForCount/InnerDirectory1/InnerDIrectory2").mkdir();
        new File("TestData/DataForCopy").mkdir();
        new File("TestData/DataForCopy/from").mkdir();
        new File("TestData/DataForCopy/to").mkdir();
        new File("TestData/DataForCopy/from/InnerFolder").mkdir();
        new File("TestData/DataForMove").mkdir();
        new File("TestData/DataForMove/from").mkdir();
        new File("TestData/DataForMove/to").mkdir();
        new File("TestData/DataForMove/from/InnerFolder").mkdir();
        createFilesWithContent("TestData/DataForCount/File1.txt");
        createFilesWithContent("TestData/DataForCount/File2.txt");
        createFilesWithContent("TestData/DataForCount/File3.txt");
        createFilesWithContent("TestData/DataForCount/InnerDirectory1/File4.txt");
        createFilesWithContent("TestData/DataForCopy/from/File1.txt");
        createFilesWithContent("TestData/DataForCopy/from/File2.txt");
        createFilesWithContent("TestData/DataForCopy/from/InnerFolder/File3.txt");
        createFilesWithContent("TestData/DataForMove/from/File1.txt");
        createFilesWithContent("TestData/DataForMove/from/InnerFolder/File1.txt");
    }

    private void createFilesWithContent(String path) throws IOException {
        new File(path).createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
        bufferedWriter.write("This is a test file");
        bufferedWriter.newLine();
        bufferedWriter.write("It's made to test FileManager");
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    @Test
    public void testCountFiles() throws FileNotFoundException {
        assertEquals(4, FileManager.countFiles("./TestData/DataForCount"));
    }

    @Test
    public void tesCountFilesWhenDirectoryDoesNotExist() {
        assertThrows(FileNotFoundException.class, () -> {
            FileManager.countFiles("./Test");
        });
    }

    @Test
    public void testCountFilesWhenSourceIsFile() throws FileNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.countFiles("./TestData/DataForCount/File1.txt");
        });
    }

    @Test
    public void testCountDIrs() throws FileNotFoundException {
        assertEquals(2, FileManager.countDirs("./TestData/DataForCount"));
    }

    @Test
    public void tesCountDirsWhenDirectoryDoesNotExist() {
        assertThrows(FileNotFoundException.class, () -> {
            FileManager.countDirs("./Test");
        });
    }

    @Test
    public void testCountDirsWhenSourceIsFile() throws FileNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.countDirs("./TestData/DataForCount/File1.txt");
        });
    }

    @Test
    public void testCopyFile() throws IOException {
        FileManager.copy("./TestData/DataForCopy/from/File1.txt", "./TestData/DataForCopy/to/");
        assertTrue((new File("./TestData/DataForCopy/to/File1.txt")).exists());
    }

    @Test
    public void testCopyDirectory() throws IOException {
        FileManager.copy(".//TestCopy//from//sourceFolder", ".//TestCopy//to//destinationFolder//");
        assertTrue((new File(".//TestCopy//to//destinationFolder//File1.rtf")).exists());
        assertTrue((new File(".//TestCopy//to//destinationFolder//innerFolder")).exists());
        assertTrue((new File(".//TestCopy//to//destinationFolder//innerFolder")).isDirectory());
        assertTrue((new File(".//TestCopy//to//destinationFolder//innerFolder//File1.rtf")).exists());
    }

    @Test
    public void testMove() throws IOException {
        assertTrue((new File(".//TestCopy//from//sourceToMove")).exists());
        assertTrue((new File(".//TestCopy//from//sourceToMove//File1.rtf")).exists());
        FileManager.move(".//TestCopy//from//sourceToMove", ".//TestCopy//to//destinationOfMovedFolder");
        assertFalse((new File(".//TestCopy//from//sourceToMove")).exists());
        assertFalse((new File(".//TestCopy//from//sourceToMove//File1.rtf")).exists());
        assertTrue((new File(".//TestCopy//to//destinationOfMovedFolder")).exists());
    }
}
