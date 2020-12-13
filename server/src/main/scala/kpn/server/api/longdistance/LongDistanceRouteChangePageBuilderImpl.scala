package kpn.server.api.longdistance

import kpn.api.common.longdistance.LongDistanceRouteChangePage
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.LongDistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongDistanceRouteChangePageBuilderImpl(
  longDistanceRouteRepository: LongDistanceRouteRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends LongDistanceRouteChangePageBuilder {

  override def build(routeId: Long, changeId: Long): Option[LongDistanceRouteChangePage] = {

    val comment = changeSetInfoRepository.get(changeId).flatMap(_.tags("comment"))

    longDistanceRouteRepository.change(routeId, changeId).flatMap { change =>
      val changeWithComment = change.copy(comment = comment)
      longDistanceRouteRepository.routeWithId(routeId).map { route =>
        LongDistanceRouteChangePage(
          route.id,
          route.ref,
          route.name,
          changeWithComment
        )
      }
    }
  }

}
