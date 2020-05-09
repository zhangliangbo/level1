package xxl.mongo;

import org.bson.types.ObjectId;

public class MongoDemo {
    public static void main(String[] args) {
        Mongo mongo = new Mongo("localhost", 27017, "root", "civic", "quilt");
        ObjectId id = mongo.uploadFile("zlb", "C:\\Users\\zhang\\Desktop\\my.png");
        System.err.println(id);
        boolean downloadQ = mongo.downloadFile(id, "C:\\Users\\zhang\\Desktop\\my11111.png");
        System.err.println("downloadQ: " + downloadQ);
    }
}
