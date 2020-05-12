package xxl.mp;

public class MsgValue {
    private final String value;
    private final String color;

    public MsgValue(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public MsgValue(String value) {
        this(value, "#000000");
    }

    public String getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }
}
