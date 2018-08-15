package kpn.shared.route

/*
  Both = the way can be travelled in both directions
  Forward = the way can only be travelled in the forward direction
  Backward = the way can only be travelled in the backward direction
 */
sealed trait WayDirection

case object Both extends WayDirection
case object Forward extends WayDirection
case object Backward extends WayDirection
