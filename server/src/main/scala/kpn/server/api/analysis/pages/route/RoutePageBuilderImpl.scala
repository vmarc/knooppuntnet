package kpn.server.api.analysis.pages.route

import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.route.RouteChangesPage
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteMapPage
import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.analysis.route.RouteHistoryAnalyzer
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RoutePageBuilderImpl(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends RoutePageBuilder {

  def buildDetailsPage(user: Option[String], routeId: Long): Option[RouteDetailsPage] = {
    if (routeId == 1) {
      Some(RouteDetailsPageExample.page)
    }
    else {
      routeRepository.routeWithId(routeId).map { route =>
        val changeCount = changeSetRepository.routeChangesCount(route.id)
        val routeReferences = routeRepository.routeReferences(routeId)
        RouteDetailsPage(route, routeReferences, changeCount)
      }
    }
  }

  def buildMapPage(user: Option[String], routeId: Long): Option[RouteMapPage] = {
    if (routeId == 1) {
      Some(RouteMapPageExample.page)
    }
    else {
      routeRepository.routeWithId(routeId).map { route =>
        val changeCount = changeSetRepository.routeChangesCount(route.id)
        RouteMapPage(route, changeCount)
      }
    }
  }

  def buildChangesPage(user: Option[String], routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {
    if (routeId == 1) {
      Some(RouteChangesPageExample.page)
    }
    else {
      routeRepository.routeWithId(routeId).map { route =>
        val changeCount = changeSetRepository.routeChangesCount(route.id)
        val changesFilter = changeSetRepository.routeChangesFilter(routeId, parameters.year, parameters.month, parameters.day)
        val totalCount = changesFilter.currentItemCount(parameters.impact)
        val routeChanges: Seq[RouteChange] = if (user.isDefined) {
          changeSetRepository.routeChanges(route.id, parameters)
        }
        else {
          Seq()
        }
        val changeSetInfos = {
          val changeSetIds = routeChanges.map(_.key.changeSetId)
          changeSetInfoRepository.all(changeSetIds)
        }
        val history = new RouteHistoryAnalyzer(routeChanges, changeSetInfos).history
        RouteChangesPage(route, changesFilter, history.changes, history.incompleteWarning, totalCount, changeCount)
      }
    }
  }
}
