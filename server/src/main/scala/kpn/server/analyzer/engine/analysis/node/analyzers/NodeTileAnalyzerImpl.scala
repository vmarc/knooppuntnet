package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import org.springframework.stereotype.Component

@Component
class NodeTileAnalyzerImpl(nodeTileCalculator: NodeTileCalculator) extends NodeTileAnalyzer {
  override def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    val tiles = (ZoomLevel.nodeMinZoom to ZoomLevel.vectorTileMaxZoom).flatMap { z =>
      nodeTileCalculator.tiles(z, analysis.node)
    }
    val tileNames = tiles.flatMap { tile =>
      analysis.nodeNames.map(_.networkType).map { networkType =>
        s"${networkType.name}-${tile.name}"
      }
    }
    analysis.copy(tiles = tileNames)
  }
}
