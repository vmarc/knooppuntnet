package kpn.core.tools.support

import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.StringId
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.group
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.sort
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.or
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Sorts.ascending
import org.mongodb.scala.model.Sorts.orderBy

object FindNodesWithImagesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new FindNodesWithImagesTool(database).report()
    }
  }
}

case class NodeImageTag(nodeId: Long, key: String, value: String)

class FindNodesWithImagesTool(database: Database) {

  private val log = Log(classOf[FindSpecialNodesTool])

  def report(): Unit = {
    // findNodeTagKeys().foreach(println)

    findNodeImageTags().foreach(println)
  }

  private def findNodeImageTags(): Seq[NodeImageTag] = {

    val pipeline = Seq(
      filter(
        equal("labels", Label.active),
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
    database.nodes.aggregate[NodeImageTag](pipeline, log)
  }

  private def findNodeTagKeys(): Seq[String] = {
    val pipeline = Seq(
      filter(
        equal("labels", Label.active),
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
    database.nodes.aggregate[StringId](pipeline, log).map(_._id).sorted
  }
}
