package xxl.mp;

public class EventMessage extends MpMessage {
    private final String Event;

    public EventMessage(String toUserName, String fromUserName, Long createTime, String msgType, String event) {
        super(toUserName, fromUserName, createTime, msgType);
        Event = event;
    }

    public String getEvent() {
        return Event;
    }
}
