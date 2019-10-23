package xxl.mathematica.string

import xxl.mathematica.io.ImportText

class StringCountTest extends GroovyTestCase {
    void testStringCount() {
        String file = ImportText.importText(new File("D:\\xxlun\\xxlun\\netlogo\\20190921\\netlogo-vue\\src\\components\\Ask.vue"))
        println(StringCount.stringCount(file, "\n"))
    }
}
