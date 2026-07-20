package kpn.api.common.node;

import kpn.api.common.common.Reference;

import com.google.common.collect.ImmutableList;

public record NodeReferences(
  ImmutableList<Reference> networkReferences,
  ImmutableList<Reference> routeReferences
) {
}
