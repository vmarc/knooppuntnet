package kpn.server.analyzer.engine.tile

import kpn.server.analyzer.engine.tiles.domain.RouteTiles
import kpn.server.analyzer.engine.tiles.domain.TileCache
import org.springframework.stereotype.Component

@Component
class RouteTileCache extends TileCache(RouteTiles)
