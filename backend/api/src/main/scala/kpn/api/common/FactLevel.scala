package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait FactLevel extends EnumEntry with Hyphencase

object FactLevel extends Enum[FactLevel] {

  val values: IndexedSeq[FactLevel] = findValues

  case object ERROR extends FactLevel

  case object INFO extends FactLevel

  case object OTHER extends FactLevel
}
