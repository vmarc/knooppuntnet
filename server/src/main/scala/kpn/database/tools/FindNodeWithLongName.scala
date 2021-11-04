package kpn.database.tools

import kpn.core.doc.Label
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

      val pipeline = Seq(
        filter(
          equal("labels", Label.active)
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

      val nodes = database.nodes.aggregate[NodeWithLongName](pipeline)
      nodes.foreach(println)

      println("node count = " + nodes.size)
      println("unique names node count = " + nodes.count(node => node.name != node.longName))
    }
  }
}
