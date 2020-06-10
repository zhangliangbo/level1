package xxl.rabbitmq;

/**
 * 投递失败打回的消息
 */
public class Return {
    private int code;
    private String text;
    private String exchange;
    private String routingKey;
    private byte[] body;

    public Return(int code, String text, String exchange, String routingKey, byte[] body) {
        this.code = code;
        this.text = text;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.body = body;
    }

    public int code() {
        return code;
    }

    public String text() {
        return text;
    }

    public String exchange() {
        return exchange;
    }

    public String routingKey() {
        return routingKey;
    }

    public byte[] body() {
        return body;
    }

    @Override
    public String toString() {
        return "Return{" +
                "code='" + code + '\'' +
                ", text='" + text + '\'' +
                ", exchange='" + exchange + '\'' +
                ", routingKey='" + routingKey + '\'' +
                ", body=" + new String(body) +
                '}';
    }
}
