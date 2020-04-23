package xxl.firmware;

/**
 * 数据缓冲
 */
public interface ICache {

    byte head();

    void append(byte[] data);

    boolean addOnNewFrameListener(OnNewFrameListener listener);

    boolean removeOnNewFrameListener(OnNewFrameListener listener);

    interface OnNewFrameListener {
        void onNewFrame(byte[] frame);
    }

}
