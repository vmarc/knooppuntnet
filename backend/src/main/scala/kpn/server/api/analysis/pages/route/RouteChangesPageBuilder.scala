package kpn.server.api.analysis.pages.route

import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.route.RouteChangesPage
import kpn.server.analyzer.engine.changes.builder.RouteChangeInfoBuilder
import kpn.server.config.RequestContext
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class RouteChangesPageBuilder(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) {

  def build(routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {
    if (routeId == 1) {
      Some(RouteChangesPageExample.page)
    }
    else {
      buildPage(routeId, parameters)
    }
  }

  private def buildPage(routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {

    routeRepository.routeInfo(routeId).map { routeNameInfo =>
      val filterOptions = changeSetRepository.routeChangesFilter(routeId, parameters.year, parameters.month, parameters.day)
      val totalCount = ChangesFilterOption.changesCount(filterOptions, parameters)
      val changeCount = if (filterOptions.isEmpty) 0 else filterOptions.head.totalCount

      val routeChanges: Seq[RouteChange] = if (RequestContext.isLoggedIn) {
        changeSetRepository.routeChanges(routeId, parameters)
      }
      else {
        Seq.empty
      }

      val baseRouteChanges: Seq[BaseRouteChange] = if (routeChanges.nonEmpty) {
        val ids = routeChanges.map(_._id)
        changeSetRepository.baseRouteChanges(ids)
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
        val baseRouteChangeOption = baseRouteChanges.find(_._id == routeChange._id)
        RouteChangeInfoBuilder.build(rowIndex, routeChange, baseRouteChangeOption, changeSetInfos)
      }

      RouteChangesPage(
        routeNameInfo.copy(changeCount = totalCount),
        filterOptions,
        changes,
      )
    }
  }
}
