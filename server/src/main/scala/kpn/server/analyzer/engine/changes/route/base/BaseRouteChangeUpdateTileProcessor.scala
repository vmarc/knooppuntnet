package kpn.server.analyzer.engine.changes.route.base

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class BaseRouteChangeUpdateTileProcessor(
  routeRepository: RouteRepository
) {

  case class TileActions(deletes: Set[String], saves: Set[String]) {
    def tileIds: Seq[String] = (deletes ++ saves).toSeq.sorted
  }

  def process(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext = {
    val beforeRouteTileInfos = routeRepository.routeTiles(context.routeId)
    val afterRouteTileInfos = RouteTileInfoBuilder.build(context)
    val tileActions = analyzeTileActions(beforeRouteTileInfos, afterRouteTileInfos)
    applyTileUpdates(tileActions, afterRouteTileInfos)
    changeSetContext.withImpact(
      tileIds = tileActions.tileIds
    )
  }

  private def analyzeTileActions(beforeRouteTileInfos: Seq[RouteTileInfo], afterRouteTileInfos: Seq[RouteTileInfo]): TileActions = {
    val beforeTileIds = beforeRouteTileInfos.map(_._id).toSet
    val afterTileIds = afterRouteTileInfos.map(_._id).toSet

    val creates = afterTileIds -- beforeTileIds
    val deletes = beforeTileIds -- afterTileIds
    val commons = afterTileIds.intersect(beforeTileIds)

    val changes = commons.filter { tileId =>
      val before = beforeRouteTileInfos.find(_._id == tileId)
      val after = afterRouteTileInfos.find(_._id == tileId)
      before != after
    }
    TileActions(deletes, creates ++ changes)
  }

  private def applyTileUpdates(tileActions: TileActions, afterTileDocs: Seq[RouteTileInfo]): Unit = {
    applyTileDeletes(tileActions.deletes)
    applyTileSaves(tileActions.saves, afterTileDocs)
  }

  private def applyTileDeletes(deletes: Set[String]): Unit = {
    deletes.foreach(routeRepository.deleteRouteTile)
  }

  private def applyTileSaves(saves: Set[String], afterRouteTileInfos: Seq[RouteTileInfo]): Unit = {
    afterRouteTileInfos
      .filter(doc => saves.contains(doc._id))
      .foreach(routeRepository.saveRouteTile)
  }
}
