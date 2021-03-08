package kpn.api.common.diff.common

import kpn.api.common.common.ToStringBuilder
import kpn.api.custom.Fact

case class FactDiffs(
  resolved: Set[Fact] = Set.empty,
  introduced: Set[Fact] = Set.empty,
  remaining: Set[Fact] = Set.empty
) {

  def isEmpty: Boolean = !nonEmpty

  def nonEmpty: Boolean = resolved.nonEmpty || introduced.nonEmpty || remaining.nonEmpty

  def happy: Boolean = resolved.nonEmpty

  def investigate: Boolean = introduced.exists(_.isError)

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("resolved", resolved).
    field("introduced", introduced).
    field("remaining", remaining).
    build
}
