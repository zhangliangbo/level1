package xxl.division;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xxl.mathematica.Rule;
import xxl.mathematica.single.GsonSingle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Division {
    /**
     * 获取所有的区划信息
     *
     * @param level
     */
    public static String division(int level) {
        return generateData(level, null, null);
    }

    /**
     * 获取省市区划信息
     *
     * @param level
     * @param province
     * @param city
     */
    public static String division(int level, Predicate<Rule<String, String>> province, Predicate<Rule<String, String>> city) {
        return generateData(level, province, city);
    }

    private static class Node {

        private String code;
        private String name;
        private String html;

        public Node(String html) {
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


    private static String generateData(int level, Predicate<Rule<String, String>> province, Predicate<Rule<String, String>> city) {
        //省份
        if (level < 0) return null;
        JsonArray pa = new JsonArray();
        List<Node> provinces = provinces();
        for (Node pn : provinces) {
            if (province != null && !province.test(Rule.valueOf(pn.getCode(), pn.getName()))) continue;
            System.err.println("province " + pn.getName());
            JsonObject po = new JsonObject();
            po.addProperty("name", pn.getName());
            po.addProperty("code", pn.getCode());
            pa.add(po);
            if (level < 1) continue;
            JsonArray ca = new JsonArray();
            po.add("cities", ca);
            //城市
            List<Node> cities = cities(pn);
            for (Node cn : cities) {
                if (city != null && !city.test(Rule.valueOf(cn.getCode(), cn.getName()))) continue;
                System.err.println("city " + cn.getName());
                JsonObject co = new JsonObject();
                co.addProperty("name", cn.getName());
                co.addProperty("code", cn.getCode());
                ca.add(co);
                if (level < 2) continue;
                JsonArray aa = new JsonArray();
                co.add("areas", aa);
                //地区
                List<Node> areas = areas(cn);
                for (Node an : areas) {
                    System.err.println("area " + an.getName());
                    JsonObject ao = new JsonObject();
                    ao.addProperty("name", an.getName());
                    ao.addProperty("code", an.getCode());
                    aa.add(ao);
                    if (level < 3) continue;
                    JsonArray sa = new JsonArray();
                    ao.add("streets", sa);
                    //街道
                    List<Node> streets = streets(an);
                    for (Node sn : streets) {
                        System.err.println("street " + sn.getName());
                        JsonObject so = new JsonObject();
                        so.addProperty("name", sn.getName());
                        so.addProperty("code", sn.getCode());
                        sa.add(so);
                        if (level < 4) continue;
                        JsonArray ma = new JsonArray();
                        so.add("communities", ma);
                        //社区
                        List<Node> communities = communities(sn);
                        for (Node mn : communities) {
                            System.err.println("community " + mn.getName());
                            JsonObject mo = new JsonObject();
                            mo.addProperty("name", mn.getName());
                            mo.addProperty("code", mn.getCode());
                            ma.add(mo);
                        }
                    }
                }
            }
        }
        return GsonSingle.instance().toJson(pa);
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
