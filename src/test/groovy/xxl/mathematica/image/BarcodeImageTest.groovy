package xxl.mathematica.image

import com.google.zxing.client.j2se.GUIRunner

class BarcodeImageTest extends GroovyTestCase {
    void testBarcodeImage() {
        println(BarcodeImage.barcodeImage("hello world", 250, "C:\\Users\\zhang\\Desktop\\hello.png"))
    }

}
