package kpn.api.common.data.raw;

import kpn.api.common.LatLon;
import kpn.api.custom.Tag;
import kpn.api.custom.Timestamp;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList;

public record RawWay(
  Long id,
  Long version,
  Timestamp timestamp,
  Long changeSetId,
  ImmutableList<Long> nodeIds,
  ImmutableList<Tag> tags
) implements RawElement {}

/* TODO migrate
package kpn.api.common.data.raw

import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class RawWay(
  id: Long,
  version: Long,
  timestamp: Timestamp,
  changeSetId: Long,
  nodeIds: Vector[Long],
  tags: Seq[Tag]
) extends RawElement {

  override def isWay: Boolean = true
}

*/
