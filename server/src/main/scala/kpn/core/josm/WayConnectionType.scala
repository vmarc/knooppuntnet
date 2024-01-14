package kpn.core.josm

object OldDirection extends Enumeration {
  val FORWARD, BACKWARD, ROUNDABOUT, NONE = Value
}

object WayConnectionType {
  def apply(linkPrev: Boolean, linkNext: Boolean, direction: OldDirection.Value): WayConnectionType = {
    val wct = new WayConnectionType(false)
    wct.hasLinkPrev = linkPrev
    wct.hasLinkNext = linkNext
    wct.isLoop = false
    wct.direction = direction
    wct
  }
}

class WayConnectionType(val invalid: Boolean = true) {

  /* True, if linked to the previous / next member.  */
  var hasLinkPrev = false
  var hasLinkNext = false

  /*
   * direction is FORWARD if the first node of this way is connected to the previous way
   * and / or the last node of this way is connected to the next way.
   * direction is BACKWARD if it is the other way around.
   * direction has a ROUNDABOUT value, if it is tagged as such and it is somehow
   * connected to the previous / next member.
   * If there is no connection to the previous or next member, then
   * direction has the value NONE.
   */
  var direction: OldDirection.Value = OldDirection.NONE

  /* True, if the element is part of a closed loop of ways. */
  var isLoop = false

  var isOnewayLoopForwardPart = false
  var isOnewayLoopBackwardPart = false
  var isOnewayHead = false
  var isOnewayTail = false

  def isValid: Boolean = !invalid

}
