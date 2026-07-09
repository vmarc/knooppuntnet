package kpn.api.common.changes.details

import kpn.api.common.common.Ref

object RefChanges {
  val empty: RefChanges = RefChanges(Seq.empty, Seq.empty)
}

case class RefChanges(
  oldRefs: Seq[Ref] = Seq.empty,
  newRefs: Seq[Ref] = Seq.empty
) {
}
