package kpn.api.common.node;

import kpn.api.common.common.Reference;

import com.google.common.collect.ImmutableList;

public record NodeReferences(
  ImmutableList<Reference> networkReferences,
  ImmutableList<Reference> routeReferences
) {
}

/*
package kpn.api.common.node

import kpn.api.common.common.Reference

case class NodeReferences(
  networkReferences: Seq[Reference] = Seq.empty,
  routeReferences: Seq[Reference] = Seq.empty
)

*/
