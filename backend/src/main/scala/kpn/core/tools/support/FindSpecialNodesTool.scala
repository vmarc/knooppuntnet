package kpn.core.tools.support

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.elemMatch
import com.mongodb.client.model.Projections.fields
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.core.doc.Storable
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.util.Mongo

case class SpecialNode(
  nodeId: Long,
  country: Country,
  routeType: RouteType
) extends Storable

object FindSpecialNodesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-prod") { database =>
      new FindSpecialNodesTool(database).report()
    }
  }
}

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
      unwind("$base.names"),
      filter(
        elemMatch("base.names", equal("name", "o")),
      ),
      project(
        fields(
          computed("nodeId", "$_id"),
          computed("country", "$base.country"),
          computed("routeType", "$base.names.routeType"),
        )
      )
    )

    database.nodes.aggregate(pipeline, classOf[SpecialNode], log)
  }
}
