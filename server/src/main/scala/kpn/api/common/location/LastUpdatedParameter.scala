package kpn.api.common.location

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait LastUpdatedParameter extends EnumEntry

object LastUpdatedParameter extends Enum[LastUpdatedParameter] {

  val values: IndexedSeq[LastUpdatedParameter] = findValues

  case object lastWeek extends LastUpdatedParameter

  case object lastYear extends LastUpdatedParameter

  case object older extends LastUpdatedParameter
}
