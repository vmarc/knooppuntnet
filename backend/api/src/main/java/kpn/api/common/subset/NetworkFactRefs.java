package kpn.api.common.subset;

import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record NetworkFactRefs(
  Long networkId,
  String networkName,
  ImmutableList<Ref> factRefs
) {}
