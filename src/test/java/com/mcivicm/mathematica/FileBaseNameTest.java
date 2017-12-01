package com.mcivicm.mathematica;

import org.junit.Test;

/**
 * Created by zhang on 2017/9/22.
 */

public class FileBaseNameTest {
    @Test
    public void name() throws Exception {
        System.out.println(FileBaseName.fileBaseName("file"));
        System.out.println(FileBaseName.fileBaseName("file.tar.gz"));
        System.out.println(FileBaseName.fileBaseName("file."));
        System.out.println(FileBaseName.fileBaseName("file.txt"));
        System.out.println(FileBaseName.fileBaseName("file.txt"));
        System.out.println(FileBaseName.fileBaseName("C:\\Users\\zhang\\Rdrnss\\Code\\Flavors\\app\\release\\file.txt"));
        System.out.println(FileBaseName.fileBaseName(null));

    }
}
