package kpn.server.analyzer.engine.tiles.raster

import kpn.api.common.FeatureLayer
import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileDataRouteSegment

import java.awt.Color

class TileColorAnalysis extends TileColor {

  override def routeColor(route: TileDataRoute, segment: TileDataRouteSegment): Color = {
    route.layer match {
      case FeatureLayer.orphanRoute => TileColor.darkGreen
      case FeatureLayer.incompleteRoute => TileColor.red
      case FeatureLayer.errorRoute => TileColor.red
      case FeatureLayer.route => TileColor.green
      case _ => TileColor.green
    }
  }

  override def nodeColor(node: TileDataNode): Color = {
    node.layer match {
      case FeatureLayer.errorOrphanNode => TileColor.darkRed
      case FeatureLayer.orphanNode => TileColor.darkGreen
      case FeatureLayer.errorNode => TileColor.red
      case FeatureLayer.node => TileColor.green
      case _ => TileColor.green
    }
  }
}
