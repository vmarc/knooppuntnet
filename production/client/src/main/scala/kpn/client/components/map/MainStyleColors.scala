// TODO migrate to Angular
package kpn.client.components.map

object MainStyleColors {
  val green = ol.Color(0, 200, 0) // regular nodes and routes
  val darkGreen = ol.Color(0, 96,0) // orphan nodes and routes
  val red = ol.Color(255, 0, 0) // orphan
  val darkRed = ol.Color(187, 0, 0) // orphan error
  val blue = ol.Color(0, 0, 255) // orphan error
  val darkBlue = ol.Color(0, 0, 187) // orphan error
  val gray = ol.Color(150, 150, 150) // nodes and routes that do not belong to the current network

}
