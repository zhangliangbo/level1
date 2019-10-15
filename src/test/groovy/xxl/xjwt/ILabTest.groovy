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
        println(new ILab().uploadState("zhangliangbo"))
    }

    void testUploadFile() {
        println(new ILab().uploadFile(new File("C:\\Users\\zhang\\Desktop\\comm.pdf")))
    }
//18674068268
    void testUploadResult() {
        Do.loop(new Consumer<Integer>() {
            @Override
            void accept(Integer integer) {
                println(new ILab().uploadResult("15172384938", "mancivic",
                        "社会舆情演化模型", 1, RandomInteger.randomInteger(100), System.currentTimeMillis(), RandomInteger.randomInteger(1, 10),
                        new File("C:\\Users\\zhang\\Desktop\\comm.pdf")))
                Thread.sleep(10000)
            }
        }, 3)
    }

    void testGetToken() {
        println(new ILab().getToken("AAABbcnBHUABAAAAAAABjf0%3D.J%2FSmXAjX%2FILMYSx8utVvqJOa3o7nECCrvc5GybYqxvJerQbYKuOINvwqJg%2F4020dltcaRNidBSpf%2FQOwEUYIYB3xbRp70KFCPhQcSefcrshNOvr4c7Q%2BNen%2BXHwUxoc9EjEzk0hpGxoeknJQXXnhzqWEjPdq7Iv4Ilb4p6OP0S1soezxu8ws674F2is5vb%2FzJRNoTDA6wgK6iRJxYtJ4aXs%2BWh9y0Q99jenEAa7h1iLXi9b7ThWSkZBu67z6RmNytucGm5%2FXDdp9QmbcAvLNGVJ7fayHJGgoZwMk64eae%2B3RSXdLgTx39Y2Fb5lwqYyDBAFU%2B7JZebUWGqvcLeTvXg%3D%3D.TXZ0eItIw8ct%2Bg9wLVF0RybOgA5BjYmcQGBbuRsTr7g%3D"))
    }
}
