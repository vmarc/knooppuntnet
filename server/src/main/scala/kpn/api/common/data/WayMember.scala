package kpn.api.common.data

case class WayMember(way: Way, role: Option[String]) extends Member {
  override def isWay: Boolean = true

  def hasRoleForward: Boolean = role.contains("forward")

  def hasRoleBackward: Boolean = role.contains("backward")

}
