package kpn.api.common.node;

import kpn.api.common.common.Reference;
import kpn.api.custom.Timestamp;

import com.google.common.collect.ImmutableList;

public record MapNodeDetail(
  Long id,
  String name,
  String latitude,
  String longitude,
  Timestamp lastUpdated,
  ImmutableList<Reference> networkReferences,
  ImmutableList<Reference> routeReferences
) {
}

/*
package kpn.api.common.node

import kpn.api.common.common.Reference
import kpn.api.custom.Timestamp

case class MapNodeDetail(
  id: Long,
  name: String,
  latitude: String,
  longitude: String,
  lastUpdated: Timestamp,
  networkReferences: Seq[Reference],
  routeReferences: Seq[Reference]
)

*/
