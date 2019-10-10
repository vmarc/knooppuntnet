package kpn.shared.tiles

case class ClientPoiGroupDefinition(name: String, enabledDefault: Boolean, poiDefinitions: Seq[ClientPoiDefinition])
