package kpn.server.api.analysis.pages.route

import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.route.RouteChangesPage
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteMapInfo
import kpn.api.common.route.RouteMapPage
import kpn.api.common.route.RouteNameInfo
import kpn.server.analyzer.engine.analysis.route.RouteHistoryAnalyzer
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RoutePageBuilderImpl(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository,
  // old
  mongoEnabled: Boolean
) extends RoutePageBuilder {

  def buildDetailsPage(user: Option[String], routeId: Long): Option[RouteDetailsPage] = {
    if (routeId == 1) {
      Some(RouteDetailsPageExample.page)
    }
    else {
      if (mongoEnabled) {
        mongoBuildDetailsPage(routeId)
      }
      else {
        oldBuildDetailsPage(routeId)
      }
    }
  }

  def buildMapPage(user: Option[String], routeId: Long): Option[RouteMapPage] = {
    if (routeId == 1) {
      Some(RouteMapPageExample.page)
    }
    else {
      if (mongoEnabled) {
        mongoBuildMapPage(routeId)
      }
      else {
        oldBuildMapPage(routeId)
      }
    }
  }

  def buildChangesPage(user: Option[String], routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {
    if (routeId == 1) {
      Some(RouteChangesPageExample.page)
    }
    else {
      if (mongoEnabled) {
        mongoBuildChangesPage(user, routeId, parameters)
      }
      else {
        oldBuildChangesPage(user, routeId, parameters)
      }
    }
  }

  private def mongoBuildDetailsPage(routeId: Long): Option[RouteDetailsPage] = {
    routeRepository.routeWithId(routeId).map { route =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val networkReferences = routeRepository.networkReferences(routeId)
      RouteDetailsPage(route, networkReferences, changeCount)
    }
  }

  private def oldBuildDetailsPage(routeId: Long): Option[RouteDetailsPage] = {
    routeRepository.routeWithId(routeId).map { route =>
      val changeCount = changeSetRepository.routeChangesCount(route.id)
      val networkReferences = routeRepository.networkReferences(routeId)
      RouteDetailsPage(route, networkReferences, changeCount)
    }
  }

  private def mongoBuildMapPage(routeId: Long): Option[RouteMapPage] = {
    routeRepository.mapInfo(routeId).map { routeMapInfo =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      RouteMapPage(routeMapInfo, changeCount)
    }
  }

  private def oldBuildMapPage(routeId: Long): Option[RouteMapPage] = {
    routeRepository.routeWithId(routeId).map { route =>
      val changeCount = changeSetRepository.routeChangesCount(route.id)
      RouteMapPage(
        RouteMapInfo(
          routeId,
          route.summary.name,
          route.summary.networkType,
          route.analysis.map
        ),
        changeCount
      )
    }
  }

  private def mongoBuildChangesPage(user: Option[String], routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {
    routeRepository.nameInfo(routeId).map { routeNameInfo =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val changesFilter = changeSetRepository.routeChangesFilter(routeId, parameters.year, parameters.month, parameters.day)
      val totalCount = changesFilter.currentItemCount(parameters.impact)
      val routeChanges: Seq[RouteChange] = if (user.isDefined) {
        changeSetRepository.routeChanges(routeId, parameters)
      }
      else {
        Seq()
      }
      val changeSetInfos = {
        val changeSetIds = routeChanges.map(_.key.changeSetId)
        changeSetInfoRepository.all(changeSetIds) // TODO include in aggregate !!!
      }
      val history = new RouteHistoryAnalyzer(routeChanges, changeSetInfos).history
      RouteChangesPage(
        routeNameInfo,
        changesFilter,
        history.changes,
        history.incompleteWarning,
        totalCount,
        changeCount
      )
    }
  }

  private def oldBuildChangesPage(user: Option[String], routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {
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
      RouteChangesPage(
        RouteNameInfo(
          route._id,
          route.summary.name,
          route.summary.networkType
        ),
        changesFilter,
        history.changes,
        history.incompleteWarning,
        totalCount,
        changeCount
      )
    }
  }
}
