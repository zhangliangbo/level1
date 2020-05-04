package xxl.mathematica.integration

import xxl.mathematica.function.Function

class NIntegrateTest extends GroovyTestCase {
    void testNIntegrate() {
        println(NIntegrate.nIntegrate(new Function<Double, Double>() {
            @Override
            Double apply(Double aDouble) {
                return Math.sin(Math.sin(aDouble))
            }
        }, 0, 2))
    }
}
