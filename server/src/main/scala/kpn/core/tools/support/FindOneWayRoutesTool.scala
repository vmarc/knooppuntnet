package kpn.core.tools.support

import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.database.views.planner.GraphEdgesView
import kpn.core.db.couch.Couch

object FindOneWayRoutesTool {
  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "analysis") { database =>
      new FindOneWayRoutesTool(database).find()
    }
  }
}

case class NodePair(sourceNodeId: Long, singNodeId: Long)

class FindOneWayRoutesTool(database: Database) {

  def find(): Unit = {
    val edges = GraphEdgesView.query(database, NetworkType.cycling)
    val nodePairs = edges.map(edge => NodePair(edge.sourceNodeId, edge.sinkNodeId)).toSet
    val oneWays = edges.filterNot { edge =>
      val back = NodePair(edge.sinkNodeId, edge.sourceNodeId)
      nodePairs.contains(back)
    }

    oneWays.foreach(println)
    println(s"${edges.size} edges")
    println(s"${oneWays.size} one ways")

    // RouteOneWay

  }

}
