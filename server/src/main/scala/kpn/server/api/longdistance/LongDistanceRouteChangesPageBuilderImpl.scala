package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteChangeSummary
import kpn.api.common.longdistance.LongDistanceRouteChangesPage
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.LongDistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongDistanceRouteChangesPageBuilderImpl(
  longDistanceRouteRepository: LongDistanceRouteRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends LongDistanceRouteChangesPageBuilder {

  override def build(routeId: Long): Option[LongDistanceRouteChangesPage] = {

    val changes = if (routeId == 3121667L) {
      longDistanceRouteRepository.changes().map { change =>
        val comment: Option[String] = changeSetInfoRepository.get(change.key.changeSetId).flatMap { changeSetInfo =>
          changeSetInfo.tags("comment")
        }
        LongDistanceRouteChangeSummary(
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

    longDistanceRouteRepository.routeWithId(routeId).map { route =>
      LongDistanceRouteChangesPage(
        route.id,
        route.ref,
        route.name,
        changes
      )
    }
  }

}
