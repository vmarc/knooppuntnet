package kpn.shared.diff

import kpn.shared.common.ToStringBuilder

object IdDiffs {
  def empty: IdDiffs = IdDiffs()
}

case class IdDiffs(
  removed: Seq[Long] = Seq(),
  added: Seq[Long] = Seq(),
  updated: Seq[Long] = Seq()
) {

  def ids: Seq[Long] = removed ++ added ++ updated

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    optionalCollection("removed", removed).
    optionalCollection("added", added).
    optionalCollection("updated", updated).
    build
}
