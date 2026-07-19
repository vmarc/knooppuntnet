package kpn.api.common.data;

import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;

import com.google.common.collect.ImmutableList;

public record Node(
  Long id,
  String latitude,
  String longitude,
  Long version,
  Timestamp timestamp,
  Long changeSetId,
  ImmutableList<Tag> tags
) {
}

/*
package kpn.api.common.data

import kpn.api.common.LatLon
import kpn.api.common.data.raw.RawNode
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class Node(
  id: Long,
  latitude: String,
  longitude: String,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  tags: Seq[Tag]
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

*/
