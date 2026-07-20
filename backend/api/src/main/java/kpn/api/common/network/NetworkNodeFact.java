package kpn.api.common.network;

import kpn.api.common.Fact;
import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record NetworkNodeFact(
  Fact fact,
  ImmutableList<Ref> nodes
) {
}
