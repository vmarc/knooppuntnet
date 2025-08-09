package kpn.core.tools.support

import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.sort
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.or
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.orderBy
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.StringId
import kpn.database.util.Mongo

object FindNodesWithImagesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new FindNodesWithImagesTool(database).report()
    }
  }
}

class FindNodesWithImagesTool(database: Database) {

  private val log = Log(classOf[FindSpecialNodesTool])

  def report(): Unit = {
    // findNodeTagKeys().foreach(println)

    findNodeImageTags().foreach(println)
  }

  private def findNodeImageTags(): Seq[NodeImageTag] = {

    val pipeline = Seq(
      filter(
        equal("active", true),
      ),
      unwind("$tags.tags"),
      project(
        fields(
          excludeId(),
          computed("nodeId", "$_id"),
          computed("key", "$tags.tags.key"),
          computed("value", "$tags.tags.value"),
        )
      ),
      filter(
        or(
          equal("key", "image"),
          equal("key", "image:0"),
          equal("key", "image:panorama"),
          equal("key", "wikimedia_commons"),
        )
      ),
      sort(orderBy(ascending("key", "_id")))
    )
    database.nodes.aggregate(pipeline, classOf[NodeImageTag], log)
  }

  private def findNodeTagKeys(): Seq[String] = {
    val pipeline = Seq(
      filter(
        equal("active", true),
      ),
      unwind("$tags.tags"),
      project(
        fields(
          excludeId(),
          computed("key", "$tags.tags.key"),
        )
      ),
      group("$key"),
      sort(orderBy(ascending("_id")))
    )
    database.nodes.aggregate(pipeline, classOf[StringId], log).map(_._id).sorted
  }
}
