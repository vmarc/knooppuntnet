package kpn.api.common.data

import kpn.api.common.data.raw.RawMember

case class WayMember(way: Way, role: Option[String]) extends Member {
  override def isWay: Boolean = true

  def toRaw: RawMember = {
    RawMember(
      "way",
      way.id,
      role
    )
  }
}
