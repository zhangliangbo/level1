package com.mcivicm.mathematica.image;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageCollageTest {
    @Test
    public void file() {
        InputStream is = ImageCollageTest.this.getClass().getResourceAsStream("/1.png");
        if (is == null) {
            System.out.println("not exist.");
        } else {
            System.out.println("exist.");
        }
    }

    @Test
    public void collage() throws IOException {
        BufferedImage[] origins = new BufferedImage[]{
                ImageIO.read(
                        ImageCollageTest.this.getClass().getResourceAsStream("/1.png")
                ),
                ImageIO.read(
                        ImageCollageTest.this.getClass().getResourceAsStream("/3.png")
                )
        };
        BufferedImage dest = ImageCollage.imageCollage(origins);
        System.out.println(ImageIO.write(dest,"png",new File("dest.png")));
    }
}
