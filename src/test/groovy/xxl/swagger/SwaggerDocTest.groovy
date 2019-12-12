package xxl.swagger

class SwaggerDocTest extends GroovyTestCase {
    void testSwaggerDoc() {
        def url = "https://test.xxlun.com/rice/v2/api-docs"
        println("xxl=" + SwaggerDoc.swaggerDoc(url, SwaggerDoc.Format.ascii))
        println("xxl=" + SwaggerDoc.swaggerDoc(url, SwaggerDoc.Format.markdown))
        println("xxl=" + SwaggerDoc.swaggerDoc(url, SwaggerDoc.Format.text))
        println("xxl=" + SwaggerDoc.swaggerDoc(url, SwaggerDoc.Format.ascii, "C:\\Users\\Admin\\Desktop\\aaa", false))
        println("xxl=" + SwaggerDoc.swaggerDoc(url, SwaggerDoc.Format.ascii, "C:\\Users\\Admin\\Desktop\\aaa", true))
    }
}
