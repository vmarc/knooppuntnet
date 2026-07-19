package kpn.database.actions.graph

import com.mongodb.client.model.Aggregates.project
import com.mongodb.client.model.Aggregates.unwind
import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Projections.computed
import com.mongodb.client.model.Projections.fields
import com.mongodb.client.model.Projections.include
import kpn.api.common.RouteType
import kpn.api.common.common.TrackPathKey
import kpn.core.doc.Storable
import kpn.core.planner.graph.GraphEdge
import kpn.core.util.Log
import kpn.database.actions.graph.MongoQueryGraphEdges.log
import kpn.database.base.Database
import kpn.database.base.MongoAggregates.equal
import kpn.database.base.MongoAggregates.filter
import kpn.database.base.Types.MongoPipeline
import kpn.database.util.Mongo
import kpn.server.repository.GraphEdges

case class RouteGraphEdge(
  routeType: RouteType,
  proposed: Boolean,
  _id: Long,
  pathId: Long,
  sourceNodeId: Long,
  sinkNodeId: Long,
  meters: Long
) extends Storable

object MongoQueryGraphEdges {
  private val log = Log(classOf[MongoQueryGraphEdges])

  def main(args: Array[String]): Unit = {
    log.info("start")
    Mongo.executeIn("kpn-laptop") { database =>
      val query = new MongoQueryGraphEdges(database)
      database.nodes.findById(0L)
      val t1 = System.currentTimeMillis()
      query.execute()
      val t2 = System.currentTimeMillis()
      log.info(s"Total = ${t2 - t1}ms")
    }
  }
}

class MongoQueryGraphEdges(database: Database) {

  def execute(): Seq[GraphEdges] = {
    val pipeline = buildPipeline()
    log.infoElapsed {
      val edges = database.routes.aggregate(pipeline, classOf[RouteGraphEdge], log)
      val edgesByRouteType = groupByRouteType(edges)
      val message = summary(edgesByRouteType)
      (message, edgesByRouteType)
    }
  }

  private def buildPipeline(): MongoPipeline = {
    Seq(
      filter(
        and(
          equal("active", true),
          equal("base.nodeNetwork", true),
        )
      ),
      unwind("$edges"),
      unwind("$base.routeTypes"),
      project(
        fields(
          computed("routeType", "$base.routeTypes"),
          include("proposed"),
          include("_id"),
          computed("pathId", "$edges.pathId"),
          computed("sourceNodeId", "$edges.sourceNodeId"),
          computed("sinkNodeId", "$edges.sinkNodeId"),
          computed("meters", "$edges.meters")
        )
      )
    )
  }

  private def groupByRouteType(edges: Seq[RouteGraphEdge]): Seq[GraphEdges] = {
    RouteType.values.map { routeType =>
      val routeTypeEdges = edges.filter(_.routeType == routeType).map { edge =>
        GraphEdge(
          edge.sourceNodeId,
          edge.sinkNodeId,
          edge.meters,
          edge.proposed,
          TrackPathKey(edge._id, edge.pathId)
        )
      }
      GraphEdges(routeType, routeTypeEdges)
    }
  }

  private def summary(edgesByRouteType: Seq[GraphEdges]): String = {
    edgesByRouteType.map(e => s"${e.routeType.entryName}: ${e.edges.size}").mkString(", ")
  }
}
