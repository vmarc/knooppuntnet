package kpn.database.tools

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.exists
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.excludeId
import com.mongodb.client.model.Projections.fields
import kpn.api.id.Storable
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo

case class NodeWithLongName(id: Long, name: String, longName: String) extends Storable

object FindNodeWithLongName {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      val pipeline = buildPipeline()
      val nodes = database.nodes.aggregate(pipeline, classOf[NodeWithLongName])
      nodes.foreach(println)
      println(s"node count = ${nodes.size}")
      println(s"unique names node count = ${nodes.count(node => node.name != node.longName)}")
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      filter(
        equal("active", true),
      ),
      unwind("$base.names"),
      filter(
        exists("base.names.longName")
      ),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          computed("name", "$base.names.name"),
          computed("longName", "$base.names.longName"),
        )
      )
    )
  }
}
