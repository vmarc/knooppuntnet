package kpn.shared.data.raw

import kpn.shared.LatLon
import kpn.shared.Timestamp
import kpn.shared.data.Tags

case class RawNode(
  id: Long,
  latitude: String,
  longitude: String,
  version: Int,
  timestamp: Timestamp,
  changeSetId: Long,
  tags: Tags
) extends RawElement with LatLon {
  override def isNode: Boolean = true
}
