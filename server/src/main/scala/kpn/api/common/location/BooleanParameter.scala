package kpn.api.common.location

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait BooleanParameter extends EnumEntry

object BooleanParameter extends Enum[BooleanParameter] {

  val values: IndexedSeq[BooleanParameter] = findValues

  case object yes extends BooleanParameter

  case object no extends BooleanParameter
}
