package kpn.api.common.tiles;

import kpn.api.common.tiles.ClientPoiGroupDefinition;

import com.google.common.collect.ImmutableList;

public record ClientPoiConfiguration(
  ImmutableList<ClientPoiGroupDefinition> groupDefinitions
) {
}

/*
package kpn.api.common.tiles

case class ClientPoiConfiguration(groupDefinitions: Seq[ClientPoiGroupDefinition])

*/
