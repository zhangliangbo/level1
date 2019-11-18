package xxl.mathematica.io

import xxl.mathematica.external.Pojo

class ExportTest extends GroovyTestCase {
    void testExportXlsx() {
        println(Export.exportExcel("C:\\Users\\zhang\\Desktop\\helloworld.xlsx", true, [[new Pojo("zlb", 18, "男")]]))
    }
}
