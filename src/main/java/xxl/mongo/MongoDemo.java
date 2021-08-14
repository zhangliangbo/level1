package xxl.mongo;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDemo {
    public static void main(String[] args) {
        Mongo.use("localhost", 27017, "root", "civic");
        System.err.println(Mongo.databases());
        System.err.println(Mongo.collections());
        Mongo.newCollection("a");
        String a = Mongo.collection("a");
        System.err.println(a);
    }
}
