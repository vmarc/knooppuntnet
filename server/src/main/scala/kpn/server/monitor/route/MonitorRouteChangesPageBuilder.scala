package kpn.server.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteChangeSummary
import kpn.server.monitor.domain.MonitorRouteChange
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.ChangeSetInfoRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteChangesPageBuilder(
  monitorRouteRepository: MonitorRouteRepository,
  monitorGroupRepository: MonitorGroupRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) {

  def changes(parameters: MonitorChangesParameters): Option[MonitorChangesPage] = {
    val changes = build(monitorRouteRepository.changes(parameters))
    val totalChangeCount = monitorRouteRepository.changesCount(parameters)
    val groupMap = monitorGroupRepository.groups().map(group => group.name -> group.description).toMap
    val routeMap = monitorRouteRepository.routes().map(route => route.relationId -> route.name).toMap
    val enrichedChanges = changes.map { change =>
      change.copy(
        groupDescription = groupMap.get("TODO MON change.groupName"),
        routeName = Some("TODO MON routeName") //routeMap.get(change.key.elementId)
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

  def groupChanges(groupName: String, parameters: MonitorChangesParameters): Option[MonitorGroupChangesPage] = {
    val changes = build(monitorRouteRepository.groupChanges(groupName, parameters))
    val totalChangeCount = monitorRouteRepository.groupChangesCount(groupName, parameters)
    val routeMap = monitorRouteRepository.routes().map(route => route.relationId -> route.name).toMap
    val enrichedChanges = changes.map { change =>
      change.copy(
        routeName = Some("TODO MON routeName") //routeMap.get(change.key.elementId)
      )
    }
    monitorGroupRepository.groupByName(groupName).map { group =>
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

  def routeChanges(monitorRouteId: String, parameters: MonitorChangesParameters): Option[MonitorRouteChangesPage] = {
    val changes = build(monitorRouteRepository.routeChanges(monitorRouteId, parameters))
    val totalChangeCount = monitorRouteRepository.routeChangesCount(monitorRouteId, parameters)
    monitorRouteRepository.routeById(ObjectId("TODO") /*monitorRouteId*/).flatMap { route =>
      monitorGroupRepository.groupById(route.groupId).map { group =>
        MonitorRouteChangesPage(
          route._id.oid,
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
        None,
        None,
        None,
        comment,
        change.wayCount,
        change.waysAdded,
        change.waysRemoved,
        change.waysUpdated,
        change.osmDistance,
        change.routeSegmentCount,
        change.newDeviationCount,
        change.resolvedDeviationCount,
        change.happy,
        change.investigate
      )
    }
  }
}
