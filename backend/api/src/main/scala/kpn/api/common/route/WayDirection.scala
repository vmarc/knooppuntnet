package kpn.api.common.route

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait WayDirection extends EnumEntry with Hyphencase

object WayDirection extends Enum[WayDirection] {

  val values: IndexedSeq[WayDirection] = findValues

  case object Both extends WayDirection // the way can be traveled in both directions

  case object Forward extends WayDirection // the way can only be traveled in the forward direction

  case object Backward extends WayDirection // the way can only be traveled in the backward direction
}
