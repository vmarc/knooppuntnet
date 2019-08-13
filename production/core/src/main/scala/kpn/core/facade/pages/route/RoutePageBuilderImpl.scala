package kpn.core.facade.pages.route

import kpn.core.db.couch.Couch
import kpn.core.engine.analysis.RouteHistoryAnalyzer
import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetRepository
import kpn.core.repository.RouteRepository
import kpn.shared.changes.details.RouteChange
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.route.RouteChangesPage
import kpn.shared.route.RouteDetailsPage
import kpn.shared.route.RouteMapPage
import kpn.shared.route.RoutePage

class RoutePageBuilderImpl(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends RoutePageBuilder {

  /* supports scalajs-react version only */
  def build(user: Option[String], routeId: Long): Option[RoutePage] = {
    if (routeId == 1) {
      Some(RoutePageExample.page)
    }
    else {
      routeRepository.routeWithId(routeId, Couch.uiTimeout).map { route =>
        val routeReferences = routeRepository.routeReferences(routeId, Couch.uiTimeout)
        val routeChanges: Seq[RouteChange] = if (user.isDefined) {
          changeSetRepository.routeChanges(ChangesParameters(routeId = Some(route.id), itemsPerPage = 5))
        }
        else {
          Seq()
        }
        val changeSetInfos = {
          val changeSetIds = routeChanges.map(_.key.changeSetId)
          changeSetInfoRepository.all(changeSetIds)
        }
        val history = new RouteHistoryAnalyzer(routeChanges, changeSetInfos).history
        RoutePage(route, routeReferences, history)
      }
    }
  }

  def buildDetailsPage(user: Option[String], routeId: Long): Option[RouteDetailsPage] = {
    if (routeId == 1) {
      Some(RoutePageExample.detailsPage)
    }
    else {
      routeRepository.routeWithId(routeId, Couch.uiTimeout).map { route =>
        val routeReferences = routeRepository.routeReferences(routeId, Couch.uiTimeout)
        RouteDetailsPage(route, routeReferences)
      }
    }
  }

  def buildMapPage(user: Option[String], routeId: Long): Option[RouteMapPage] = {
    if (routeId == 1) {
      Some(RoutePageExample.mapPage)
    }
    else {
      routeRepository.routeWithId(routeId, Couch.uiTimeout).map { route =>
        RouteMapPage(route)
      }
    }
  }

  def buildChangesPage(user: Option[String], routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {
    if (routeId == 1) {
      Some(RoutePageExample.changesPage)
    }
    else {
      routeRepository.routeWithId(routeId, Couch.uiTimeout).map { route =>
        val changesFilter = changeSetRepository.nodeChangesFilter(routeId, parameters.year, parameters.month, parameters.day)
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
        RouteChangesPage(route, changesFilter, history.changes, history.incompleteWarning, totalCount)
      }
    }
  }

}
