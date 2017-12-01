package com.mcivicm.mathematica;

import org.junit.Test;

/**
 * Created by zhang on 2017/9/22.
 */

public class FileExtensionTest {
    @Test
    public void name() throws Exception {
        System.out.println("ext:"+FileExtension.fileExtension("file"));
        System.out.println("ext:"+FileExtension.fileExtension("file."));
        System.out.println("ext:"+FileExtension.fileExtension("file.txt"));
        System.out.println("ext:"+FileExtension.fileExtension("file.tar.gz"));
        System.out.println("ext:"+FileExtension.fileExtension("C:\\file\\file\\file.tar.gz"));
        System.out.println("ext:"+FileExtension.fileExtension("C:\\file\\file\\file.txt"));
        System.out.println("ext:"+FileExtension.fileExtension("C:\\file\\file\\file"));
        System.out.println("ext:"+FileExtension.fileExtension(null));
    }
}
