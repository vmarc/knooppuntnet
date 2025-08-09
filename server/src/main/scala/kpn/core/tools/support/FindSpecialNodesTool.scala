package kpn.core.tools.support

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.util.Mongo

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
        equal("active", true),
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

    database.nodes.aggregate(pipeline, classOf[SpecialNode], log)
  }
}
