package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait Country extends EnumEntry

object Country extends Enum[Country] {

  val values: IndexedSeq[Country] = findValues

  final case object nl extends Country

  final case object be extends Country

  final case object de extends Country

  final case object fr extends Country

  final case object at extends Country

  final case object es extends Country

  final case object dk extends Country
}
