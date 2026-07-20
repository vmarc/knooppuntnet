package kpn.api.common.tiles;

public record ClientPoiDefinition(
  String name,
  String icon,
  Long minLevel,
  Long defaultLevel
) {}
