package com.mongodb;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.util.Arrays;
import java.util.Date;

import static com.mongodb.Helpers.printJson;

public class DocumentTest {

    public static void main(String[] args) {
        Document document = new Document()
                .append("str", "MongoDb, Hello")
                .append("int", 42)
                .append("long", 1L)
                .append("b", false)
                .append("double", 1.1)
                .append("date", new Date())
                .append("null", null)
                .append("embeddedDoc", new Document("x", 0))
                .append("list", Arrays.asList(1, 2, 3))
                ;

        printJson(document);

        String str = document.getString("str");

        BsonDocument bsonDocument = new BsonDocument("str", new BsonString("MongoDB, Hello"));
    }
}
