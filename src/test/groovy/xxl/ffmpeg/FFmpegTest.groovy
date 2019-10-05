package xxl.ffmpeg

class FFmpegTest extends GroovyTestCase {
    void testGetLicense() {
        println(FFmpeg.getInstance().getLicense())
    }
}
