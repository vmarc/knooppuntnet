package kpn.core.poi

import kpn.core.poi.tags.TagExpression
import kpn.shared.tiles.ClientPoiDefinition

case class PoiDefinition(name: String, icon: String, minLevel: Int, defaultLevel: Int, expression: TagExpression) {

  def toClient: ClientPoiDefinition = {
    ClientPoiDefinition(name, icon, minLevel, defaultLevel)
  }
}
