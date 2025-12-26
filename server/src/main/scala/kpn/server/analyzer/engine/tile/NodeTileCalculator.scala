package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.PointTileCalculator
import org.springframework.stereotype.Component

@Component
class NodeTileCalculator(routeTileCache: RouteTileCache)
  extends PointTileCalculator(routeTileCache)

