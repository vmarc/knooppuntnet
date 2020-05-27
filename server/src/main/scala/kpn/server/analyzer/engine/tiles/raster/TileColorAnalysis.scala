package kpn.server.analyzer.engine.tiles.raster
import java.awt.Color

import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileRouteSegment

class TileColorAnalysis extends TileColor {

  override def routeColor(route: TileDataRoute, segment: TileRouteSegment): Color = {
    val colorValue = route.layer match {
      case "orphan-route" => "#006000" // MainStyleColors.darkGreen
      case "incomplete-route" => "#ff0000" // MainStyleColors.red
      case "error-route" => "#ffa500" // orange
      case "route" => "#00c800" // MainStyleColors.green
      case _ => "#00c800" // MainStyleColors.green
    }
    Color.decode(colorValue)
  }

  override def nodeColor(node: TileDataNode): Color = {
    val colorString = node.layer match {
      case "error-orphan-node" => "#0000bb" // MainStyleColors.darkBlue
      case "orphan-node" => "#006000" // MainStyleColors.darkGreen
      case "error-node" => "#0000ff" // MainStyleColors.blue
      case "node" => "#00c800" // MainStyleColors.green
      case _ => "#00c800" // MainStyleColors.green
    }
    Color.decode(colorString)
  }
}
