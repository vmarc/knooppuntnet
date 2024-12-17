package kpn.api.common.location

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait BooleanParameter extends EnumEntry with Hyphencase

object BooleanParameter extends Enum[BooleanParameter] {

  val values: IndexedSeq[BooleanParameter] = findValues

  final case object Yes extends BooleanParameter

  final case object No extends BooleanParameter
}
