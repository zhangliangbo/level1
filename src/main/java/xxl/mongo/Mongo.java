package xxl.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Mongo {

    private static ConnectionString connectionString;
    private static MongoClient client;
    private static MongoDatabase database;
    private static Map<String, MongoDatabase> databaseMap = new HashMap<>();
    private static Map<MongoDatabase, GridFSBucket> bucketMap = new HashMap<>();

    public static void use(String host, int port, String username, String password) {
        connectionString = new ConnectionString("mongodb://" + username + ":" + password + "@" + host + ":" + port);
    }

    private static synchronized MongoClient getClient() throws IllegalStateException {
        if (Objects.isNull(connectionString)) {
            throw new IllegalStateException("请使用Mongo.use(..)指定数据源");
        }
        if (Objects.nonNull(client)) {
            client.close();
        }
        client = MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .applyToConnectionPoolSettings(builder -> builder.maxSize(200).maxWaitTime(60, TimeUnit.MINUTES))
                        .build()
        );
        return client;
    }

    /**
     * 获取当前数据库
     *
     * @return
     */
    private static synchronized MongoDatabase database() {
        if (Objects.isNull(database)) {
            database("test");
        }
        return database;
    }

    /**
     * 获取当前数据库的bucket
     *
     * @return
     */
    private static synchronized GridFSBucket bucket() {
        MongoDatabase mongoDatabase = database();
        GridFSBucket gridFsBucket = bucketMap.get(mongoDatabase);
        if (Objects.isNull(gridFsBucket)) {
            gridFsBucket = GridFSBuckets.create(mongoDatabase);
            bucketMap.put(mongoDatabase, gridFsBucket);
        }
        return gridFsBucket;
    }

    /**
     * 列出所有的数据库
     *
     * @return
     */
    public static List<String> databases() {
        MongoIterable<String> dbs = getClient().listDatabaseNames();
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
    public static boolean database(String db) {
        try {
            MongoDatabase mongoDatabase = databaseMap.get(db);
            if (Objects.isNull(mongoDatabase)) {
                mongoDatabase = getClient().getDatabase(db);
                databaseMap.put(db, mongoDatabase);
            }
            database = mongoDatabase;
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 移除数据库
     *
     * @param db
     * @return
     */
    public static boolean dropDatabase(String db) {
        getClient().getDatabase(db).drop();
        return true;
    }

    /**
     * 获取集合
     *
     * @return
     */
    public static List<String> collections() {
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
    public static boolean newCollection(String collection) {
        try {
            database().createCollection(collection);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 返回一个集合
     *
     * @param collection
     * @return
     */
    public static String collection(String collection) {
        return database().getCollection(collection).getNamespace().getFullName();
    }

    /**
     * 移除集合
     *
     * @param collection
     * @return
     */
    public static boolean dropCollection(String collection) {
        database().getCollection(collection).drop();
        return true;
    }

    /**
     * 列出一个集合的所有文档
     *
     * @param collectionName
     * @return
     */
    public static List<Document> documents(String collectionName) {
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
    public static boolean newDocument(String collection, Document document) {
        database().getCollection(collection).insertOne(document);
        return true;
    }

    /**
     * 上传文件
     *
     * @param name
     * @param inputFile
     * @return
     */
    public static ObjectId uploadFile(String name, String inputFile) {
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
    public static boolean downloadFile(ObjectId id, String outputFile) {
        try {
            bucket().downloadToStream(id, new FileOutputStream(outputFile));
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

}
