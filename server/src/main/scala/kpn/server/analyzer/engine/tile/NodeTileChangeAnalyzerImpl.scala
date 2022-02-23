package kpn.server.analyzer.engine.tile

import kpn.api.custom.NetworkType
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

    val impactedNetworkTypes = NetworkType.all.filter { networkType =>
      val beforeTileNodeData = tileDataNodeBuilder.build(networkType, beforeNodeTileInfo)
      val afterTileNodeData = tileDataNodeBuilder.build(networkType, afterNodeTileInfo)
      beforeTileNodeData != afterTileNodeData
    }

    (before.tiles ++ after.tiles).distinct.filter { tile =>
      NetworkType.withName(TileName.networkType(tile)) match {
        case Some(networkType) => impactedNetworkTypes.contains(networkType)
        case None => false
      }
    }.sorted
  }

  private def toNodeTileInfo(nodeDoc: NodeDoc): NodeTileInfo = {
    NodeTileInfo(
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
