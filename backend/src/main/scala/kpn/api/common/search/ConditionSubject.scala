package kpn.api.common.search

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Lowercase

sealed trait ConditionSubject extends EnumEntry with Lowercase

object ConditionSubject extends Enum[ConditionSubject] {

  val values: IndexedSeq[ConditionSubject] = findValues

  final case object Tag extends ConditionSubject

  final case object Location extends ConditionSubject

  final case object Name extends ConditionSubject

  final case object Group extends ConditionSubject
}
