package xxl.firmware;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 缓存所有的帧
 */
public class FrameContainer implements ICache {

    private List<Byte> container = new ArrayList<>();
    private Set<OnNewFrameListener> onNewFrameListeners = new HashSet<>();
    private byte head;
    private int max;

    /**
     * @param head 帧头
     * @param max  最大缓存容量
     */
    public FrameContainer(byte head, int max) {
        this.head = head;
        this.max = max;
    }

    /**
     * @param head               帧头
     * @param max                最大缓存
     * @param onNewFrameListener 新帧回调
     */
    public FrameContainer(byte head, int max, OnNewFrameListener onNewFrameListener) {
        this.head = head;
        this.max = max;
        this.onNewFrameListeners.add(onNewFrameListener);
    }

    @Override
    public byte head() {
        return this.head;
    }

    @Override
    public void append(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return;
        }
        //字节加入到容器中
        for (byte b : bytes) {
            container.add(b);
        }
        //开始遍历查找帧数据
        int lastHeadIndex = -1;
        List<Byte> currentFrame = new ArrayList<>();
        for (int i = 0; i < container.size(); i++) {
            if (container.get(i) == head) {
                //帧头标记
                if (currentFrame.size() < 3) {
                    if (i + 2 < container.size()) {
                        int len = getLen(new byte[]{container.get(i + 1), container.get(i + 2)});
                        //如果实际长度过大，说明不是有效帧头
                        if (len <= container.size() - i) {
                            //记录帧头位置
                            lastHeadIndex = i;
                            //加入帧头
                            currentFrame.add(container.get(i));
                        }
                    }
                } else {
                    //本帧已经有帧头，又出现帧头
                    int len = getLen(new byte[]{currentFrame.get(1), currentFrame.get(2)});
                    //本帧的实际长度和给定的长度相同，是个完整的帧
                    if (len == currentFrame.size()) {
                        //发送本帧数据
                        tryFlushFrame(currentFrame);
                        //记录帧头位置
                        lastHeadIndex = i;
                        //加入帧头
                        currentFrame.add(container.get(i));
                    } else if (len > currentFrame.size()) {
                        //给定的帧长大于实际帧长，说明该字节处于帧中。
                        //直接加到本帧
                        currentFrame.add(container.get(i));
                    } else {
                        //给定的帧长小于实际帧长，说明数据无效
                        //清空本帧
                        currentFrame.clear();
                        //记录帧头位置
                        lastHeadIndex = i;
                        //加入帧头
                        currentFrame.add(container.get(i));
                    }
                }
            } else {
                //非帧头标记
                if (currentFrame.size() > 0 && currentFrame.get(0) == head) {
                    //已经有帧头数据，添加后续数据
                    currentFrame.add(container.get(i));
                }
            }
        }
        //检查最后一帧是否完整
        if (currentFrame.size() > 2 && currentFrame.get(0) == head) {
            int len = getLen(new byte[]{currentFrame.get(1), currentFrame.get(2)});
            //如果给定的帧长和实际的帧长相同，则最后一帧是完整的
            if (len == currentFrame.size()) {
                tryFlushFrame(currentFrame);
                lastHeadIndex = container.size();
            }
        }
        //超过限制时全部清空
        if (container.size() > max) {
            lastHeadIndex = container.size();
        }
        //清空最后一个帧头前面的所有数据，最后一帧完整时，所有数据都删除
        if (lastHeadIndex > 0 && lastHeadIndex <= container.size()) {
            //从最后开始删除
            for (int i = lastHeadIndex - 1; i >= 0; i--) {
                container.remove(i);
            }
        }
    }

    @Override
    public boolean addOnNewFrameListener(OnNewFrameListener listener) {
        return this.onNewFrameListeners.add(listener);
    }

    @Override
    public boolean removeOnNewFrameListener(OnNewFrameListener listener) {
        return this.onNewFrameListeners.remove(listener);
    }

    private void tryFlushFrame(List<Byte> frameList) {
        if (frameList.size() > 0 && frameList.get(0) == head) {
            byte[] frame = new byte[frameList.size()];
            for (int j = 0; j < frameList.size(); j++) {
                frame[j] = frameList.get(j);
            }
            for (OnNewFrameListener listener : onNewFrameListeners) {
                listener.onNewFrame(frame);
            }
            frameList.clear();
        }
    }

    private int getLen(byte[] len) {
        if (len.length == 2) {
            return ByteBuffer.wrap(new byte[]{len[0], len[1]}).order(ByteOrder.BIG_ENDIAN).getShort() & 0xffff;
        } else {
            throw new IllegalStateException("len must be 2.");
        }
    }
}

