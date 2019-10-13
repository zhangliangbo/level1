package xxl.xjwt

import com.google.gson.Gson
import xxl.mathematica.Do
import xxl.mathematica.RandomInteger
import xxl.mathematica.function.Consumer

class ILabTest extends GroovyTestCase {
    void testGetUserInfo() {
        String json = new ILab().getUserInfo("15172384938", "mancivic")
        Map map = new Gson().fromJson(json, Map.class)
        println(map)
    }

    void testUploadState() {
        println(new ILab().uploadState("15172384938", "mancivic"))
    }

    void testUploadFile() {
        println(new ILab().uploadFile(new File("C:\\Users\\zhang\\Desktop\\comm.pdf")))
    }
//18674068268
    void testUploadResult() {
        Do.loop(new Consumer<Integer>() {
            @Override
            void accept(Integer integer) {
                println(new ILab().uploadResult("18674068268", "18674068268",
                        "社会舆情演化模型", 1, RandomInteger.randomInteger(100), System.currentTimeMillis(), RandomInteger.randomInteger(1, 10),
                        new File("C:\\Users\\zhang\\Desktop\\comm.pdf")))
                Thread.sleep(10000)
            }
        }, 3)
    }
}
