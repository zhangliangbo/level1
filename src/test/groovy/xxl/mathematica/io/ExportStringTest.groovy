package xxl.mathematica.io

class ExportStringTest extends GroovyTestCase {
    void testExportStringJson() {
        Map<String, String> map = new HashMap<>()
        map.put("a", "b")
        map.put("c", "d")
        map.put("e", "f")
        println(ExportString.exportStringJson(map))
    }

    void testExportStringXml() {
        XmlBean xmlBean = new XmlBean()
        xmlBean.name = "xxl"
        xmlBean.age = 18
        xmlBean.state = 12
        println(ExportString.exportStringXml(xmlBean))
    }
}
