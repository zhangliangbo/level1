package xxl.mathematica.string

import xxl.mathematica.io.ImportText

class StringCasesTest extends GroovyTestCase {
    void testStringCases() {
        println(StringCases.stringCases("the cat in the hat", "a.*e"))
    }

    void testUseFile() {
        String file = ImportText.importText(new File("D:\\xxlun\\xxlun\\netlogo\\20190921\\netlogo-vue\\src\\components\\Ask.vue"))
        println(file)
        println(StringCases.stringCases(file, "\n"))
    }

    void test1() {
        println(StringCases.stringCases("abcdabcdcd", "abc|cd"))
    }
}
