package org.ogorodnik.IO;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class FileManagerTest {

    @Test
    public void testCountFiles() throws FileNotFoundException {
        assertEquals(4, FileManager.countFiles(".//TestData"));
    }

    @Test
    public void tesCountFilesWhenDirectoryDoesNotExist(){
        assertThrows(FileNotFoundException.class, () -> {
            FileManager.countFiles(".//Test");
        });
    }

    @Test
    public void testCountFilesWhenSourceIsFile() throws FileNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.countFiles(".//TestData//File1.rtf");
        });
    }

    @Test
    public void testCountDIrs() throws FileNotFoundException {
        assertEquals(2, FileManager.countDirs(".//TestData"));
    }

    @Test
    public void tesCountDirsWhenDirectoryDoesNotExist(){
        assertThrows(FileNotFoundException.class, () -> {
            FileManager.countDirs(".//Test");
        });
    }

    @Test
    public void testCountDirsWhenSourceIsFile() throws FileNotFoundException {
        assertThrows(IllegalArgumentException.class, () -> {
            FileManager.countDirs(".//TestData//File1.rtf");
        });
    }

    @Test
    public void testCopyFile() throws IOException {
        FileManager.copy(".//TestCopy//from//File1.rtf", ".//TestCopy//to//");
        assertTrue((new File(".//TestCopy//to//File1.rtf")).exists());
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
