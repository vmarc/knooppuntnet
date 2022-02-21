package kpn.server.analyzer.engine.tiles.raster

import java.awt.Color

import kpn.server.analyzer.engine.tiles.domain.TileDataNode
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment

// See also: Typescript MainStyleColors
object TileColor {

  val gray: Color = new Color(200, 200, 200)

  val lightGreen: Color = new Color(0, 255, 0)

  val green: Color = new Color(0, 200, 0)

  val darkGreen: Color = new Color(0, 150, 0)

  val veryDarkGreen: Color = new Color(0, 90, 0)

  val red: Color = new Color(255, 0, 0)

  val darkRed: Color = new Color(187, 0, 0)

  val orange: Color = new Color(255, 165, 0)

  val blue: Color = new Color(0, 0, 255)

  val darkBlue: Color = new Color(0, 0, 187)
}

trait TileColor {

  def routeColor(route: TileDataRoute, segment: RouteTileSegment): Color

  def nodeColor(node: TileDataNode): Color

}
