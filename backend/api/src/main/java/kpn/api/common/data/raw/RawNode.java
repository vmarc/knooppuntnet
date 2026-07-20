package kpn.api.common.data.raw;

import kpn.api.common.LatLon;
import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;

import com.google.common.collect.ImmutableList;

public record RawNode(
  Long id,
  String latitude,
  String longitude,
  Long version,
  Timestamp timestamp,
  Long changeSetId,
  ImmutableList<Tag> tags
) implements RawElement, LatLon {
}

/* TODO migrate
package kpn.api.common.data.raw

import kpn.api.common.LatLon
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class RawNode(
  id: Long,
  latitude: String,
  longitude: String,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  tags: Seq[Tag]
) extends RawElement with LatLon {

  override def isNode: Boolean = true
}

*/
