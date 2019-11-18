package xxl.mathematica.io

import xxl.mathematica.external.Pojo
import xxl.mathematica.io.excel.IExcel

class ExportTest extends GroovyTestCase {
    void testExportXlsx() {
        Export.exportExcel(IExcel.POI, "C:\\Users\\zhang\\Desktop\\helloworld.xls", true, [new Pojo("zlb", 18, "男")])
    }
}
