package xxl.mp;

public class TemplateMessageValue {
    private final String value;
    private final String color;

    public TemplateMessageValue(String value, String color) {
        this.value = value;
        this.color = color;
    }

    public TemplateMessageValue(String value) {
        this(value, "#000000");
    }

    public String getValue() {
        return value;
    }

    public String getColor() {
        return color;
    }
}
