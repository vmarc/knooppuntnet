package kpn.api.common.search

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Lowercase

sealed trait ConditionSubject extends EnumEntry with Lowercase

object ConditionSubject extends Enum[ConditionSubject] {

  val values: IndexedSeq[ConditionSubject] = findValues

  case object Tag extends ConditionSubject

  case object Location extends ConditionSubject

  case object Name extends ConditionSubject

  case object Group extends ConditionSubject
}
