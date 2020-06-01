package xxl.xjwt

import com.google.gson.Gson
import xxl.mathematica.function.Consumer
import xxl.mathematica.random.RandomInteger

class ILabTest extends GroovyTestCase {
    String name = "********";
    String pwd = "**********"

    void testGetUserInfo() {
        String json = new ILab().getUserInfo(name, pwd)
        Map map = new Gson().fromJson(json, Map.class)
        println(map)
    }

    void testUploadState() {
        println(new ILab().uploadState(name))
    }

    void testUploadFile() {
        println(new ILab().uploadFile(new File("C:\\Users\\zhang\\Desktop\\comm.pdf")))
    }

    void testUploadResult() {
        Do.loop(new Consumer<Integer>() {
            @Override
            void accept(Integer integer) {
                println(new ILab().uploadResult(name, pwd,
                        "社会舆情演化模型", 1, RandomInteger.randomInteger(100), System.currentTimeMillis(), RandomInteger.randomInteger(1, 10),
                        new File("C:\\Users\\zhang\\Desktop\\comm.pdf")))
                Thread.sleep(10000)
            }
        }, 3)
    }

    void testGetToken() {
        println(new ILab().getToken("AAABbc47ktwBAAAAAAABjf0%3D.jRnYV%2B1H0aVgyRM4I4tX5Pgu5nUFfb5PTsxrMfbVnzp4PY6XxLwl9ujaaKhe9sJDHTSZjsoJnuW88AYMttPipc5uIwCrh96O2%2BnLK2%2Fbj67%2FTj2e4LDGJdMh%2BoviDscyH4Qyx9opn109PEJzNBDniaDTGs1EPXCpxza3CHIG%2B9%2BUzytKoIKOyT8gg7%2B9Wu%2Bo63HdzJQfFLWST2e7cLGMIVyyDaUZDBv6v3m7oIJS8TtwRMQ51pdJ4kexGlcitMhDFi8dQD2sZkg3c0IjbagM8%2B0Vf%2FSqv2OwZ4P101nGTJcY%2FnH2Iercx2LDog5wjDWtCV%2BRwvSIPQeQcOufUin1cA%3D%3D.GAD%2Fn%2B%2BqyWBwSizEv%2FpSchV0xPL%2FqsYNr6BxlG7k8xk%3D"))
    }

    void testUploadResultFromToken() {
        println(new ILab().uploadResultFromToken("AAABbc1aMg8BAAAAAAABjf0%3D.MMfTApI7IJidFfLWYila1djcdVF6KZPrPk4rTAaydCLHttkWTzPB9lCc9SJcZzQp0j4xjr2RSN728P2arTSmRa3J3Ih5um7V6zvo1EuST%2Fk9mciM9DAcCAfVUdW%2FbTAK4Lq4ksqn7YLFBhYgHx5d%2F1hBEs2k%2Fi7jXviQMsLpr8i3EefzYk6V63YZ74LMUog2aBrfGcOhe270xxqCS2XDxEgXcUIprABUsV5hU8JQXJ7Bc7xPqaCxYJJiZQ39Kz8tYT2scebXsAimedk7Q0M12v9sldFj6SQAUgU7pe9CsjwhIgUnNil1BVpsdKcsFRJzVLxXIV8qg6%2BDWX6LluAe%2BA%3D%3D.SieRzlIXZZFyFujFiEplequnupBcCv7tZL9TDzFbvd8%3D",
                "token", 1, RandomInteger.randomInteger(100), System.currentTimeMillis(), RandomInteger.randomInteger(1, 10),
                new File("C:\\Users\\zhang\\Desktop\\comm.pdf")))
    }
}
