package kpn.core.poi

import kpn.api.common.tiles.ClientPoiDefinition
import kpn.core.poi.tags.TagExpression

case class PoiDefinition(
  name: String,
  icon: String,
  minLevel: Long,
  defaultLevel: Long,
  expression: TagExpression
) {

  def toClient: ClientPoiDefinition = {
    ClientPoiDefinition(name, icon, minLevel, defaultLevel)
  }
}
