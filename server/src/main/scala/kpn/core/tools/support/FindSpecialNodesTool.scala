package kpn.core.tools.support

import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.core.doc.Label
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

object FindSpecialNodesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new FindSpecialNodesTool(database).report()
    }
  }
}

case class SpecialNode(nodeId: Long, country: Country, routeType: RouteType)

class FindSpecialNodesTool(database: Database) {

  private val log = Log(classOf[FindSpecialNodesTool])

  def report(): Unit = {
    val nodes = findNodes()
    nodes.zipWithIndex.foreach { case (node, index) =>
      println(s"| ${index + 1} | ${node.country.entryName} | ${node.routeType.entryName} | [${node.nodeId}](https://knooppuntnet.nl/en/analysis/node/${node.nodeId}) |")
    }
  }

  private def findNodes(): Seq[SpecialNode] = {

    val pipeline = Seq(
      filter(
        equal("labels", Label.active),
      ),
      unwind("$names"),
      filter(
        equal("names.name", "o"),
      ),
      project(
        fields(
          computed("nodeId", "$_id"),
          include("country"),
          computed("routeType", "$names.routeType"),
        )
      ),
    )

    database.nodes.aggregate[SpecialNode](pipeline, log)
  }
}
