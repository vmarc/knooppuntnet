package kpn.server.api.monitor.route

import kpn.api.common.BoundsI
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
    Some(MonitorChangesPage(changes))
  }

  override def groupChanges(groupName: String, parameters: MonitorChangesParameters): Option[MonitorGroupChangesPage] = {
    val changes = build(monitorRouteRepository.groupChanges(groupName, parameters))
    monitorGroupRepository.group(groupName).map { group =>
      MonitorGroupChangesPage(
        group.name,
        group.description,
        changes
      )
    }
  }

  override def routeChanges(routeId: Long, parameters: MonitorChangesParameters): Option[MonitorRouteChangesPage] = {
    val changes = build(monitorRouteRepository.routeChanges(routeId, parameters))
    monitorRouteRepository.route(routeId).flatMap { route =>
      monitorGroupRepository.group(route.groupName).map { group =>
        MonitorRouteChangesPage(
          route.id,
          route.name,
          group.name,
          group.description,
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
        change.key.cleaned,
        change.groupName,
        comment,
        change.wayCount,
        change.waysAdded,
        change.waysRemoved,
        change.waysUpdated,
        change.osmDistance,
        change.reference.map(_.distance).getOrElse(0),
        change.reference.flatMap(_.filename).getOrElse(""),
        change.reference.map(_.bounds).getOrElse(BoundsI()),
        change.routeSegmentCount,
        change.newNokSegmentCount,
        change.resolvedNokSegmentCount,
        change.happy,
        change.investigate
      )
    }
  }
}
