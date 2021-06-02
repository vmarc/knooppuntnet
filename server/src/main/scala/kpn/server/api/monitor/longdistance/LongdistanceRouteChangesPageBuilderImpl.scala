package kpn.server.api.monitor.longdistance

import kpn.api.common.monitor.LongdistanceRouteChangeSummary
import kpn.api.common.monitor.LongdistanceRouteChangesPage
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.LongdistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongdistanceRouteChangesPageBuilderImpl(
  longdistanceRouteRepository: LongdistanceRouteRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends LongdistanceRouteChangesPageBuilder {

  override def build(routeId: Long): Option[LongdistanceRouteChangesPage] = {

    val changes = if (routeId == 3121667L) {
      longdistanceRouteRepository.changes().map { change =>
        val comment: Option[String] = changeSetInfoRepository.get(change.key.changeSetId).flatMap { changeSetInfo =>
          changeSetInfo.tags("comment")
        }
        LongdistanceRouteChangeSummary(
          change.key,
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

    longdistanceRouteRepository.routeWithId(routeId).map { route =>
      LongdistanceRouteChangesPage(
        route.id,
        route.ref,
        route.name,
        changes
      )
    }
  }

}
