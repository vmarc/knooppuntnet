package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.WayMember

object WayMemberLink {

  def from(wayMembers: Seq[WayMember]): Seq[WayMemberLink] = {
    var next: Option[WayMemberLink] = None
    val links = wayMembers.reverse.map { wayMember =>
      val link = WayMemberLink(wayMember, next)
      next = Some(link)
      link
    }
    links.reverse
  }
}

case class WayMemberLink(
  wayMember: WayMember,
  next: Option[WayMemberLink]
)
