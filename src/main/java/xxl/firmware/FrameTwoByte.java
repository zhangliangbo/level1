package xxl.firmware;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 数据帧
 */
public class FrameTwoByte {

    private static short num = 0x00;

    private byte head;
    private short len;//数据帧的总长度
    private short source;
    private short target;
    private short order;
    private byte type;
    private byte[] data;
    private byte check;

    private FrameTwoByte() {
        this.data = new byte[0];
    }

    public FrameTwoByte(byte head, short source, short target, byte type, byte[] content) {
        this.head = head;
        this.source = source;
        this.target = target;
        this.type = type;
        if (content == null) {
            this.data = new byte[0];
        } else {
            this.data = content;
        }
        this.len = (short) (11 + this.data.length);
    }

    /**
     * 解析数据帧
     *
     * @param frame
     * @return
     */
    public static FrameTwoByte parseFrame(byte[] frame) {
        if (frame == null || frame.length < 11) {
            return null;
        }
        //第二和第三字节表示数据帧长度
        int len = ByteBuffer.wrap(new byte[]{frame[1], frame[2]}).order(ByteOrder.BIG_ENDIAN).getShort() & 0xffff;
        if (len != frame.length) {
            return null;
        }
        byte myCheck = frame[0];
        for (int i = 1; i < frame.length - 1; i++) {
            myCheck += frame[i];
        }
        if (frame[frame.length - 1] != myCheck) {
            return null;
        }
        FrameTwoByte df = new FrameTwoByte();
        ByteBuffer buffer = ByteBuffer.wrap(frame).order(ByteOrder.BIG_ENDIAN);
        df.head = buffer.get();
        df.len = buffer.getShort();
        df.source = buffer.getShort();
        df.target = buffer.getShort();
        df.order = buffer.getShort();
        df.type = buffer.get();
        df.data = new byte[df.len - 11];
        buffer.get(df.data);
        df.check = buffer.get();
        return df;
    }

    /**
     * 数据没有头，补充头再解析
     *
     * @param frame
     * @param head
     * @return
     */
    public static FrameTwoByte parseFrameWithHead(byte[] frame, byte head) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[frame.length + 1]);
        buffer.put(head);
        buffer.put(frame);
        return parseFrame(buffer.array());
    }

    public static void main(String[] args) {
        parseFrameWithHead(new byte[]{1, 2, 3}, (byte) 4);
    }

    /**
     * 打包数据帧
     *
     * @return
     */
    public byte[] toFrame() {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[this.len]).order(ByteOrder.BIG_ENDIAN);
        buffer.put(head);
        buffer.putShort(len);
        buffer.putShort(source);
        buffer.putShort(target);
        order = num++;//序号自增1个
        buffer.putShort(order);
        buffer.put(type);
        buffer.put(data);
        //对以上数据求和，生成校验位
        this.check = head;
        for (int i = 1; i < buffer.limit(); i++) {
            this.check += buffer.get(i);
        }
        buffer.put(check);
        return buffer.array();
    }

    /**
     * 获取数据
     *
     * @return
     */
    public byte[] getData() {
        return data;
    }

    /**
     * 获取源地址
     *
     * @return
     */
    public short getSource() {
        return source;
    }

    /**
     * 获取目标地址
     *
     * @return
     */
    public short getTarget() {
        return target;
    }

    /**
     * 获取帧类型
     *
     * @return
     */
    public byte getType() {
        return type;
    }
}

