package kpn.api.common.location

import kpn.api.common.LatLon
import kpn.api.common.common.Ref
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class LocationNodeInfo(
  id: Long,
  name: String,
  number: String,
  latitude: String,
  longitude: String,
  definedInRoute: Boolean,
  timestamp: Timestamp,
  routeReferences: Seq[Ref],
  facts: Seq[Fact],
  tags: Tags
) extends LatLon
