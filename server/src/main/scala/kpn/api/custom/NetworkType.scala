package kpn.api.custom

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait NetworkType extends EnumEntry with Hyphencase

object NetworkType extends Enum[NetworkType] {

  val values: IndexedSeq[NetworkType] = findValues

  final case object hiking extends NetworkType

  final case object cycling extends NetworkType

  final case object horseRiding extends NetworkType 

  final case object canoe extends NetworkType

  final case object motorboat extends NetworkType

  final case object inlineSkating extends NetworkType
}
