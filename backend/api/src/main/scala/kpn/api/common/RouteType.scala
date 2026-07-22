package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait RouteType extends EnumEntry with Hyphencase

object RouteType extends Enum[RouteType] {

  val values: IndexedSeq[RouteType] = findValues

  case object hiking extends RouteType

  case object cycling extends RouteType

  case object horseRiding extends RouteType

  case object canoe extends RouteType

  case object motorboat extends RouteType

  case object inlineSkating extends RouteType

  case object mtb extends RouteType
}
