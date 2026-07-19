package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait Language extends EnumEntry with Hyphencase

object Language extends Enum[Language] {

  val values: IndexedSeq[Language] = findValues

  final case object EN extends Language

  final case object NL extends Language

  final case object DE extends Language

  final case object FR extends Language
}
