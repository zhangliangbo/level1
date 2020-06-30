package xxl.division;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Division {

    public static void division(int level) {
        generateData(level);
    }

    private static class Node {

        private String code;
        private String name;
        private String html;

        public Node(String html) {
            this.html = html;
        }

        public Node(String name, String html) {
            this.name = name;
            this.html = html;
        }

        public Node(String code, String name, String html) {
            this.code = code;
            this.name = name;
            this.html = html;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", html='" + html + '\'' +
                    '}';
        }
    }


    private static void generateData(int level) {
        //省份
        if (level < 0) {
            return;
        }
        List<Node> provinces = provinces();
        for (Node pn : provinces) {
            System.out.println("province " + pn.getName());
            if (level < 1) continue;
            //城市
            List<Node> cities = cities(pn);
            for (Node cn : cities) {
                System.out.println("city " + cn.getName());
                if (level < 2) continue;
                //地区
                List<Node> areas = areas(cn);
                for (Node an : areas) {
                    System.out.println("area " + an.getName());
                    if (level < 3) continue;
                    //街道
                    List<Node> streets = streets(an);
                    for (Node sn : streets) {
                        System.out.println("street " + sn.getName());
                        if (level < 4) continue;
                        //社区
                        List<Node> communities = communities(sn);
                        for (Node mn : communities) {
                            System.out.println("community " + mn.getName());
                        }
                    }
                }
            }
        }
    }


    private static void fillData(List<Node> nodeList, String href, String text) {
        Node exist = null;
        for (Node node : nodeList) {
            if (node.getHtml().equals(href)) {
                exist = node;
                break;
            }
        }
        if (exist == null) {
            exist = new Node(href);
            nodeList.add(exist);
        }
        String chinesePattern = "^[\\u4e00-\\u9fa5]+$";
        String numberPattern = "^[0-9]+$";
        if (text.matches(chinesePattern)) {
            exist.setName(text);
        } else if (text.matches(numberPattern) && text.length() == 12) {
            exist.setCode(text);
        }
    }

    private static List<Node> provinces() {
        return Single
                .fromCallable(() -> {
                    System.err.println("get provinces");
                    Document doc = document("index.html");
                    Elements as = doc.select("a");
                    //所有省份
                    List<Node> nodeList = new ArrayList<>();
                    for (Element element : as) {
                        String href = element.attr("href");
                        if (href.endsWith(".html")) {
                            String text = element.text();
                            fillData(nodeList, href, text);
                        }
                    }
                    return nodeList;
                })
                .retry((integer, throwable) -> {
                    System.err.println(integer + " retry get provinces");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .blockingGet();
    }

    private static List<Node> cities(Node province) {
        return Single
                .fromCallable(() -> {
                    System.err.println("get cities from province " + province.getName() + " " + province.getHtml());
                    Document doc = document(province.getHtml());
                    Elements as = doc.select("a");
                    //所有省份
                    List<Node> nodeList = new ArrayList<>();
                    for (Element element : as) {
                        String href = element.attr("href");
                        String text = element.text();
                        if (href.endsWith(".html")) {
                            fillData(nodeList, href, text);
                        }
                    }
                    return nodeList;
                })
                .retry((integer, throwable) -> {
                    System.err.println(integer + " retry get cities from province " + province.getName() + " " + province.getHtml());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .blockingGet();
    }

    private static List<Node> areas(Node city) {
        return Single
                .fromCallable(() -> {
                    System.err.println("get areas from city " + city.getName() + " " + city.getHtml());
                    Document doc = document(city.getHtml());
                    Elements as = doc.select("a");
                    //所有省份
                    List<Node> nodeList = new ArrayList<>();
                    for (Element element : as) {
                        String href = element.attr("href");
                        String text = element.text();
                        if (href.endsWith(".html")) {
                            String[] hrefs = city.getHtml().split("/");
                            if (hrefs.length > 1) {
                                href = hrefs[0] + "/" + href;
                                fillData(nodeList, href, text);
                            }
                        }
                    }
                    return nodeList;
                })
                .retry((integer, throwable) -> {
                    System.err.println(integer + " retry get areas from city " + city.getName() + " " + city.getHtml());
                    Thread.sleep(3000);
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .blockingGet();

    }

    private static List<Node> streets(Node area) {
        return Single
                .fromCallable(() -> {
                    System.err.println("get streets from area " + area.getName() + " " + area.getHtml());
                    Document doc = document(area.getHtml());
                    Elements as = doc.select("a");
                    //所有省份
                    List<Node> nodeList = new ArrayList<>();
                    for (Element element : as) {
                        String href = element.attr("href");
                        String text = element.text();
                        if (href.endsWith(".html")) {
                            String[] hrefs = area.getHtml().split("/");
                            if (hrefs.length > 2) {
                                href = hrefs[0] + "/" + hrefs[1] + "/" + href;
                                fillData(nodeList, href, text);
                            }
                        }
                    }
                    return nodeList;
                })
                .retry((integer, throwable) -> {
                    System.err.println(integer + " retry get streets from area " + area.getName() + " " + area.getHtml());
                    Thread.sleep(3000);
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .blockingGet();

    }

    private static List<Node> communities(Node street) {
        return Single
                .fromCallable(() -> {
                    System.err.println("get communities from street " + street.getName() + " " + street.getHtml());
                    Document doc = document(street.getHtml());
                    Elements as = doc.select(".villagetr");
                    //所有省份
                    List<Node> nodeList = new ArrayList<>();
                    for (Element element : as) {
                        String code = element.child(0).text();
                        String name = element.child(2).text();
                        nodeList.add(new Node(code, name, ""));
                    }
                    return nodeList;
                })
                .retry((integer, throwable) -> {
                    System.err.println(integer + " retry get communities from street " + street.getName() + " " + street.getHtml());
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                })
                .subscribeOn(Schedulers.io())
                .blockingGet();

    }

    private static Document document(String relativeUrl) throws IOException {
        String baseUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2018/";
        String url = baseUrl + relativeUrl;
        return Jsoup.parse(new URL(url).openStream(), "GBK", url);
    }

}
