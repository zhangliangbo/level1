package xxl.reactornetty;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class OneFrame {
    public static void main(String[] args) throws IOException {
        byte[] bytes = IOUtils.toByteArray(new FileInputStream("C:\\Users\\Admin\\Desktop\\frame.txt"));
        System.err.println("len:" + bytes.length);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        byte[] head = new byte[4];
        buffer.get(head);
        System.err.println(new String(head));
        System.err.println(buffer.get());
        System.err.println(buffer.get());
        System.err.println(buffer.getInt());
    }
}
