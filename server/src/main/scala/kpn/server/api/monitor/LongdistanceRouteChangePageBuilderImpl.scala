package kpn.server.api.monitor

import kpn.api.common.monitor.LongdistanceRouteChangePage
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.LongdistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongdistanceRouteChangePageBuilderImpl(
  longdistanceRouteRepository: LongdistanceRouteRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends LongdistanceRouteChangePageBuilder {

  override def build(routeId: Long, changeId: Long): Option[LongdistanceRouteChangePage] = {

    val comment = changeSetInfoRepository.get(changeId).flatMap(_.tags("comment"))

    longdistanceRouteRepository.change(routeId, changeId).flatMap { change =>
      val changeWithComment = change.copy(comment = comment)
      longdistanceRouteRepository.routeWithId(routeId).map { route =>
        LongdistanceRouteChangePage(
          route.id,
          route.ref,
          route.name,
          changeWithComment
        )
      }
    }
  }

}
