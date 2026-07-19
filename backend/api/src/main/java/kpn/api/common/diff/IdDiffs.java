package kpn.api.common.diff;

import com.google.common.collect.ImmutableList;

public record IdDiffs(
  ImmutableList<Long> removed,
  ImmutableList<Long> added,
  ImmutableList<Long> updated
) {
}

/*
package kpn.api.common.diff

object IdDiffs {
  def empty: IdDiffs = IdDiffs()
}

case class IdDiffs(
  removed: Seq[Long] = Seq.empty,
  added: Seq[Long] = Seq.empty,
  updated: Seq[Long] = Seq.empty
) {

  def ids: Seq[Long] = removed ++ added ++ updated

  def nonEmpty: Boolean = removed.nonEmpty || added.nonEmpty || updated.nonEmpty
}

*/
