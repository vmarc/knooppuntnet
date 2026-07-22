package kpn.api.common.search

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait ConditionOperator extends EnumEntry with Hyphencase

object ConditionOperator extends Enum[ConditionOperator] {

  val values: IndexedSeq[ConditionOperator] = findValues

  case object Equals extends ConditionOperator

  case object Contains extends ConditionOperator

  case object StartsWith extends ConditionOperator

  case object EndsWith extends ConditionOperator
}
