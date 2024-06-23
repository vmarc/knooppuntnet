package kpn.server.analyzer.engine.analysis.route.structure

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait ReferenceDirection extends EnumEntry

object ReferenceDirection extends Enum[ReferenceDirection] {

  val values: IndexedSeq[ReferenceDirection] = findValues

  case object Forward extends ReferenceDirection // the first node of this way is connected to the previous way and/or the last node of this way is connected to the next way

  case object Backward extends ReferenceDirection

  case object RoundaboutLeft extends ReferenceDirection // tagged as roundabout and connected to the previous/next member

  case object RoundaboutRight extends ReferenceDirection

  case object Unknown extends ReferenceDirection
}

