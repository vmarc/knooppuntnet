package kpn.api.common;

import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record RoutesFact(
  ImmutableList<Ref> routes
) {
}
