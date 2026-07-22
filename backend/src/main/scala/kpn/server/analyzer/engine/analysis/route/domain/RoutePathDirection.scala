package kpn.server.analyzer.engine.analysis.route.domain

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait RoutePathDirection extends EnumEntry with Hyphencase

object RoutePathDirection extends Enum[RoutePathDirection] {

  val values: IndexedSeq[RoutePathDirection] = findValues

  case object Forward extends RoutePathDirection

  case object Backward extends RoutePathDirection

  case object Bidirectional extends RoutePathDirection
}
