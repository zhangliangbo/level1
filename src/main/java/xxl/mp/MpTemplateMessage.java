package xxl.mp;

/**
 * 公众号消息模版
 */
public class  MpTemplateMessage<T> {

    private final String touser;
    private final String template_id;
    private final String url;
    private final String topcolor;
    private final T data;

    public MpTemplateMessage(String touser, String template_id, String url, String topcolor, T data) {
        this.touser = touser;
        this.template_id = template_id;
        this.url = url;
        this.topcolor = topcolor;
        this.data = data;
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

    public T getData() {
        return data;
    }
}
