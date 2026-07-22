package kpn.api.common.search

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Lowercase

sealed trait ConditionGroupOperator extends EnumEntry with Lowercase

object ConditionGroupOperator extends Enum[ConditionGroupOperator] {

  val values: IndexedSeq[ConditionGroupOperator] = findValues

  case object And extends ConditionGroupOperator

  case object Or extends ConditionGroupOperator
}
