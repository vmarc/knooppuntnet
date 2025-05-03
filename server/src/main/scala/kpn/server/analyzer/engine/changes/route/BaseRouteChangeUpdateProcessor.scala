package kpn.server.analyzer.engine.changes.route

import kpn.api.common.Fact
import kpn.api.custom.Relation
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteDocBuilder
import kpn.server.analyzer.engine.analysis.route.base.BaseRouteMainAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.tile.RouteTileChangeAnalyzer
import kpn.server.repository.RawDataRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeUpdateProcessor(
  analysisContext: AnalysisContext,
  baseRouteMainAnalyzer: BaseRouteMainAnalyzer,
  routeTileChangeAnalyzer: RouteTileChangeAnalyzer,
  routeRepository: RouteRepository,
  rawDataRepository: RawDataRepository,
) {

  private val log = Log(classOf[BaseRouteChangeUpdateProcessor])

  def process(changeSetContext: ChangeSetContext, routeIds: Seq[Long]): ChangeSetContext = {
    val impacts = routeIds.flatMap { routeId =>
      processRoute(changeSetContext, routeId)
    }

    changeSetContext.copy(
      baseRouteUpdatedIds = routeIds,
    ).withImpact(
      tiles = impacts.flatMap(_.impactedTiles),
      nodeIds = impacts.flatMap(_.impactedNodeIds),
      routeIds = routeIds,
    )
  }

  private def processRoute(changeSetContext: ChangeSetContext, routeId: Long): Option[ChangeImpact] = {
    rawDataRepository.route(changeSetContext.changeSet.timestampAfter, routeId) match {
      case None =>
        // TODO report?
        None
      case Some(rawRouteDoc) =>
        val beforeOption = routeRepository.findBaseRouteById(routeId)
        analyzeBaseRoute(rawRouteDoc.relation, rawRouteDoc.structure) match {
          case None => None
          case Some(baseRouteDoc) =>
            beforeOption match {
              case None =>
                Some(
                  ChangeImpact(
                    impactedNodeIds = baseRouteDoc.nodes.nodeIds,
                    impactedTiles = baseRouteDoc.tiles
                  )
                )

              case Some(before) =>
                val beforeNodeIds = before.nodes.nodeIds.toSet
                val afterNodeIds = baseRouteDoc.nodes.nodeIds.toSet
                val addedNodeIds = afterNodeIds -- beforeNodeIds
                val removedNodeIds = beforeNodeIds -- afterNodeIds
                val impactedNodeIds = (addedNodeIds ++ removedNodeIds).toSeq.sorted
                val impactedTiles = routeTileChangeAnalyzer.impactedTiles(before, baseRouteDoc)
                Some(
                  ChangeImpact(
                    impactedNodeIds = (addedNodeIds ++ removedNodeIds).toSeq.sorted,
                    impactedTiles = impactedTiles
                  )
                )
            }
        }
    }
  }

  private def analyzeBaseRoute(relation: Relation, hierarchy: Option[RouteRelation]): Option[BaseRouteDoc] = {
    val context = baseRouteMainAnalyzer.analyze(relation, hierarchy)
    if (context.abort) {
      if (context.facts.contains(Fact.RouteTagMissing)) {
        routeRepository.findBaseRouteById(relation.id) match {
          case None =>
          case Some(baseRouteDoc) =>
            // TODO redesign - remove corresponding route tile doc !!!
            routeRepository.saveBaseRoute(baseRouteDoc.deactivated)
        }
        analysisContext.watched.routes.delete(relation.id)
        None
      }
      else {
        None
      }
    }
    else {
      val baseRouteDoc = new BaseRouteDocBuilder(context).build()
      analysisContext.watched.routes.add(relation.id, context.elementIds)
      routeRepository.saveBaseRoute(baseRouteDoc)
      context.tileDatas.foreach { tileData =>
        val doc = RouteTileDoc(
          _id = s"${tileData.name}-${context.relation.id}",
          routeId = context.relation.id,
          routeName = context.routeNameAnalysis.name.getOrElse("no-name"), // TODO redesign tiles - can do better?
          routeTypes = context.routeTypes,
          z = tileData.z,
          x = tileData.x,
          y = tileData.y,
          layer = tileData.layer,
          scope = tileData.scope,
          survey = tileData.survey,
          error = tileData.error,
          segments = tileData.segments
        )
        routeRepository.saveRouteTile(doc)
      }
      Some(baseRouteDoc)
    }
  }
}
