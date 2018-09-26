package kpn.shared.diff

import kpn.shared.common.ToStringBuilder

object IdDiffs {
  def empty: IdDiffs = IdDiffs()
}

case class IdDiffs(
  removed: Seq[Long] = Seq.empty,
  added: Seq[Long] = Seq.empty,
  updated: Seq[Long] = Seq.empty
) {

  def ids: Seq[Long] = removed ++ added ++ updated

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    optionalCollection("removed", removed).
    optionalCollection("added", added).
    optionalCollection("updated", updated).
    build
}
