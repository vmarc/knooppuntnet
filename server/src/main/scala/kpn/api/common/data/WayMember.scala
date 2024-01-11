package kpn.api.common.data

case class WayMember(way: Way, role: Option[String]) extends Member {
  override def isWay: Boolean = true

  def hasRoleForward: Boolean = role.contains("forward")

  def hasRoleBackward: Boolean = role.contains("backward")

  def isUnidirectional: Boolean = hasRoleForward || hasRoleBackward

  def startNode: Node = {
    if (hasRoleBackward) {
      way.nodes.last
    }
    else {
      way.nodes.head
    }
  }

  def endNode: Node = {
    if (hasRoleBackward) {
      way.nodes.head
    }
    else {
      way.nodes.last
    }
  }
}
