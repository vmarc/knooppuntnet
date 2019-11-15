package kpn.api.common.data.raw

import kpn.api.common.LatLon
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class RawNode(
  id: Long,
  latitude: String,
  longitude: String,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  tags: Tags
) extends RawElement with LatLon {
  override def isNode: Boolean = true
}
