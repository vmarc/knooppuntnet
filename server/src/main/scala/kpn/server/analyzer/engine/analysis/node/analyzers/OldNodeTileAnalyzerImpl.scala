package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import org.springframework.stereotype.Component

@Component
class OldNodeTileAnalyzerImpl(nodeTileCalculator: NodeTileCalculator) extends OldNodeTileAnalyzer {
  override def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    val tiles = (ZoomLevel.nodeMinZoom to ZoomLevel.vectorTileMaxZoom).flatMap { z =>
      nodeTileCalculator.tiles(z, analysis.node)
    }
    val tileNames = tiles.flatMap { tile =>
      analysis.nodeNames.map(_.routeType).map { routeType =>
        s"${routeType.entryName}-${tile.name}"
      }
    }
    analysis.copy(tiles = tileNames)
  }
}
