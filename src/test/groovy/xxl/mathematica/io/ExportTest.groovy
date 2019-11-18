package xxl.mathematica.io

import xxl.mathematica.external.Pojo
import xxl.mathematica.io.excel.IExcel

class ExportTest extends GroovyTestCase {
    void testExportXlsx() {
        Export.exportXls(IExcel.POI, "C:\\Users\\zhang\\Desktop\\helloworld.xlsx", true, [new Pojo("zlb", 18, "男")])
    }
}
