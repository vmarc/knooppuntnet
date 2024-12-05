package kpn.server.analyzer.engine.analysis.route.domain

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait RoutePathDirection extends EnumEntry

object RoutePathDirection extends Enum[RoutePathDirection] {

  val values: IndexedSeq[RoutePathDirection] = IndexedSeq(
    Forward,
    Backward,
    Bidirectional,
  )

  case object Forward extends RoutePathDirection

  case object Backward extends RoutePathDirection

  case object Bidirectional extends RoutePathDirection
}
