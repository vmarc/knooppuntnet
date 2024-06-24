package kpn.core.analysis

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait LinkDirection extends EnumEntry

object LinkDirection extends Enum[LinkDirection] {

  val values: IndexedSeq[LinkDirection] = findValues

  /*
     The link direction is "Forward" if the first node of this way is connected to the previous way
     and/or the last node of this way is connected to the next way.
   */
  case object Forward extends LinkDirection

  /*
     The link direction is "Backward" if the first node of this way is connected to the next way
     and/or the last node of this way is connected to the previous way.
   */
  case object Backward extends LinkDirection

  /*
    The direction has value "RoundaboutLeft", if the way is tagged as such and it is somehow
    connected to the previous/next member.
   */
  case object RoundaboutLeft extends LinkDirection // tagged as roundabout and connected to the previous/next member

  /*
    The direction has value "RoundaboutRight", if the way is tagged as such and it is somehow
    connected to the previous/next member.
   */
  case object RoundaboutRight extends LinkDirection

  /*
    If there is no connection to the previous or next member, then direction has the value "Unconnected".
   */
  case object Unconnected extends LinkDirection
}
