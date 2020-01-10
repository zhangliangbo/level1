package xxl.mathematica.io

class ImportStringTest extends GroovyTestCase {
    void testImportString() {
        XmlBean xmlBean = ImportString.importStringXml("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<xmlBean state=\"12\">\n" +
                "    <name>xxl</name>\n" +
                "    <age>18</age>\n" +
                "    <goods>apple</goods>\n" +
                "    <goods>orange</goods>\n" +
                "    <goods>vegetable</goods>\n" +
                "</xmlBean>", XmlBean.class)
        println(xmlBean)
    }
}
