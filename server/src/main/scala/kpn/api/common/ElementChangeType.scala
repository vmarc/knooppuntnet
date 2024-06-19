package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait ElementChangeType extends EnumEntry

object ElementChangeType extends Enum[ElementChangeType] {

  val values: IndexedSeq[ElementChangeType] = findValues

  case object Unchanged extends ElementChangeType

  case object Added extends ElementChangeType

  case object Changed extends ElementChangeType

  case object Removed extends ElementChangeType
}
