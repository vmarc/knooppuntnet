package kpn.api.common.diff.common

import kpn.api.custom.Fact

case class FactDiffs(
  resolved: Seq[Fact] = Seq.empty,
  introduced: Seq[Fact] = Seq.empty,
  remaining: Seq[Fact] = Seq.empty
) {

  def isEmpty: Boolean = !nonEmpty

  def nonEmpty: Boolean = resolved.nonEmpty || introduced.nonEmpty || remaining.nonEmpty

  def happy: Boolean = resolved.nonEmpty

  def investigate: Boolean = introduced.exists(_.isError)

}
