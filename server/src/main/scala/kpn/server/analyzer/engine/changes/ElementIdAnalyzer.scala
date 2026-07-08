package kpn.server.analyzer.engine.changes

import it.unimi.dsi.fastutil.longs.LongList
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.context.ChangeElementIds
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

import scala.jdk.CollectionConverters.ListHasAsScala

@Component
@Profile(Array("analysis"))
class ElementIdAnalyzer(analysisContext: AnalysisContext) {

  def routesReferencedBy(elementIds: ChangeElementIds): Set[Long] = {

    val routes = analysisContext.watched.routes

    val nodeRouteIds = references(elementIds.nodeIds, routes.routesReferencingNode)
    val wayRouteIds = references(elementIds.wayIds, routes.routesReferencingWay)
    val relationRouteIds = references(elementIds.relationIds, routes.routesReferencingRelation)
    val routeRelationIds = elementIds.relationIds.filter(routes.contains)

    nodeRouteIds ++ wayRouteIds ++ relationRouteIds ++ routeRelationIds
  }

  private def references(elementIds: Set[Long], referenceFinder: Long => Option[LongList]): Set[Long] = {
    elementIds.flatMap { elementId =>
      referenceFinder(elementId) match {
        case Some(routeIds) => routeIds.asScala.map(_.longValue())
        case None => Seq.empty
      }
    }
  }
}
