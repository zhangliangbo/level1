package com.mcivicm.mathematica;

import org.junit.Test;

import java.io.File;

/**
 * Created by zhang on 2017/9/22.
 */

public class ParentDirectoryTest {
    @Test
    public void name() throws Exception {

        System.out.println(ParentDirectory.parentDirectory("C:\\Program Files\\"));
    }

    @Test
    public void name1() throws Exception {
        File file=new File("");
        System.out.println(file.getAbsolutePath());
    }
}
