db.posts.aggregate([
    { $project: { comments: 1, _id: 0 } },
    {$unwind: "$comments"},
    {$group: {
            _id: "$comments.author",
            count: { $sum : 1 }
        }
    },
    { $sort: { count: 1 } }
]) 

db.grades.aggregate([
    {$unwind: "$scores"},
    { $match: { "$or": [ {"scores.type": "exam"}, {"scores.type": "homework"} ] } },
    {$group: {
            _id: "$class_id",
            avgScore: { $avg: "$scores.score" }
        }
    },
    { $sort: { avgScore: 1 } }
]) 

db.zips.aggregate([
    { $match: { pop: { $gte: 25000 }, $or: [{state: "CA"}, {state: "NY"}] } },
    { $group: {
        _id: null,
        avgPop: { $avg: "$pop" }
    } }
])

db.zips.aggregate([
    { $match: { pop: { $gte: 25000 } } },
    { $group: {
        _id: "$state",
        avgPop: { $avg: "$pop" }
    } }
])

db.zips.aggregate([
    { $match: { city: { $regex: /[B|D|O|G|N|M]\w+/g } } },
    { $group: {
        _id: null,
        totalPop: { $sum: "$pop" }
    } }
])

db.zips.aggregate([
    { $project: {
        first_char: {$substr : ["$city",0,1]},
        pop: 1
    } },
    { $match: { first_char: { $in: ["B", "D", "O", "G", "N", "M"] } } },
    { $group: {
        _id: null,
        totalPop: { $sum: "$pop" }
    } }
])

db.zips.aggregate([
    { $match: { pop: { $gte: 25000 }, $or: [{state: "CT"}, {state: "NJ"}] } },
    { $group: {
        _id: null,
        avgPop: { $avg: "$pop" }
    } }
])