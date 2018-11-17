package com.mongodb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;

import static com.mongodb.Helpers.printJson;
import static com.mongodb.client.model.Filters.eq;

public class DeleteTest {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("course");
        MongoCollection<Document> collection = database.getCollection("test");

        collection.drop();

        // insert 8 documents, with _id set to the value of the loop variable
        for (int i = 0; i < 8; i++) {
            collection.insertOne(new Document().append("_id", i));
        }

        collection.deleteOne(eq("_id", 4));

        for (Document cur : collection.find().into(new ArrayList<Document>())) {
            printJson(cur);
        }
    }
}
