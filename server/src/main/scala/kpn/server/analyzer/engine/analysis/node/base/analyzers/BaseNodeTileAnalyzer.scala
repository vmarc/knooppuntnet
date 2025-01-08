package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.api.common.tiles.ZoomLevel
import kpn.server.analyzer.engine.tile.NodeTileCalculator
import org.springframework.stereotype.Component

@Component
class BaseNodeTileAnalyzer(nodeTileCalculator: NodeTileCalculator) extends BaseNodeAnalyzer {
  override def analyze(context: BaseNodeAnalysisContext): BaseNodeAnalysisContext = {
    val tiles = (ZoomLevel.nodeMinZoom to ZoomLevel.vectorTileMaxZoom).flatMap { z =>
      nodeTileCalculator.tiles(z, context.node)
    }
    val tileNames = tiles.flatMap { tile =>
      context.names.map(_.routeType).map { routeType =>
        s"${routeType.entryName}-${tile.name}"
      }
    }
    context.copy(_tiles = Some(tileNames))
  }
}
