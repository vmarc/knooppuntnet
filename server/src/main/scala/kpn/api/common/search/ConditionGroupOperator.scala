package kpn.api.common.search

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Lowercase

sealed trait ConditionGroupOperator extends EnumEntry with Lowercase

object ConditionGroupOperator extends Enum[ConditionGroupOperator] {

  val values: IndexedSeq[ConditionGroupOperator] = findValues

  final case object And extends ConditionGroupOperator

  final case object Or extends ConditionGroupOperator
}
