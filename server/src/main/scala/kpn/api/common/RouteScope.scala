package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait RouteScope extends EnumEntry with Hyphencase

object RouteScope extends Enum[RouteScope] {

  val values: IndexedSeq[RouteScope] = findValues

  final case object Local extends RouteScope

  final case object Regional extends RouteScope

  final case object National extends RouteScope

  final case object International extends RouteScope

  final case object Unknown extends RouteScope
}
