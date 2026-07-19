package kpn.api.common.tiles;

import kpn.api.common.tiles.ClientPoiDefinition;

import com.google.common.collect.ImmutableList;

public record ClientPoiGroupDefinition(
  String name,
  Boolean enabledDefault,
  ImmutableList<ClientPoiDefinition> poiDefinitions
) {
}

/*
package kpn.api.common.tiles

case class ClientPoiGroupDefinition(
  name: String,
  enabledDefault: Boolean,
  poiDefinitions: Seq[ClientPoiDefinition]
)

*/
