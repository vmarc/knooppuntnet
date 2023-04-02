package kpn.server.api.analysis.pages.route

import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.route.RouteChangesPage
import kpn.server.analyzer.engine.changes.builder.RouteChangeInfoBuilder
import kpn.server.config.RequestContext
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteChangesPageBuilderImpl(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends RouteChangesPageBuilder {

  override def build(routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {
    if (routeId == 1) {
      Some(RouteChangesPageExample.page)
    }
    else {
      buildPage(routeId, parameters)
    }
  }

  private def buildPage(routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {

    routeRepository.nameInfo(routeId).map { routeNameInfo =>
      val filterOptions = changeSetRepository.routeChangesFilter(routeId, parameters.year, parameters.month, parameters.day)
      val totalCount = ChangesFilterOption.changesCount(filterOptions, parameters)
      val changeCount = if (filterOptions.isEmpty) 0 else filterOptions.head.totalCount

      val routeChanges: Seq[RouteChange] = if (RequestContext.isLoggedIn) {
        changeSetRepository.routeChanges(routeId, parameters)
      }
      else {
        Seq.empty
      }

      val changeSetInfos = {
        val changeSetIds = routeChanges.map(_.key.changeSetId)
        changeSetInfoRepository.all(changeSetIds)
      }

      val changes = routeChanges.zipWithIndex.map { case (routeChange, index) =>
        val rowIndex = parameters.pageSize * parameters.pageIndex + index
        new RouteChangeInfoBuilder().build(rowIndex, routeChange, changeSetInfos)
      }

      RouteChangesPage(
        routeNameInfo,
        filterOptions,
        changes,
        totalCount,
        changeCount
      )
    }
  }
}
