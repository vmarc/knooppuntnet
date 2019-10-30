package kpn.server.api.analysis.pages.route

import kpn.core.db.couch.Couch
import kpn.server.analyzer.engine.analysis.RouteHistoryAnalyzer
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import kpn.shared.changes.details.RouteChange
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.route.RouteChangesPage
import kpn.shared.route.RouteDetailsPage
import kpn.shared.route.RouteMapPage
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
      routeRepository.routeWithId(routeId, Couch.uiTimeout).map { route =>
        val changeCount = changeSetRepository.routeChangesCount(route.id)
        val routeReferences = routeRepository.routeReferences(routeId, Couch.uiTimeout)
        RouteDetailsPage(route, routeReferences, changeCount)
      }
    }
  }

  def buildMapPage(user: Option[String], routeId: Long): Option[RouteMapPage] = {
    if (routeId == 1) {
      Some(RouteMapPageExample.page)
    }
    else {
      routeRepository.routeWithId(routeId, Couch.uiTimeout).map { route =>
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
      routeRepository.routeWithId(routeId, Couch.uiTimeout).map { route =>
        val changeCount = changeSetRepository.routeChangesCount(route.id)
        val changesFilter = changeSetRepository.routeChangesFilter(routeId, parameters.year, parameters.month, parameters.day)
        val totalCount = changesFilter.currentItemCount(parameters.impact)
        val routeChanges: Seq[RouteChange] = if (user.isDefined) {
          changeSetRepository.routeChanges(parameters)
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
