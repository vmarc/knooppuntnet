package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait ElementChangeType extends EnumEntry

object ElementChangeType extends Enum[ElementChangeType] {

  val values: IndexedSeq[ElementChangeType] = findValues

  final case object Unchanged extends ElementChangeType

  final case object Added extends ElementChangeType

  final case object Changed extends ElementChangeType

  final case object Removed extends ElementChangeType
}
