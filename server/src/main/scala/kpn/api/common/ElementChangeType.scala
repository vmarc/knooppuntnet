package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait ElementChangeType extends EnumEntry with Hyphencase

object ElementChangeType extends Enum[ElementChangeType] {

  val values: IndexedSeq[ElementChangeType] = findValues

  final case object Unchanged extends ElementChangeType

  final case object Added extends ElementChangeType

  final case object Changed extends ElementChangeType

  final case object Removed extends ElementChangeType
}
