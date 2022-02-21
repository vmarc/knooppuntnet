package kpn.server.analyzer.engine.tiles.raster

import java.awt.Color

import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment

class TileColorAnalysis extends TileColor {

  override def routeColor(route: TileDataRoute, segment: RouteTileSegment): Color = {
    route.layer match {
      case "orphan-route" => TileColor.darkGreen
      case "incomplete-route" => TileColor.red
      case "error-route" => TileColor.red
      case "route" => TileColor.green
      case _ => TileColor.green
    }
  }

  override def nodeColor(node: TileDataNode): Color = {
    node.layer match {
      case "error-orphan-node" => TileColor.darkRed
      case "orphan-node" => TileColor.darkGreen
      case "error-node" => TileColor.red
      case "node" => TileColor.green
      case _ => TileColor.green
    }
  }
}
