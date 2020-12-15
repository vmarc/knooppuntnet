package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteChangeSummary
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteChangesPageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends MonitorRouteChangesPageBuilder {

  override def build(routeId: Long): Option[MonitorRouteChangesPage] = {

    val changes = if (routeId == 3121667L) {
      monitorRouteRepository.changes().map { change =>
        val comment: Option[String] = changeSetInfoRepository.get(change.key.changeSetId).flatMap { changeSetInfo =>
          changeSetInfo.tags("comment")
        }
        MonitorRouteChangeSummary(
          change.key.cleaned,
          comment,
          change.wayCount,
          change.waysAdded,
          change.waysRemoved,
          change.waysUpdated,
          change.osmDistance,
          change.gpxDistance,
          change.gpxFilename,
          change.bounds,
          change.routeSegmentCount,
          change.newNokSegments.size,
          change.resolvedNokSegments.size,
          change.happy,
          change.investigate
        )
      }.reverse
    }
    else {
      Seq()
    }

    monitorRouteRepository.routeWithId(routeId).map { route =>
      MonitorRouteChangesPage(
        route.id,
        route.ref,
        route.name,
        changes
      )
    }
  }

}
