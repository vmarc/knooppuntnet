package kpn.core.tiles.domain

import kpn.api.common.LatLon
import kpn.api.common.NodeIntegrityCheck
import kpn.api.common.common.Ref

case class TileDataNode(
  id: Long,
  name: String,
  latitude: String,
  longitude: String,
  definedInRelation: Boolean,
  routeReferences: Seq[Ref],
  integrityCheck: Option[NodeIntegrityCheck]
) extends LatLon
