package xxl.division

import xxl.mathematica.Rule

import java.util.function.Predicate

class DivisionTest extends GroovyTestCase {
    void testDivision() {
        println(Division.division(2, new Predicate<Rule<String, String>>() {
            @Override
            boolean test(Rule<String, String> stringStringRule) {
                return stringStringRule.getValue().equals("湖北省")
            }
        }, new Predicate<Rule<String, String>>() {
            @Override
            boolean test(Rule<String, String> stringStringRule) {
                return stringStringRule.getValue().equals("武汉市")
            }
        }))
    }
}
