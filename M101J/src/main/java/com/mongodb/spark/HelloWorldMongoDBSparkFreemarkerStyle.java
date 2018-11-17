package com.mongodb.spark;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.bson.Document;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;

import static spark.Spark.halt;

public class HelloWorldMongoDBSparkFreemarkerStyle {

    public static void main(String[] args) {
        final Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading
                (HelloWorldMongoDBSparkFreemarkerStyle.class, "/freemarker");

        MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));

        MongoDatabase database = client.getDatabase("course");
        final MongoCollection<Document> collection = database.getCollection("hello");
        collection.drop();

        collection.insertOne(new Document("name", "MongoDB"));

        Spark.get("/", new Route() {
            @Override
            public Object handle(final Request request,
                                 final Response response) {
                StringWriter writer = new StringWriter();
                try {
                    Template helloTemplate = configuration.getTemplate("hello.ftl");

                    Document document = collection.find().first();

                    helloTemplate.process(document, writer);
                } catch (Exception e) {
                    halt(500);
                    e.printStackTrace();
                }
                return writer;
            }
        });
    }
}
