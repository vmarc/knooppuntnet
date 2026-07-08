package kpn.server.analyzer.engine.changes.route.base

import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteTileInfo
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.repository.RouteTileRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class BaseRouteChangeUpdateTileProcessor(routeTileRepository: RouteTileRepository) {

  case class TileActions(deleteTileIds: Set[String], saveTileIds: Set[String]) {
    def tileIds: Seq[String] = (deleteTileIds ++ saveTileIds).toSeq.sorted
  }

  def process(changeSetContext: ChangeSetContext, context: BaseRouteAnalysisContext): ChangeSetContext = {
    val beforeRouteTileInfos = routeTileRepository.routeTiles(context.routeId)
    val afterRouteTileInfos = RouteTileInfoBuilder.build(context)
    val tileActions = analyzeTileActions(beforeRouteTileInfos, afterRouteTileInfos)
    applyRouteTileUpdates(tileActions, afterRouteTileInfos)
    changeSetContext.withImpact(
      tileIds = tileActions.tileIds
    )
  }

  private def analyzeTileActions(beforeRouteTileInfos: Seq[RouteTileInfo], afterRouteTileInfos: Seq[RouteTileInfo]): TileActions = {
    val beforeTileIds = beforeRouteTileInfos.map(_._id).toSet
    val afterTileIds = afterRouteTileInfos.map(_._id).toSet

    val createTileIds = afterTileIds -- beforeTileIds
    val deleteTileIds = beforeTileIds -- afterTileIds
    val commonTileIds = afterTileIds.intersect(beforeTileIds)

    val changeTileIds = commonTileIds.filter { tileId =>
      val before = beforeRouteTileInfos.find(_._id == tileId)
      val after = afterRouteTileInfos.find(_._id == tileId)
      before != after
    }
    TileActions(deleteTileIds, createTileIds ++ changeTileIds)
  }

  private def applyRouteTileUpdates(tileActions: TileActions, afterTileDocs: Seq[RouteTileInfo]): Unit = {
    applyRouteTileDeletes(tileActions.deleteTileIds)
    applyRouteTileSaves(tileActions.saveTileIds, afterTileDocs)
  }

  private def applyRouteTileDeletes(deleteTileIds: Set[String]): Unit = {
    deleteTileIds.foreach(routeTileRepository.deleteRouteTile)
  }

  private def applyRouteTileSaves(saveTileIds: Set[String], afterRouteTileInfos: Seq[RouteTileInfo]): Unit = {
    afterRouteTileInfos
      .filter(doc => saveTileIds.contains(doc._id))
      .foreach(routeTileRepository.saveRouteTile)
  }
}
