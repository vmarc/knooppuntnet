package kpn.server.analyzer.engine.changes.route

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeUpdateTileProcessorImpl(
  routeRepository: RouteRepository
) extends BaseRouteChangeUpdateTileProcessor {

  case class TileActions(deletes: Set[String], saves: Set[String]) {
    def tileIds: Seq[String] = (deletes ++ saves).toSeq.sorted
  }

  def process(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext = {
    val beforeTileDocs = routeRepository.routeTiles(context.routeId)
    val afterTileDocs = RouteTileDocBuilder.build(context)
    val tileActions = analyzeTileActions(beforeTileDocs, afterTileDocs)
    applyTileUpdates(tileActions, afterTileDocs)
    changeSetContext.withImpact(
      tileIds = tileActions.tileIds
    )
  }

  private def analyzeTileActions(beforeTileDocs: Seq[RouteTileDoc], afterTileDocs: Seq[RouteTileDoc]): TileActions = {
    val beforeTileIds = beforeTileDocs.map(_._id).toSet
    val afterTileIds = afterTileDocs.map(_._id).toSet

    val creates = afterTileIds -- beforeTileIds
    val deletes = beforeTileIds -- afterTileIds
    val commons = afterTileIds.intersect(beforeTileIds)

    val changes = commons.filter { tileId =>
      val before = beforeTileDocs.find(_._id == tileId)
      val after = afterTileDocs.find(_._id == tileId)
      before != after
    }
    TileActions(deletes, creates ++ changes)
  }

  private def applyTileUpdates(tileActions: TileActions, afterTileDocs: Seq[RouteTileDoc]): Unit = {
    applyTileDeletes(tileActions.deletes)
    applyTileSaves(tileActions.saves, afterTileDocs)
  }

  private def applyTileDeletes(deletes: Set[String]): Unit = {
    deletes.foreach(routeRepository.deleteRouteTile)
  }

  private def applyTileSaves(saves: Set[String], afterTileDocs: Seq[RouteTileDoc]): Unit = {
    afterTileDocs
      .filter(doc => saves.contains(doc._id))
      .foreach(routeRepository.saveRouteTile)
  }
}
