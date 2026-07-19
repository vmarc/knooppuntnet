package kpn.api.common.changes.details;

import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record RefChanges(
  ImmutableList<Ref> oldRefs,
  ImmutableList<Ref> newRefs
) {
}

/*
package kpn.api.common.changes.details

import kpn.api.common.common.Ref

object RefChanges {
  val empty: RefChanges = RefChanges(Seq.empty, Seq.empty)
}

case class RefChanges(
  oldRefs: Seq[Ref] = Seq.empty,
  newRefs: Seq[Ref] = Seq.empty
) {
}

*/
