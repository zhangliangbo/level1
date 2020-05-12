package xxl.mp;

public class GeneralMessage extends MpMessage {

    private final String Content;
    private final String MsgId;

    public GeneralMessage(String toUserName, String fromUserName, Long createTime, String msgType, String content, String msgId) {
        super(toUserName, fromUserName, createTime, msgType);
        Content = content;
        MsgId = msgId;
    }

    public String getContent() {
        return Content;
    }

    public String getMsgId() {
        return MsgId;
    }
}
