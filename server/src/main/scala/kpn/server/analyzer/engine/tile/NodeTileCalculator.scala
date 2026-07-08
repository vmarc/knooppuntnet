package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.PointTileCalculator
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class NodeTileCalculator(routeTileCache: RouteTileCache)
  extends PointTileCalculator(routeTileCache)

