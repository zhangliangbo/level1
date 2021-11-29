package xxl.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * zookeeper客户端封装
 *
 * @author zhangliangbo
 * @since 2021/9/12
 **/


@Slf4j
public class Zk {

    public static String host;
    public static ZooKeeper zooKeeper;

    public static void use(String h) {
        host = h;
    }

    public static ZooKeeper getClient() throws IOException {
        if (zooKeeper == null) {
            zooKeeper = new ZooKeeper(host, 60000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    log.info("zk watched {}", event);
                }
            });
        }
        return zooKeeper;
    }

    public static String create(String path, byte[] data, int flag) throws IOException, KeeperException, InterruptedException {
        return getClient().create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.fromFlag(flag));
    }

    public static String setData(String path, byte[] data, int version) throws IOException, KeeperException, InterruptedException {
        Stat stat = getClient().setData(path, data, version);
        return stat.toString();
    }

    public static byte[] getData(String path) throws IOException, KeeperException, InterruptedException {
        return getClient().getData(path, false, null);
    }

    public static void delete(String path, int version) throws IOException, KeeperException, InterruptedException {
        getClient().delete(path, version);
    }

    public static String exists(String path) throws IOException, KeeperException, InterruptedException {
        Stat exists = getClient().exists(path, false);
        return Objects.nonNull(exists) ? exists.toString() : null;
    }

    public static List<String> getChildren(String path) throws IOException, KeeperException, InterruptedException {
        return getClient().getChildren(path, false);
    }

    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        Zk.use("localhost:7777");
        byte[] data = Zk.getData("/zlb");
        System.err.println(new String(data));
    }

}
