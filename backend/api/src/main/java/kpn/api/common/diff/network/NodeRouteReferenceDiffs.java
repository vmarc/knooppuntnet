package kpn.api.common.diff.network;

import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record NodeRouteReferenceDiffs(
  ImmutableList<Ref> removed,
  ImmutableList<Ref> added,
  ImmutableList<Ref> remaining
) {
}

/*
package kpn.api.common.diff.network

import kpn.api.common.common.Ref

case class NodeRouteReferenceDiffs(
  removed: Seq[Ref],
  added: Seq[Ref],
  remaining: Seq[Ref]
) {
  def nonEmpty: Boolean = removed.nonEmpty || added.nonEmpty || remaining.nonEmpty
}


*/
