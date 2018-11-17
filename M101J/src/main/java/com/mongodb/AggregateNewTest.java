package com.mongodb;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

import static com.mongodb.client.model.Accumulators.sum;

public class AggregateNewTest {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("blog");
        MongoCollection<Document> collection = database.getCollection("posts");

        Bson project = Aggregates.project(Projections.include("comments"));
        Bson unwind = Aggregates.unwind("comments");
        Bson group = Aggregates.group("$comments.author", sum("count", 1));

        AggregateIterable<Document> aggregate = collection.aggregate(Arrays.asList(
                //Aggregates.match(Filters.and(filters))
                project,
                unwind,
                group
        ));

        aggregate.forEach((Block<? super Document>) Helpers::printJson);
    }
}
