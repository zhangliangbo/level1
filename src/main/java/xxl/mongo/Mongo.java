package xxl.mongo;

import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mongo {

    private MongoClient client;
    private MongoDatabase database;
    private Map<MongoDatabase, GridFSBucket> bucketMap = new HashMap<>();

    public Mongo(String host, int port, String username, String password, String db) {
        client = MongoClients.create("mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + db);
        database = client.getDatabase(db);
    }

    public Mongo(String host, int port, String username, String password) {
        client = MongoClients.create("mongodb://" + username + ":" + password + "@" + host + ":" + port);
    }

    public Mongo(String host, int port, String db) {
        client = MongoClients.create("mongodb://" + host + ":" + port + "/" + db);
        database = client.getDatabase(db);
    }

    public Mongo(String host, int port) {
        client = MongoClients.create("mongodb://" + host + ":" + port);
    }

    /**
     * 列出所有的数据库
     *
     * @return
     */
    public List<String> databases() {
        MongoIterable<String> dbs = client.listDatabaseNames();
        MongoCursor<String> iterator = dbs.iterator();
        List<String> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * 使用当前数据
     *
     * @param db
     * @return
     */
    public boolean useDatabase(String db) {
        try {
            database = client.getDatabase(db);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取当前数据库
     *
     * @return
     */
    public MongoDatabase database() {
        if (database == null) {
            database = client.getDatabase("local");
        }
        return database;
    }

    /**
     * 移除数据库
     *
     * @param db
     * @return
     */
    public boolean dropDatabase(String db) {
        client.getDatabase(db).drop();
        return true;
    }

    /**
     * 获取集合
     *
     * @return
     */
    public List<String> connections() {
        MongoIterable<String> iterable = database().listCollectionNames();
        MongoCursor<String> iterator = iterable.iterator();
        List<String> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * 新建集合
     *
     * @param collection
     * @return
     */
    public boolean newCollection(String collection) {
        database().createCollection(collection);
        return true;
    }

    /**
     * 返回一个集合
     *
     * @param collection
     * @return
     */
    public MongoCollection<Document> collection(String collection) {
        return database().getCollection(collection);
    }

    /**
     * 移除集合
     *
     * @param collection
     * @return
     */
    public boolean dropCollection(String collection) {
        database().getCollection(collection).drop();
        return true;
    }

    /**
     * 列出一个集合的所有文档
     *
     * @param collectionName
     * @return
     */
    public List<Document> documents(String collectionName) {
        MongoCollection<Document> collection = database().getCollection(collectionName);
        FindIterable<Document> documents = collection.find();
        MongoCursor<Document> iterator = documents.iterator();
        List<Document> list = new ArrayList<>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return list;
    }

    /**
     * 新建一个文档记录
     *
     * @param collection
     * @param document
     * @return
     */
    public boolean newDocument(String collection, Document document) {
        database().getCollection(collection).insertOne(document);
        return true;
    }

    /**
     * 获取当前数据库的bucket
     *
     * @return
     */
    public GridFSBucket bucket() {
        if (!bucketMap.containsKey(database())) {
            bucketMap.put(database(), GridFSBuckets.create(database()));
        }
        return bucketMap.get(database());
    }

    /**
     * 上传文件
     *
     * @param name
     * @param inputFile
     * @return
     */
    public ObjectId uploadFile(String name, String inputFile) {
        try {
            return bucket().uploadFromStream(name, new FileInputStream(inputFile));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * 下载文件
     *
     * @param outputFile
     * @return
     */
    public boolean downloadFile(ObjectId id, String outputFile) {
        try {
            bucket().downloadToStream(id, new FileOutputStream(outputFile));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

}
