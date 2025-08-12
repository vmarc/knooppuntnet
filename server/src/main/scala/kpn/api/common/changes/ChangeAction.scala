package kpn.api.common.changes

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait ChangeAction extends EnumEntry with Hyphencase

object ChangeAction extends Enum[ChangeAction] {

  val values: IndexedSeq[ChangeAction] = findValues

  final case object Create extends ChangeAction

  final case object Modify extends ChangeAction

  final case object Delete extends ChangeAction
}
