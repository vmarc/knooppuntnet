package kpn.core.tools.support

import kpn.api.common.common.TrackPath
import kpn.api.common.route.RouteEdge
import kpn.core.mongo.Database
import kpn.core.mongo.util.Mongo

object MigrateRoutesTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test-2") { database =>
      new MigrateRoutesTool(database).migrate()
    }
  }
}

class MigrateRoutesTool(database: Database) {

  def migrate(): Unit = {
    val routeIds = database.routes.ids()
    routeIds.zipWithIndex.foreach { case (routeId, index) =>
      if ((index % 100) == 0) {
        println(s"${index + 1}/${routeIds.size}")
      }
      database.routes.findById(routeId) match {
        case None =>
        case Some(routeInfo) =>
          val routeMap = routeInfo.analysis.map
          val edges = Seq(
            toEdges(routeMap.forwardPath),
            toEdges(routeMap.backwardPath),
            routeMap.freePaths.map(toEdge),
            routeMap.startTentaclePaths.map(toEdge),
            routeMap.endTentaclePaths.map(toEdge),
          ).flatten
          val updatedRouteInfo = routeInfo.copy(edges = edges)
          database.routes.save(updatedRouteInfo)
      }
    }
  }

  private def toEdges(trackPathOption: Option[TrackPath]): Seq[RouteEdge] = {
    trackPathOption match {
      case None => Seq.empty
      case Some(trackPath) =>
        if (trackPath.oneWay) {
          Seq(toEdge(trackPath))
        }
        else {
          Seq(
            toEdge(trackPath),
            toReverseEdge(trackPath)
          )
        }
    }
  }

  private def toEdge(trackPath: TrackPath): RouteEdge = {
    RouteEdge(
      trackPath.pathId,
      trackPath.startNodeId,
      trackPath.endNodeId,
      trackPath.meters
    )
  }

  private def toReverseEdge(trackPath: TrackPath): RouteEdge = {
    RouteEdge(
      100L + trackPath.pathId,
      trackPath.endNodeId,
      trackPath.startNodeId,
      trackPath.meters
    )
  }
}
