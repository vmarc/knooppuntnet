package kpn.core.josm

import kpn.shared.data.Element
import kpn.shared.data.Node
import kpn.shared.data.Way

case class RelationMember(role: String, element: Element) {

  def isWay: Boolean = element.isWay

  def isNode: Boolean = element.isNode

  def way: Way = element.asInstanceOf[Way]

  def node: Node = element.asInstanceOf[Node]

  def isRoundabout: Boolean = isWay && way.tags.has("junction", "roundabout")

  def isOneWay: Boolean = isBackward || isForward

  def isBackward: Boolean = "backward".equals(role)

  def isForward: Boolean = "forward".equals(role)

}
