package kpn.server.monitor.group

import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorGroupsPageGroup
import kpn.server.config.RequestContext
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorGroupRouteInfo
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.repository.MonitorUserRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupsPageBuilder(
  monitorUserRepository: MonitorUserRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(): Option[MonitorGroupsPage] = {
    val admin = monitorUserRepository.isAdminUser(RequestContext.user)
    val groups = monitorGroupRepository.groups().sortBy(_.name)
    val groupRouteInfoMap = buildGroupRouteInfoMap()
    val routeCount = calculateTotalRouteCount(groupRouteInfoMap)
    val pageGroups = groups.map(g => toPageGroup(groupRouteInfoMap, g))

    Some(
      MonitorGroupsPage(
        admin,
        routeCount,
        pageGroups
      )
    )
  }

  private def buildGroupRouteInfoMap(): Map[String, MonitorGroupRouteInfo] = {
    monitorRouteRepository.groupRouteInfos().map(i => (i.groupId -> i)).toMap
  }

  private def calculateTotalRouteCount(groupRouteInfoMap: Map[String, MonitorGroupRouteInfo]) = {
    groupRouteInfoMap.values.map(_.monitorRouteIds.length).sum
  }

  private def toPageGroup(groupRouteInfoMap: Map[String, MonitorGroupRouteInfo], group: MonitorGroup): MonitorGroupsPageGroup = {
    val groupId = group._id.oid
    val routeInfo = groupRouteInfoMap.get(groupId)
    MonitorGroupsPageGroup(
      group._id.oid,
      group.name,
      group.description,
      routeInfo.map(_.monitorRouteIds.length.toLong).getOrElse(0L),
      routeInfo.map(_.monitorRouteIds).getOrElse(Seq.empty),
      routeInfo.flatMap(_.bounds),
    )
  }
}
