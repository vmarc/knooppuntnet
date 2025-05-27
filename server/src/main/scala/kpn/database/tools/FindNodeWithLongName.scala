package kpn.database.tools

import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Filters.exists
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.excludeId
import org.mongodb.scala.model.Projections.fields

case class NodeWithLongName(id: Long, name: String, longName: String)

object FindNodeWithLongName {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      val pipeline = buildPipeline()
      val nodes = database.nodes.aggregate[NodeWithLongName](pipeline)
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
      unwind("$names"),
      filter(
        exists("names.longName")
      ),
      project(
        fields(
          excludeId(),
          computed("id", "$_id"),
          computed("name", "$names.name"),
          computed("longName", "$names.longName"),
        )
      )
    )
  }
}
