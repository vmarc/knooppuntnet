package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.route.RouteChangesPage
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteDetailsPageData
import kpn.api.common.route.RouteMapPage
import kpn.core.doc.Label
import kpn.server.analyzer.engine.analysis.location.LocationService
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
  locationService: LocationService
) extends RoutePageBuilder {

  def buildDetailsPage(user: Option[String], language: Language, routeId: Long): Option[RouteDetailsPage] = {
    if (routeId == 1) {
      Some(RouteDetailsPageExample.page)
    }
    else {
      doBuildDetailsPage(language, routeId)
    }
  }

  def buildMapPage(user: Option[String], routeId: Long): Option[RouteMapPage] = {
    if (routeId == 1) {
      Some(RouteMapPageExample.page)
    }
    else {
      doBuildMapPage(routeId)
    }
  }

  def buildChangesPage(user: Option[String], routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {
    if (routeId == 1) {
      Some(RouteChangesPageExample.page)
    }
    else {
      doBuildChangesPage(user, routeId, parameters)
    }
  }

  private def doBuildDetailsPage(language: Language, routeId: Long): Option[RouteDetailsPage] = {
    routeRepository.findById(routeId).map { route =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val networkReferences = routeRepository.networkReferences(routeId)

      val routeAnalysis = route.analysis.copy(
        locationAnalysis = locationService.replaceNames(language, route.analysis.locationAnalysis)
      )

      val data = RouteDetailsPageData(
        route._id,
        route.labels.contains(Label.active),
        route.summary,
        route.proposed,
        route.version,
        route.changeSetId,
        route.lastUpdated,
        route.lastSurvey,
        route.tags,
        route.facts,
        routeAnalysis,
        route.tiles,
        route.nodeRefs
      )
      RouteDetailsPage(data, networkReferences, changeCount)
    }
  }

  private def doBuildMapPage(routeId: Long): Option[RouteMapPage] = {
    routeRepository.mapInfo(routeId).map { routeMapInfo =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      RouteMapPage(routeMapInfo, changeCount)
    }
  }

  private def doBuildChangesPage(user: Option[String], routeId: Long, parameters: ChangesParameters): Option[RouteChangesPage] = {
    routeRepository.nameInfo(routeId).map { routeNameInfo =>
      val filterOptions = changeSetRepository.routeChangesFilter(routeId, parameters.year, parameters.month, parameters.day)
      val totalCount = ChangesFilterOption.changesCount(filterOptions, parameters)
      val changeCount = if (filterOptions.isEmpty) 0 else filterOptions.head.totalCount

      val routeChanges: Seq[RouteChange] = if (user.isDefined) {
        changeSetRepository.routeChanges(routeId, parameters)
      }
      else {
        Seq.empty
      }
      val changeSetInfos = {
        val changeSetIds = routeChanges.map(_.key.changeSetId)
        changeSetInfoRepository.all(changeSetIds) // TODO include in aggregate !!!
      }
      val history = new RouteHistoryAnalyzer(routeChanges, changeSetInfos).history
      RouteChangesPage(
        routeNameInfo,
        filterOptions,
        history.changes,
        totalCount,
        changeCount
      )
    }
  }
}
