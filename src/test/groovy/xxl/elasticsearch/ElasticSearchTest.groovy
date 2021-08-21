package xxl.elasticsearch

class ElasticSearchTest extends GroovyTestCase {

    @Override
    void setUp() throws Exception {
        ElasticSearch.use("http://localhost:9200", "elastic", "mcivicm")
    }

    void testMatch() {
        def obj = ElasticSearch.match("books", "author", "zlb")
        println(obj)
    }

    void testMatchAll() {
        def obj = ElasticSearch.matchAll("books")
        println(obj)
    }

    void testTerm() {
        def obj = ElasticSearch.term("books", "title.keyword", "Java编程思想")
        println(obj)
    }

    void testMapping() {
        def obj = ElasticSearch.mapping("books")
        println(obj)
    }

    void testGet() {
        def obj = ElasticSearch.get("books", "1")
        println(obj)
    }

    void testIndex() {
        def doc = ["id": "6", "title": "java核心卷", "language": "java", "author": "zlb", "price": 1.00, "publish_time": "2021-09-01", "description": "Java技术经典名著"]
        def res = ElasticSearch.index("books", "6", doc)
        println(res)
    }

    void testUpdate() {
        def doc = ["author": "zhangliangbo"]
        def res = ElasticSearch.update("books", "6", doc)
        println(res)
    }

    void testDelete() {
        def res = ElasticSearch.delete("books", "7")
        println(res)
    }

    void testBulkIndex() {
        def doc = ["id": "6", "title": "java核心卷", "language": "java", "author": "zlb", "price": 1.00, "publish_time": "2021-09-01", "description": "Java技术经典名著"]
        def doc1 = ["id": "7", "title": "mathematic cookbook", "language": "mathematica", "author": "zhangliangbo", "price": 1.00, "publish_time": "2021-07-01", "description": "Mathematica技术经典"]
        def res = ElasticSearch.bulkIndex("books", [doc, doc1])
        println(res)
    }

    void testBulkDelete() {
        def res = ElasticSearch.bulkDelete("books", ["1", "2", "3", "4", "5", "6", "7"])
        println(res)
    }

    void testBulkUpdate() {
        def doc = ["id": "6", "author": "zlb"]
        def doc1 = ["id": "7", "author": "zhangliangbo"]
        def res = ElasticSearch.bulkUpdate("books", [doc, doc1])
        println(res)
    }

    void testDeleteIndex() {
        def res = ElasticSearch.deleteIndex("books")
        println(res)
    }

    void testMatchAllByName() {
        def res = ElasticSearch.matchAllBy("books","id")
        println(res)
    }

}
