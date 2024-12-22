package kpn.api.common.search

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Lowercase

sealed trait ConditionType extends EnumEntry with Lowercase

object ConditionType extends Enum[ConditionType] {

  val values: IndexedSeq[ConditionType] = findValues

  final case object Tag extends ConditionType

  final case object Location extends ConditionType

  final case object Name extends ConditionType

  final case object Group extends ConditionType
}
