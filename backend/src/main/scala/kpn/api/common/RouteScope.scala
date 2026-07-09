package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait RouteScope extends EnumEntry

object RouteScope extends Enum[RouteScope] {

  val all: Seq[RouteScope] = Seq(local, regional, national, international)

  val values: IndexedSeq[RouteScope] = findValues

  final case object local extends RouteScope

  final case object regional extends RouteScope

  final case object national extends RouteScope

  final case object international extends RouteScope

  final case object unknown extends RouteScope
}
