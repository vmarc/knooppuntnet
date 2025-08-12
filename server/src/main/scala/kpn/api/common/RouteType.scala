package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait RouteType extends EnumEntry with Hyphencase

object RouteType extends Enum[RouteType] {

  val values: IndexedSeq[RouteType] = findValues

  final case object hiking extends RouteType

  final case object cycling extends RouteType

  final case object horseRiding extends RouteType

  final case object canoe extends RouteType

  final case object motorboat extends RouteType

  final case object inlineSkating extends RouteType
}
