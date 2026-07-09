package kpn.api.common.route

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait WayDirection extends EnumEntry with Hyphencase

object WayDirection extends Enum[WayDirection] {

  val values: IndexedSeq[WayDirection] = findValues

  final case object Both extends WayDirection // the way can be travelled in both directions

  final case object Forward extends WayDirection // the way can only be travelled in the forward direction

  final case object Backward extends WayDirection // the way can only be travelled in the backward direction
}
