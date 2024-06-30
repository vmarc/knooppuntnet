package kpn.server.analyzer.engine.analysis.route.structure

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait RoutePathDirection extends EnumEntry

object RoutePathDirection extends Enum[RoutePathDirection] {

  val values: IndexedSeq[RoutePathDirection] = findValues

  case object Forward extends RoutePathDirection

  case object Backward extends RoutePathDirection

  case object Bidirectional extends RoutePathDirection
}
