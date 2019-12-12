package xxl.swagger

class SwaggerDocTest extends GroovyTestCase {
    void testSwaggerDoc() {
        def url = "https://test.xxlun.com/rice/v2/api-docs"
        println("xxl=" + SwaggerDoc.swaggerDoc(url, SwaggerDoc.Output.ascii))
        println("xxl=" + SwaggerDoc.swaggerDoc(url, SwaggerDoc.Output.markdown))
        println("xxl=" + SwaggerDoc.swaggerDoc(url, SwaggerDoc.Output.text))
        println("xxl=" + SwaggerDoc.swaggerDoc(url, SwaggerDoc.Output.ascii, "C:\\Users\\Admin\\Desktop\\aaa", false))
        println("xxl=" + SwaggerDoc.swaggerDoc(url, SwaggerDoc.Output.ascii, "C:\\Users\\Admin\\Desktop\\aaa", true))
    }
}
