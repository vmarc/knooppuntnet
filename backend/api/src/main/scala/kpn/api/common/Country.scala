package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait Country extends EnumEntry

object Country extends Enum[Country] {

  val values: IndexedSeq[Country] = findValues

  case object nl extends Country

  case object be extends Country

  case object de extends Country

  case object fr extends Country

  case object at extends Country

  case object es extends Country

  case object dk extends Country

  case object pl extends Country
}
