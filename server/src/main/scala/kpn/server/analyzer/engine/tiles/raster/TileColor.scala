package kpn.server.analyzer.engine.tiles.raster

import java.awt.Color

import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileRouteSegment

object TileColor {

  def lightGreen: Color = new Color(0, 255, 0)

  def green: Color = new Color(0, 200, 0)

  def darkGreen: Color = new Color(0, 150, 0)

  def veryDarkGreen: Color = new Color(0, 90, 0)

  def darkRed: Color = new Color(187, 0, 0)

  def gray: Color = new Color(150, 150, 150)

  def orange: Color = new Color(255, 165, 0)
}

trait TileColor {

  def routeColor(route: TileDataRoute, segment: TileRouteSegment): Color

  def nodeColor(node: TileDataNode): Color

}
