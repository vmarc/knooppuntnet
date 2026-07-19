package kpn.api.common.tiles;

public record ClientPoiDefinition(
  String name,
  String icon,
  Long minLevel,
  Long defaultLevel
) {
}

/*
package kpn.api.common.tiles

case class ClientPoiDefinition(name: String, icon: String, minLevel: Long, defaultLevel: Long)

*/
