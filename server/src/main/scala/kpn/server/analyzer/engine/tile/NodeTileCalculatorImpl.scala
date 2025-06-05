package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.PointTileCalculator
import org.springframework.stereotype.Component

@Component
class NodeTileCalculatorImpl(routeTileCache: RouteTileCache)
  extends PointTileCalculator(routeTileCache)
    with NodeTileCalculator
