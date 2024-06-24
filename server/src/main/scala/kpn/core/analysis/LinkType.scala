package kpn.core.analysis

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait LinkType extends EnumEntry

object LinkType extends Enum[LinkType] {

  val values: IndexedSeq[LinkType] = findValues

  case object Forward extends LinkType // the first node of this way is connected to the previous way and/or the last node of this way is connected to the next way

  case object Backward extends LinkType

  case object RoundaboutLeft extends LinkType // tagged as roundabout and connected to the previous/next member

  case object RoundaboutRight extends LinkType

  case object All extends LinkType
}
