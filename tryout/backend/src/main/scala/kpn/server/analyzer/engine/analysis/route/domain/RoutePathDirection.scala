package kpn.server.analyzer.engine.analysis.route.domain

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait RoutePathDirection extends EnumEntry with Hyphencase

object RoutePathDirection extends Enum[RoutePathDirection] {

  val values: IndexedSeq[RoutePathDirection] = findValues

  final case object Forward extends RoutePathDirection

  final case object Backward extends RoutePathDirection

  final case object Bidirectional extends RoutePathDirection
}
