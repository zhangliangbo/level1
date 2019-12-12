package xxl.adoc

import xxl.mathematica.external.External
import xxl.mathematica.string.StringSplit

class AdocTest extends GroovyTestCase {

    void testCommand() {
        //可以尝试用本地的
        String cmd = new String(External.runProcess("where asciidoctorj"))
        List<String> cmds = StringSplit.stringSplit(cmd, "\r\n")
        println(cmds)
        if (cmds.size() > 0) {
            println(new String(External.runProcess(cmds.get(1) + " " + "-b pdf " + "C:\\Users\\Admin\\Desktop\\rice.adoc")))
        }
    }

    void testConvertFile() {
        println(Adoc.convertFile("C:\\Users\\Admin\\Desktop\\rice.adoc", Adoc.Output.html))
    }
}
