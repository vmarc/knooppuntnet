package kpn.api.common.diff;

import kpn.api.common.data.MetaData;
import kpn.api.common.diff.TagDiffs;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record WayUpdate(
  Long id,
  MetaData before,
  MetaData after,
  ImmutableList<Long> removedNodeIds,
  ImmutableList<Long> addedNodeIds,
  Boolean directionReversed,
  Optional<TagDiffs> tagDiffs
) {
}

/*
package kpn.api.common.diff

import kpn.api.common.data.MetaData

case class WayUpdate(
  id: Long,
  before: MetaData,
  after: MetaData,
  removedNodeIds: Seq[Long],
  addedNodeIds: Seq[Long],
  directionReversed: Boolean,
  tagDiffs: Option[TagDiffs]
)

*/
