package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember

object MonitorRouteWayFilter {

  private val ignoredRoles = Seq("place_of_worship", "guest_house", "outer", "inner")

  def filter(wayMembers: Seq[WayMember]): Seq[WayMember] = {
    filterBuildings(filterIgnoredRoles(wayMembers))
  }

  private def filterIgnoredRoles(wayMembers: Seq[WayMember]): Seq[WayMember] = {
    wayMembers.filter { member =>
      member.role match {
        case Some(role) => !ignoredRoles.contains(role)
        case None => true
      }
    }
  }

  private def filterBuildings(wayMembers: Seq[WayMember]): Seq[WayMember] = {
    wayMembers.filter { member =>
      !member.way.tags.has("building")
    }
  }

}
