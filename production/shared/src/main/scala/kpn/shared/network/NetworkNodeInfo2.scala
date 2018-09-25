package kpn.shared.network

import kpn.shared.LatLon
import kpn.shared.NodeIntegrityCheck
import kpn.shared.Timestamp
import kpn.shared.common.Ref
import kpn.shared.data.Tags

case class NetworkNodeInfo2(
  id: Long,
  title: String,
  number: String,
  latitude: String,
  longitude: String,
  connection: Boolean, // true if all routes (in the network) that contain this node have role "connection" in the network relation
  roleConnection: Boolean,
  definedInRelation: Boolean,
  definedInRoute: Boolean,
  timestamp: Timestamp,
  routeReferences: Seq[Ref],
  integrityCheck: Option[NodeIntegrityCheck],
  tags: Tags
) extends LatLon
