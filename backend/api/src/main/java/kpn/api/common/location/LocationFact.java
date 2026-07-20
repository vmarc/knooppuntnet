package kpn.api.common.location;

import kpn.api.common.Fact;
import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record LocationFact(
  String elementType,
  Fact fact,
  ImmutableList<Ref> refs
) {
}
