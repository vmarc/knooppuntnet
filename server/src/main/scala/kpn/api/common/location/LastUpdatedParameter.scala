package kpn.api.common.location

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait LastUpdatedParameter extends EnumEntry with Hyphencase

object LastUpdatedParameter extends Enum[LastUpdatedParameter] {

  val values: IndexedSeq[LastUpdatedParameter] = findValues

  final case object lastWeek extends LastUpdatedParameter

  final case object lastYear extends LastUpdatedParameter

  final case object older extends LastUpdatedParameter
}
