package kpn.server.api.monitor.route

import kpn.api.common.EN
import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorRouteChangeSummary
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.server.api.monitor.domain.MonitorRouteChange
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteChangesPageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository,
  monitorGroupRepository: MonitorGroupRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends MonitorRouteChangesPageBuilder {

  override def changes(parameters: MonitorChangesParameters): Option[MonitorChangesPage] = {
    val changes = build(monitorRouteRepository.changes(parameters))
    val totalChangeCount = monitorRouteRepository.changesCount(parameters)
    val groupMap = monitorGroupRepository.groups().map(group => group.name -> group.description).toMap
    val routeMap = monitorRouteRepository.routes().map(route => route.routeId -> route.name).toMap
    val enrichedChanges = changes.map { change =>
      change.copy(
        groupDescription = groupMap.get(change.groupName),
        routeName = routeMap.get(change.key.elementId)
      )
    }
    Some(
      MonitorChangesPage(
        parameters.impact,
        parameters.pageSize,
        parameters.pageIndex,
        totalChangeCount,
        enrichedChanges
      )
    )
  }

  override def groupChanges(groupName: String, parameters: MonitorChangesParameters): Option[MonitorGroupChangesPage] = {
    val changes = build(monitorRouteRepository.groupChanges(groupName, parameters))
    val totalChangeCount = monitorRouteRepository.groupChangesCount(groupName, parameters)
    val routeMap = monitorRouteRepository.routes().map(route => route.routeId -> route.name).toMap
    val enrichedChanges = changes.map { change =>
      change.copy(
        routeName = routeMap.get(change.key.elementId)
      )
    }
    monitorGroupRepository.group(groupName).map { group =>
      MonitorGroupChangesPage(
        group.name,
        group.description,
        parameters.impact,
        parameters.pageSize,
        parameters.pageIndex,
        totalChangeCount,
        enrichedChanges
      )
    }
  }

  override def routeChanges(monitorRouteId: String, parameters: MonitorChangesParameters): Option[MonitorRouteChangesPage] = {
    val changes = build(monitorRouteRepository.routeChanges(monitorRouteId, parameters))
    val totalChangeCount = monitorRouteRepository.routeChangesCount(monitorRouteId, parameters)
    monitorRouteRepository.route(monitorRouteId).flatMap { route =>
      monitorGroupRepository.group(route.groupName).map { group =>
        MonitorRouteChangesPage(
          route.routeId,
          route.name,
          group.name,
          group.description,
          parameters.impact,
          parameters.pageSize,
          parameters.pageIndex,
          totalChangeCount,
          changes
        )
      }
    }
  }

  private def build(changes: Seq[MonitorRouteChange]): Seq[MonitorRouteChangeSummary] = {

    changes.map { change =>

      val comment: Option[String] = changeSetInfoRepository.get(change.key.changeSetId).flatMap { changeSetInfo =>
        changeSetInfo.tags("comment")
      }

      MonitorRouteChangeSummary(
        change.key,
        change.groupName,
        None,
        None,
        comment,
        change.wayCount,
        change.waysAdded,
        change.waysRemoved,
        change.waysUpdated,
        change.osmDistance,
        change.routeSegmentCount,
        change.newNokSegmentCount,
        change.resolvedNokSegmentCount,
        change.happy,
        change.investigate
      )
    }
  }
}
