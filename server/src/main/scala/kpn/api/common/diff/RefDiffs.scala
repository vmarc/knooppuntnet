package kpn.api.common.diff

import kpn.api.common.common.Ref
import kpn.api.common.common.ToStringBuilder

object RefDiffs {
  def empty: RefDiffs = RefDiffs()
}

case class RefDiffs(
  removed: Seq[Ref] = Seq.empty,
  added: Seq[Ref] = Seq.empty,
  updated: Seq[Ref] = Seq.empty
) {

  def ids: Seq[Long] = removed.map(_.id) ++ added.map(_.id) ++ updated.map(_.id)

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    optionalCollection("removed", removed).
    optionalCollection("added", added).
    optionalCollection("updated", updated).
    build
}
