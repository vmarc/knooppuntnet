package kpn.core.mongo.actions.graph

import kpn.api.common.common.TrackPathKey
import kpn.api.custom.NetworkType
import kpn.core.mongo.Database
import kpn.core.mongo.GraphEdges
import kpn.core.mongo.actions.graph.MongoQueryGraphEdges.log
import kpn.core.mongo.doc.Label
import kpn.core.mongo.util.Mongo
import kpn.core.mongo.util.MongoQuery
import kpn.core.planner.graph.GraphEdge
import kpn.core.util.Log
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

case class RouteGraphEdge(
  networkType: NetworkType,
  proposed: Boolean,
  _id: Long,
  pathId: Long,
  sourceNodeId: Long,
  sinkNodeId: Long,
  meters: Long
)

object MongoQueryGraphEdges extends MongoQuery {
  private val log = Log(classOf[MongoQueryGraphEdges])

  def main(args: Array[String]): Unit = {
    println("MongoQueryGraphEdges")
    Mongo.executeIn("kpn-test-3") { database =>
      val query = new MongoQueryGraphEdges(database)
      database.nodes.findById(0L)
      val t1 = System.currentTimeMillis()
      query.execute()
      val t2 = System.currentTimeMillis()
      println("Total = " + (t2 - t1))
    }
  }
}

class MongoQueryGraphEdges(database: Database) {

  def execute(): Seq[GraphEdges] = {

    val pipeline = Seq(
      filter(equal("labels", Label.active)),
      unwind("$edges"),
      project(
        fields(
          computed("networkType", "$summary.networkType"),
          include("proposed"),
          include("_id"),
          computed("pathId", "$edges.pathId"),
          computed("sourceNodeId", "$edges.sourceNodeId"),
          computed("sinkNodeId", "$edges.sinkNodeId"),
          computed("meters", "$edges.meters")
        )
      )
    )

    log.debugElapsed {
      val edges = database.routes.aggregate[RouteGraphEdge](pipeline, log)
      val grapEdgess = NetworkType.all.map { networkType =>
        val networkTypeEdges = edges.filter(_.networkType == networkType).map { edge =>
          GraphEdge(
            edge.sourceNodeId: Long,
            edge.sinkNodeId: Long,
            edge.meters: Long,
            edge.proposed: Boolean,
            TrackPathKey(edge._id, edge.pathId)
          )
        }
        GraphEdges(networkType, networkTypeEdges)
      }
      val result = grapEdgess.map(e => s"${e.networkType.name}: ${e.edges.size}").mkString(", ")
      (result, grapEdgess)
    }
  }
}
