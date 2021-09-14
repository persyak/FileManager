package org.ogorodnik.IO;

import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


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
        createFilesWithContent("TestData/DataForCount/File1.txt", "TestData/DataForCount/File1.txt file");
        createFilesWithContent("TestData/DataForCount/File2.txt", "TestData/DataForCount/File2.txt");
        createFilesWithContent("TestData/DataForCount/File3.txt", "TestData/DataForCount/File3.txt");
        createFilesWithContent("TestData/DataForCount/InnerDirectory1/File4.txt", "TestData/DataForCount/InnerDirectory1/File4.txt");
        createFilesWithContent("TestData/DataForCopy/from/File1.txt", "TestData/DataForCopy/from/File1.txt");
        createFilesWithContent("TestData/DataForCopy/from/File2.txt", "TestData/DataForCopy/from/File2.txt");
        createFilesWithContent("TestData/DataForCopy/from/InnerFolder/File3.txt", "TestData/DataForCopy/from/InnerFolder/File3.txt");
        createFilesWithContent("TestData/DataForMove/from/File1.txt", "TestData/DataForMove/from/File1.txt");
        createFilesWithContent("TestData/DataForMove/from/InnerFolder/File1.txt", "TestData/DataForMove/from/InnerFolder/File1.txt");
    }

    private void createFilesWithContent(String path, String text) throws IOException {
        new File(path).createNewFile();
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));
        bufferedWriter.write(text);
        bufferedWriter.newLine();
        bufferedWriter.write(text);
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    private List<String> createListOfFileContent(String file) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while(bufferedReader.ready()){
            list.add(bufferedReader.readLine());
        }
        return list;
    }

    private boolean checkIfTwoFilesAreEqual(List<String> list1, List<String> list2) throws IOException {
        return list1.equals(list2);
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
        assertTrue(checkIfTwoFilesAreEqual(createListOfFileContent("./TestData/DataForCopy/from/File1.txt"),
                createListOfFileContent("./TestData/DataForCopy/to/File1.txt")));
    }

    @Test
    public void testCopyDirectory() throws IOException {
        FileManager.copy("./TestData/DataForCopy/from", "./TestData/DataForCopy/to");
        assertTrue((new File("./TestData/DataForCopy/to/File1.txt")).exists());
        assertTrue((new File("./TestData/DataForCopy/to/File2.txt")).exists());
        assertTrue((new File("./TestData/DataForCopy/to/InnerFolder")).exists());
        assertTrue((new File("./TestData/DataForCopy/to/InnerFolder")).isDirectory());
        assertTrue((new File("./TestData/DataForCopy/to/InnerFolder/File3.txt")).exists());
        assertTrue(checkIfTwoFilesAreEqual(createListOfFileContent("./TestData/DataForCopy/from/File1.txt"),
                createListOfFileContent("./TestData/DataForCopy/to/File1.txt")));
        assertTrue(checkIfTwoFilesAreEqual(createListOfFileContent("./TestData/DataForCopy/from/File2.txt"),
                createListOfFileContent("./TestData/DataForCopy/to/File2.txt")));
        assertTrue(checkIfTwoFilesAreEqual(createListOfFileContent("./TestData/DataForCopy/from/InnerFolder/File3.txt"),
                createListOfFileContent("./TestData/DataForCopy/to/InnerFolder/File3.txt")));
        assertFalse(checkIfTwoFilesAreEqual(createListOfFileContent("./TestData/DataForCopy/from/File1.txt"),
                createListOfFileContent("./TestData/DataForCopy/to/File2.txt")));
    }

    @Test
    public void testMove() throws IOException {
        assertTrue((new File("./TestData/DataForMove/from")).exists());
        assertTrue((new File("./TestData/DataForMove/from/File1.txt")).exists());
        List<String> sourceList = createListOfFileContent("./TestData/DataForMove/from/File1.txt");
        FileManager.move("./TestData/DataForMove/from", "./TestData/DataForMove/to");
        assertFalse((new File("./TestData/DataForMove/from")).exists());
        assertFalse((new File("./TestData/DataForMove/from/File1.txt")).exists());
        assertTrue((new File("./TestData/DataForMove/to/File1.txt")).exists());
        assertTrue((new File("./TestData/DataForMove/to")).exists());
        List<String> destinationList = createListOfFileContent("./TestData/DataForMove/to/File1.txt");
        assertTrue(checkIfTwoFilesAreEqual(sourceList, destinationList));
    }

    @AfterEach
    public void after(){
        new File("TestData/DataForCount/File1.txt").delete();
        new File("TestData/DataForCount/File2.txt").delete();
        new File("TestData/DataForCount/File3.txt").delete();
        new File("TestData/DataForCount/InnerDirectory1/File4.txt").delete();
        new File("TestData/DataForCopy/from/File1.txt").delete();
        new File("TestData/DataForCopy/from/File2.txt").delete();
        new File("TestData/DataForCopy/from/InnerFolder/File3.txt").delete();
        new File("TestData/DataForCopy/to/File1.txt").delete();
        new File("TestData/DataForCopy/to/File2.txt").delete();
        new File("TestData/DataForCopy/to/InnerFolder/File3.txt").delete();
        new File("TestData/DataForMove/from/File1.txt").delete();
        new File("TestData/DataForMove/from/InnerFolder/File1.txt").delete();
        new File("TestData/DataForMove/to/File1.txt").delete();
        new File("TestData/DataForMove/to/InnerFolder/File1.txt").delete();
        new File("TestData/DataForCount/InnerDirectory1/InnerDIrectory2").delete();
        new File("TestData/DataForCount/InnerDirectory1").delete();
        new File("TestData/DataForCount").delete();
        new File("TestData/DataForCopy/from/InnerFolder").delete();
        new File("TestData/DataForCopy/from").delete();
        new File("TestData/DataForCopy/to/InnerFolder").delete();
        new File("TestData/DataForCopy/to").delete();
        new File("TestData/DataForCopy").delete();
        new File("TestData/DataForMove/from/InnerFolder").delete();
        new File("TestData/DataForMove/from").delete();
        new File("TestData/DataForMove/to/InnerFolder").delete();
        new File("TestData/DataForMove/to").delete();
        new File("TestData/DataForMove").delete();
        new File("TestData").delete();
    }


}
