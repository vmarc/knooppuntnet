package kpn.core.poi

import kpn.api.common.tiles.ClientPoiGroupDefinition

case class PoiGroupDefinition(
  name: String,
  defaultEnabled: Boolean,
  definitions: Seq[PoiDefinition]
) {

  def toClient: ClientPoiGroupDefinition = {
    val clientDefinitions = definitions.map(d => d.toClient).distinct
    ClientPoiGroupDefinition(name, defaultEnabled, clientDefinitions)
  }
}
