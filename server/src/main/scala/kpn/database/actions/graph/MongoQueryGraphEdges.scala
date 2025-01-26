package kpn.database.actions.graph

import kpn.api.common.RouteType
import kpn.api.common.common.TrackPathKey
import kpn.core.doc.Label
import kpn.core.planner.graph.GraphEdge
import kpn.core.util.Log
import kpn.database.actions.graph.MongoQueryGraphEdges.log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.repository.GraphEdges
import org.mongodb.scala.model.Aggregates.filter
import org.mongodb.scala.model.Aggregates.project
import org.mongodb.scala.model.Aggregates.unwind
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Projections.computed
import org.mongodb.scala.model.Projections.fields
import org.mongodb.scala.model.Projections.include

case class RouteGraphEdge(
  routeType: RouteType,
  proposed: Boolean,
  _id: Long,
  pathId: Long,
  sourceNodeId: Long,
  sinkNodeId: Long,
  meters: Long
)

object MongoQueryGraphEdges {

  private val log = Log(classOf[MongoQueryGraphEdges])

  def main(args: Array[String]): Unit = {
    println("MongoQueryGraphEdges")
    Mongo.executeIn("kpn") { database =>
      val query = new MongoQueryGraphEdges(database)
      database.nodes.findById(0L)
      val t1 = System.currentTimeMillis()
      query.execute()
      val t2 = System.currentTimeMillis()
      println(s"Total = ${t2 - t1}")
    }
  }
}

class MongoQueryGraphEdges(database: Database) {

  def execute(): Seq[GraphEdges] = {

    val pipeline = Seq(
      filter(equal("labels", Label.active)),
      unwind("$edges"),
      unwind("$summary.routeTypes"),
      project(
        fields(
          computed("routeType", "$summary.routeTypes"),
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
      val edges = database.baseRoutes.aggregate[RouteGraphEdge](pipeline, log)
      val grapEdgess = RouteType.values.map { routeType =>
        val routeTypeEdges = edges.filter(_.routeType == routeType).map { edge =>
          GraphEdge(
            edge.sourceNodeId: Long,
            edge.sinkNodeId: Long,
            edge.meters: Long,
            edge.proposed: Boolean,
            TrackPathKey(edge._id, edge.pathId)
          )
        }
        GraphEdges(routeType, routeTypeEdges)
      }
      val result = grapEdgess.map(e => s"${e.routeType.entryName}: ${e.edges.size}").mkString(", ")
      (result, grapEdgess)
    }
  }
}
