package kpn.core.tools.next.support

import kpn.database.util.Mongo
import org.mongodb.scala.model.Accumulators.sum
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Sorts.descending
import org.mongodb.scala.model.Sorts.orderBy

case class TagCount(_id: String, count: Long)

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
    val tagCounts = nextDatabase.routeRelations.aggregate[TagCount](pipeline)
    tagCounts.foreach(println)
  }
}
