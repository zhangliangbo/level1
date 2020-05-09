package xxl.docker;

public class ImageInfo {
    private String name;
    private String desc;
    private int stars;
    private boolean official;
    private boolean automated;

    public ImageInfo(String name, String desc, int stars, boolean official, boolean automated) {
        this.name = name;
        this.desc = desc;
        this.stars = stars;
        this.official = official;
        this.automated = automated;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getStars() {
        return stars;
    }

    public boolean isOfficial() {
        return official;
    }

    public boolean isAutomated() {
        return automated;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", stars=" + stars +
                ", official=" + official +
                ", automated=" + automated +
                '}';
    }
}
