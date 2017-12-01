package com.mcivicm.mathematica;

import org.junit.Test;

/**
 * Created by zhang on 2017/9/22.
 */

public class DirectoryNameTest {
    @Test
    public void name() throws Exception {
        System.out.println("dir:" + DirectoryName.directoryName("C:\\a\\b\\c\\d.txt", 1));
        System.out.println("dir:" + DirectoryName.directoryName("C:\\a\\b\\c\\d.txt", 2));
        System.out.println("dir:" + DirectoryName.directoryName("C:\\a\\b\\c\\d.txt", 3));
        System.out.println("dir:" + DirectoryName.directoryName("C:\\a\\b\\c\\d.txt", 4));
        System.out.println("dir:" + DirectoryName.directoryName("C:\\a\\b\\c\\d.txt", 5));
        System.out.println("dir:" + DirectoryName.directoryName("C:\\a\\b\\c\\d.txt", 6));
        System.out.println("dir:" + DirectoryName.directoryName("a\\b\\c\\d.txt", 1));
        System.out.println("dir:" + DirectoryName.directoryName("a\\b\\c\\d.txt", 2));
        System.out.println("dir:" + DirectoryName.directoryName("a\\b\\c\\d.txt", 3));
        System.out.println("dir:" + DirectoryName.directoryName("a\\b\\c\\d.txt", 4));
        System.out.println("dir:" + DirectoryName.directoryName("a\\b\\c\\d.txt", 5));
        System.out.println("dir:" + DirectoryName.directoryName("\\a\\b\\c\\d.txt", 1));
        System.out.println("dir:" + DirectoryName.directoryName("\\a\\b\\c\\d.txt", 2));
        System.out.println("dir:" + DirectoryName.directoryName("\\a\\b\\c\\d.txt", 3));
        System.out.println("dir:" + DirectoryName.directoryName("\\a\\b\\c\\d.txt", 4));
        System.out.println("dir:" + DirectoryName.directoryName("\\a\\b\\c\\d.txt", 5));
        System.out.println("dir:" + DirectoryName.directoryName("C:\\Program Files\\"));
    }
}
