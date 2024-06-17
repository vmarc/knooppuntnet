package kpn.api.common.data

import kpn.api.common.LatLon
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class Node(
  id: Long,
  latitude: String,
  longitude: String,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  tags: Tags
) extends Element with LatLon {
  override def isNode: Boolean = true

  def toRaw: RawNode = {
    RawNode(
      id,
      latitude,
      longitude,
      version,
      timestamp,
      changeSetId,
      tags
    )
  }
}
