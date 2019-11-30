package xxl.sms

import xxl.mathematica.RandomInteger
import xxl.mathematica.io.Import
import xxl.mathematica.string.StringRiffle

class SendSmsTest extends GroovyTestCase {

    void testAliVerificationCode() {
        def account = Import.importJsonAsString("D:\\zlb\\公司ecs\\account.json")
        def card = Import.importJsonAsString("D:\\zlb\\公司ecs\\card.json")
        def code = Import.importJsonAsString("D:\\zlb\\公司ecs\\code.json")
        println(account)
        println(card)
        println(code)
        println(SendSms.aliSms(
                card.get("phone"),
                card.get("sign"),
                card.get("template"),
                ["name": card.get("phone"), "code": String.valueOf(StringRiffle.stringRiffle(RandomInteger.randomInteger(0, 9, 4), ""))],
                account.get("region"),
                account.get("key"),
                account.get("secret")
        ))
    }
}
