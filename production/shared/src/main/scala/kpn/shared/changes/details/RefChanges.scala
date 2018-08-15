package kpn.shared.changes.details

import kpn.shared.common.Ref
import kpn.shared.common.ToStringBuilder

object RefChanges {
  val empty: RefChanges = RefChanges(Seq(), Seq())
}

case class RefChanges(
  oldRefs: Seq[Ref] = Seq(),
  newRefs: Seq[Ref] = Seq()
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    optionalCollection("oldRefs", oldRefs).
    optionalCollection("newRefs", newRefs).
    build
}
