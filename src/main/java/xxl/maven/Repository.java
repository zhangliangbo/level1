package xxl.maven;

import io.vavr.control.Try;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import xxl.mathematica.procedural.Do;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 所有的仓库，从【https://mvnrepository.com/repos】获取
 */
public class Repository {
    /**
     * 获取一页仓库
     *
     * @param page
     * @return
     */
    public static List<Map<String, Object>> repository(int page) {
        return Try.ofCallable(() -> {
            Document document = Jsoup.connect("https://mvnrepository.com/repos?p=" + page).get();
            Element body = document.body();
            Elements orders = body.select(".im-title span");
            Elements titles = body.select(".im-title a");
            Elements urls = body.select(".im-subtitle");
            List<Map<String, Object>> list = new ArrayList<>();
            Do.loop(t -> {
                Map<String, Object> map = new HashMap<>();
                String order = orders.get(t).text();
                map.put("order", Integer.parseInt(order.substring(0, order.indexOf("."))));
                map.put("name", titles.get(t).text());
                map.put("url", urls.get(t).text());
                list.add(map);
            }, titles.size());
            return list;
        }).getOrElse(new ArrayList<>());
    }

    /**
     * 获取多页仓库
     *
     * @param pages
     * @return
     */
    public static List<Map<String, Object>> repository(List<Integer> pages) {
        ForkJoinPool pool = new ForkJoinPool();
        List<Map<String, Object>> res = pool.invoke(new Task(Arrays.asList(1, 2)));
        pool.shutdown();
        return res;
    }

    private static class Task extends RecursiveTask<List<Map<String, Object>>> {

        private final List<Integer> pages;

        public Task(List<Integer> pages) {
            this.pages = pages;
        }

        @Override
        protected List<Map<String, Object>> compute() {
            if (pages.size() == 1) {
                return repository(pages.get(0));
            }
            int middle = pages.size() / 2;
            Task task1 = new Task(pages.subList(0, middle));
            Task task2 = new Task(pages.subList(middle, pages.size()));
            List<Map<String, Object>> res = new ArrayList<>();
            task2.fork();
            res.addAll(task1.invoke());
            res.addAll(task2.join());
            return res;
        }
    }
}
