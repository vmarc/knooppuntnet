package kpn.api.common.tiles

case class ClientPoiGroupDefinition(
  name: String,
  enabledDefault: Boolean,
  poiDefinitions: Seq[ClientPoiDefinition]
)
