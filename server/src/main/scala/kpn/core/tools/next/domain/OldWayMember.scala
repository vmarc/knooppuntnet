package kpn.core.tools.next.domain

case class OldWayMember(way: OldWay, role: Option[String]) extends OldMember {
  override def isWay: Boolean = true
}
