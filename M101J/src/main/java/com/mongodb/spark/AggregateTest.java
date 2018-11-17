package com.mongodb.spark;

import com.mongodb.Block;
import com.mongodb.Helpers;
import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.min;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Sorts.descending;

public class AggregateTest {

    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase database = client.getDatabase("school");
        MongoCollection<Document> collection = database.getCollection("students");

//        AggregateIterable<Document> aggregate = findAggregateOne(collection);
//
//        aggregate.forEach((Block<? super Document>) Helpers::printJson);
//
//        findGreaterThan65(collection);

        //deleteMinimumScoreOfStudent(collection);

        //find101BestGrade(collection);

        //findAndPrint(collection);

        findAndPrintAggregateThree(collection);
    }

    private static void findAndPrintAggregateThree(MongoCollection<Document> collection) {
        Bson unwind = unwind("$scores");
        Bson group = group("$_id", avg("avgScore", "$scores.score"));
        Bson sort = sort(descending("avgScore"));
        Bson limit = limit(3);

        AggregateIterable<Document> aggregate = collection.aggregate(Arrays.asList(
                unwind,
                group,
                sort,
                limit
        ));

        aggregate.forEach((Block<? super Document>) Helpers::printJson);
    }

    private static void find101BestGrade(MongoCollection<Document> collection) {
        Bson sort = descending("score");
        Document first = collection.find()
                .sort(sort)
                .skip(100)
                .first();
        Helpers.printJson(first);
    }

    private static void findAndPrint(MongoCollection<Document> collection) {
        Bson sort = and(
                ascending("student_id"),
                ascending("score")
        );
        Bson projection = fields(
                include("student_id", "type", "score"),
                excludeId()
        );
        FindIterable<Document> docs = collection.find()
                .projection(projection)
                .sort(sort)
                .limit(5);
        docs.forEach((Block<? super Document>) Helpers::printJson);
    }

    private static AggregateIterable<Document> findAggregateOne(MongoCollection<Document> collection) {
        Bson group = group("$student_id", avg("average", "$score"));
        Bson sort = sort(descending("average"));
        Bson limit = limit(1);

        return collection.aggregate(Arrays.asList(
                group,
                sort,
                limit
        ));
    }

    private static void deleteMinimumScoreOfStudent(MongoCollection<Document> collection) {

        Bson match = match(Filters.eq("type", "homework"));
        Bson group = group("$student_id", min("minScore", "$score"));

        AggregateIterable<Document> aggregate = collection
                .aggregate(Arrays.asList(
                        match,
                        group
                ));

        aggregate.forEach((Block<? super Document>) a -> {
            collection.deleteOne(
                and(
                    eq("student_id", a.getInteger("_id")),
                    eq("score", a.getDouble("minScore"))
                )
            );
        });
    }

    private static void findGreaterThan65(MongoCollection<Document> collection) {
        Bson filter = gte("score", 65);
        Bson sort = ascending("score");
        Document first = collection.find(filter)
                .sort(sort)
                .first();
        Helpers.printJson(first);
    }
}
