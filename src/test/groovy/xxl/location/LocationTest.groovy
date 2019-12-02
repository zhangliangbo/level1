package xxl.location

import xxl.mathematica.io.Import

class LocationTest extends GroovyTestCase {
    void testWxLocation() {
        Map<String, String> account = Import.importJsonAsString("D:\\zlb\\微信位置\\account.json")
        println(Location.wxLocation(Double.valueOf(account.get("lon")), Double.valueOf(account.get("lat")), account.get("key"), account.get("sk")))
    }
}
