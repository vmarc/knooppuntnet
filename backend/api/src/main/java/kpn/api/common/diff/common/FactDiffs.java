package kpn.api.common.diff.common;

import kpn.api.common.Fact;

import com.google.common.collect.ImmutableList;

public record FactDiffs(
  ImmutableList<Fact> resolved,
  ImmutableList<Fact> introduced,
  ImmutableList<Fact> remaining
) {
}

/*
package kpn.api.common.diff.common

import kpn.api.common.Fact
import kpn.core.analysis.Facts

case class FactDiffs(
  resolved: Seq[Fact] = Seq.empty,
  introduced: Seq[Fact] = Seq.empty,
  remaining: Seq[Fact] = Seq.empty
) {

  def isEmpty: Boolean = !nonEmpty

  def nonEmpty: Boolean = resolved.nonEmpty || introduced.nonEmpty || remaining.nonEmpty

  def happy: Boolean = resolved.nonEmpty

  def investigate: Boolean = introduced.exists(Facts.isError)

}

*/
