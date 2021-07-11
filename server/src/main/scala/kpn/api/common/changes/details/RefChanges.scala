package kpn.api.common.changes.details

import kpn.api.common.common.Ref
import kpn.api.common.common.ToStringBuilder

object RefChanges {
  val empty: RefChanges = RefChanges(Seq.empty, Seq.empty)
}

case class RefChanges(
  oldRefs: Seq[Ref] = Seq.empty,
  newRefs: Seq[Ref] = Seq.empty
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    optionalCollection("oldRefs", oldRefs).
    optionalCollection("newRefs", newRefs).
    build
}
