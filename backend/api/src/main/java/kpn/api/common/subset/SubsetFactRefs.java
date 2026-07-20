package kpn.api.common.subset;

import com.google.common.collect.ImmutableList;

public record SubsetFactRefs(
  String elementType,
  ImmutableList<Long> elementIds
) {}
