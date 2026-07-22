package kpn.api.common.changes

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait ChangeAction extends EnumEntry with Hyphencase

object ChangeAction extends Enum[ChangeAction] {

  val values: IndexedSeq[ChangeAction] = findValues

  case object Create extends ChangeAction

  case object Modify extends ChangeAction

  case object Delete extends ChangeAction
}
