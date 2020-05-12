package xxl.mp;

import java.util.HashMap;
import java.util.Map;

/**
 * 公众号消息模版
 */
public class MpTemplateMessage {

    private final String touser;
    private final String template_id;
    private final String url;
    private final String topcolor;
    private final Map<String, MsgValue> data = new HashMap<>();

    public MpTemplateMessage(String touser, String template_id, String url, String topcolor) {
        this.touser = touser;
        this.template_id = template_id;
        this.url = url;
        this.topcolor = topcolor;
    }

    public String getTouser() {
        return touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public String getUrl() {
        return url;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void clear() {
        this.data.clear();
    }

    public MsgValue put(String key, MsgValue value) {
        return this.data.put(key, value);
    }
}
