package kpn.api.common.diff;

import kpn.api.common.common.Ref;

import com.google.common.collect.ImmutableList;

public record RefDiffs(
  ImmutableList<Ref> removed,
  ImmutableList<Ref> added,
  ImmutableList<Ref> updated
) {
}

/*
package kpn.api.common.diff

import kpn.api.common.common.Ref

object RefDiffs {
  def empty: RefDiffs = RefDiffs()
}

case class RefDiffs(
  removed: Seq[Ref] = Seq.empty,
  added: Seq[Ref] = Seq.empty,
  updated: Seq[Ref] = Seq.empty
) {

  def ids: Seq[Long] = removed.map(_.id) ++ added.map(_.id) ++ updated.map(_.id)

  def nonEmpty: Boolean = removed.nonEmpty || added.nonEmpty || updated.nonEmpty
}

*/
