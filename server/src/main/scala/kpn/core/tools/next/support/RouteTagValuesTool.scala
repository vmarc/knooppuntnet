package kpn.core.tools.next.support

import com.mongodb.client.model.Accumulators.sum
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Sorts.descending
import com.mongodb.client.model.Sorts.orderBy
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.util.Mongo

// report all values used for the "route" tag
object RouteTagValuesTool {
  def main(args: Array[String]): Unit = {
    val nextDatabase = Mongo.nextDatabase(Mongo.client, "kpn-next")
    val pipeline = Seq(
      unwind("$relation.tags"),
      filter(equal("relation.tags.key", "route")),
      group(
        "$relation.tags.value",
        sum("count", 1)
      ),
      sort(
        orderBy(
          descending(
            "count"
          )
        )
      )
    )
    val tagCounts = nextDatabase.routeRelations.aggregate(pipeline, classOf[TagCount])
    tagCounts.foreach(println)
  }
}
