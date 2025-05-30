package kpn.server.analyzer.engine.tile

import kpn.api.common.RouteType
import kpn.core.doc.NodeDoc
import kpn.server.analyzer.engine.tiles.TileDataNodeBuilder
import kpn.server.analyzer.engine.tiles.domain.NodeTileInfo
import org.springframework.stereotype.Component

@Component
class NodeTileChangeAnalyzerImpl(
  tileDataNodeBuilder: TileDataNodeBuilder
) extends NodeTileChangeAnalyzer {

  def impactedTiles(before: NodeDoc, after: NodeDoc): Seq[String] = {

    val beforeNodeTileInfo = toNodeTileInfo(before)
    val afterNodeTileInfo = toNodeTileInfo(after)

    val impactedrouteTypes = RouteType.values.filter { routeType =>
      val beforeTileNodeData = tileDataNodeBuilder.build(routeType, beforeNodeTileInfo)
      val afterTileNodeData = tileDataNodeBuilder.build(routeType, afterNodeTileInfo)
      beforeTileNodeData != afterTileNodeData
    }

    //    (before.tiles ++ after.tiles).distinct.filter { tile =>
    //      routeType.withNameOption(TileName.routeType(tile)) match {
    //        case Some(routeType) => impactedrouteTypes.contains(routeType)
    //        case None => false
    //      }
    //    }.sorted
    Seq.empty
  }

  private def toNodeTileInfo(nodeDoc: NodeDoc): NodeTileInfo = {
    NodeTileInfo(
      "TODO redesign",
      nodeDoc._id,
      nodeDoc.names,
      nodeDoc.latitude,
      nodeDoc.longitude,
      nodeDoc.lastSurvey,
      nodeDoc.tags,
      nodeDoc.facts
    )
  }
}
