package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait RouteScope extends EnumEntry

object RouteScope extends Enum[RouteScope] {

  val all: Seq[RouteScope] = Seq(local, regional, national, international)

  val values: IndexedSeq[RouteScope] = findValues

  case object local extends RouteScope

  case object regional extends RouteScope

  case object national extends RouteScope

  case object international extends RouteScope

  case object unknown extends RouteScope
}
