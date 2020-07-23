package kpn.server.analyzer.engine.tiles.raster

import java.awt.Color

import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileRouteSegment

class TileColorAnalysis extends TileColor {

  override def routeColor(route: TileDataRoute, segment: TileRouteSegment): Color = {
    route.layer match {
      case "orphan-route" => TileColor.darkGreen
      case "incomplete-route" => TileColor.red
      case "error-route" => TileColor.orange
      case "route" => TileColor.green
      case _ => TileColor.green
    }
  }

  override def nodeColor(node: TileDataNode): Color = {
    node.layer match {
      case "error-orphan-node" => TileColor.darkBlue
      case "orphan-node" => TileColor.darkGreen
      case "error-node" => TileColor.blue
      case "node" => TileColor.green
      case _ => TileColor.green
    }
  }
}
