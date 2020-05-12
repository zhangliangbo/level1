package xxl.mp;

public abstract class MpMessage {
    private final String ToUserName;
    private final String FromUserName;
    private final Long CreateTime;
    private final String MsgType;

    public MpMessage(String toUserName, String fromUserName, Long createTime, String msgType) {
        ToUserName = toUserName;
        FromUserName = fromUserName;
        CreateTime = createTime;
        MsgType = msgType;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public Long getCreateTime() {
        return CreateTime;
    }

    public String getMsgType() {
        return MsgType;
    }
}
