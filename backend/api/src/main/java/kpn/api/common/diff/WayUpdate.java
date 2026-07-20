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
