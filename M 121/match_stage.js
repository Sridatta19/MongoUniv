// $match all celestial bodies, not equal to Star
db.solarSystem.aggregate([{
  "$match": { "type": { "$ne": "Star" } }
}]).pretty()

db.movies.aggregate([{
  "$match": {
    "imdb.rating": { "$gte": 7 },
    "languages": { "$all": ["English", "Japanese"] },
    "genres": { "$nin": ["Crime", "Horror"] },
    "$or": [{"rated": "G"}, {"rated": "PG"}]
  }
},
{ "$project": { "title": 1, "rated": 1, "_id": 0 } }
]).toArray().length

db.movies.aggregate([
  { $project : { title: 1, splitTitle : { $split: ["$title", " "] }, _id: 0 } },
  { $match: { "splitTitle": { $size: 1 } } }
]).toArray().length

db.movies.aggregate([
  { $match: { "tomatoes.viewer.rating": { $gte: 3 }  } },
  {"$addFields": {
      "favorites": [
        "Sandra Bullock",
        "Tom Hanks",
        "Julia Roberts",
        "Kevin Spacey",
        "George Clooney"
      ]
  }},
  { $project : { title: 1, tomatoes: 1, cast: 1, favCast: { $setIntersection: [ "$cast", "$favorites" ] } } }
]).toArray().length

db.movies.aggregate([
  { $match: { "tomatoes.viewer.rating": { $gte: 3 }, "cast": { $exists: true }  } },
  { $project : { title: 1, tomatoes: 1, cast: 1 } },
  { $project : { title: 1, tomatoes: 1, cast: 1, favCast: { $setIntersection: [ "$cast", [
    "Sandra Bullock",
    "Tom Hanks",
    "Julia Roberts",
    "Kevin Spacey",
    "George Clooney"
  ] ] } } },
  { $project : { title: 1, tomatoes: 1, cast: 1, num_favs: { $size: "$favCast" } } },
  {
    "$sort": { "num_favs": -1, "tomatoes.viewer.rating": -1, "title": -1 }
  },
  { "$skip": 25  },
  { "$limit": 1  }
])

db.movies.aggregate([
  { $match: { $and: [{"languages": { $exists: true }}, {"languages": { $in: "English" }}]  } },
  { $project : { title: 1, tomatoes: 1, cast: 1 } },
  { $project : { title: 1, tomatoes: 1, cast: 1, favCast: { $setIntersection: [ "$cast", [
    "Sandra Bullock",
    "Tom Hanks",
    "Julia Roberts",
    "Kevin Spacey",
    "George Clooney"
  ] ] } } },
  { $project : { title: 1, tomatoes: 1, cast: 1, num_favs: { $size: "$favCast" } } },
  {
    "$sort": { "num_favs": -1, "tomatoes.viewer.rating": -1, "title": -1 }
  },
  { "$skip": 24  },
  { "$limit": 1  }
])

db.movies.aggregate([
  { $match: { "tomatoes.viewer.rating": { $gte: 3 }  } },
  { $project : { title: 1, tomatoes: 1, cast: 1 } },
  { $project : { title: 1, tomatoes: 1, cast: 1, num_favs: { $size: { $setIntersection: [ "$cast", [
    "Sandra Bullock",
    "Tom Hanks",
    "Julia Roberts",
    "Kevin Spacey",
    "George Clooney"
  ] ] } } } },
  {
    "$sort": { "num_favs": -1, "tomatoes.viewer.rating": -1, "title": 1 }
  },
  { "$limit": 25  }
]).toArray().length


db.movies.aggregate([
  { $match: {
    $and: [{"languages": { $exists: true }}, {"languages": { $in: ["English"] }}],
    "imdb.rating": {"$gte": 1},
    "imdb.votes": {"$gte": 1},
    "year": {"$gte": 1990}
  } },
  { $project: {
      title: 1,
      normalized_rating: {
        $let: {
          vars: {
            scaledVotes: 12
          },
          in: { $avg: [ "$$scaledVotes", "$imdb.rating" ] }
        }
      }
    } },
  { $sort: { normalized_rating: -1 } },
  { $limit: 1 }
]).toArray().length

var normalize1 = function(iv) {
  return [
{ $match: {
  $and: [{"languages": { $exists: true }}, {"languages": { $in: ["English"] }}],
  "imdb.rating": {"$gte": 1},
  "imdb.votes": {"$gte": 1},
  "year": {"$gte": 1990}
} },
{ $project: {
    title: 1,
    normalized_rating: {
      $let: {
        vars: {
          scaledVotes: iv
        },
        in: { $avg: [ "$$scaledVotes", "$imdb.rating" ] }
      }
    }
  } },
{ $sort: { normalized_rating: 1 } },
{ $limit: 1 }
]
}

db.movies.aggregate(normalize1("imdb.votes"))

// same query using find command
db.solarSystem.find({ "type": { "$ne": "Star" } }).pretty();

// count the number of matching documents
db.solarSystem.count();

// using $count
db.solarSystem.aggregate([{
  "$match": { "type": { "$ne": "Star"} }
}, {
  "$count": "planets"
}]);

// matching on value, and removing ``_id`` from projected document
db.solarSystem.find({"name": "Earth"}, {"_id": 0});
