package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.analysis.route.domain.RouteTileDoc
import org.springframework.stereotype.Component

@Component
class RouteTileChangeAnalyzerImpl extends RouteTileChangeAnalyzer {

  def impactedTiles(before: Seq[RouteTileDoc], after: Seq[RouteTileDoc]): Seq[String] = {
    val beforeTileIds = before.map(_._id).toSet
    val afterTileIds = after.map(_._id).toSet
    val createdTileIds = afterTileIds -- beforeTileIds
    val deletedTileIds = beforeTileIds -- afterTileIds
    val updatedTileIds = afterTileIds.intersect(beforeTileIds)
    val changedTileIds = updatedTileIds.filter { tileId =>
      val beforeTileDocOption = before.find(_._id == tileId)
      val afterTileDocOption = after.find(_._id == tileId)
      beforeTileDocOption != afterTileDocOption
    }
    (createdTileIds ++ deletedTileIds ++ changedTileIds).toSeq.sorted
  }
}
