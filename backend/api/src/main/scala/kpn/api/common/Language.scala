package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait Language extends EnumEntry with Hyphencase

object Language extends Enum[Language] {

  val values: IndexedSeq[Language] = findValues

  case object EN extends Language

  case object NL extends Language

  case object DE extends Language

  case object FR extends Language
}
