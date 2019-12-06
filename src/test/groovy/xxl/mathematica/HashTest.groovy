package xxl.mathematica

class HashTest extends GroovyTestCase {
    void testHashString() {
        println(Hash.encodeHexString(Hash.hashString("The quick brown fox jumps over the lazy dog", Hash.Algorithm.SHA256)))
    }
}
