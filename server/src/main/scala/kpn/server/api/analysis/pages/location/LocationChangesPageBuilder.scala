package kpn.server.api.analysis.pages.location

import kpn.api.common.Country
import kpn.api.common.Language
import kpn.api.common.LocationChangeSetInfo
import kpn.api.common.LocationChangesInfo
import kpn.api.common.RouteType
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.location.LocationChangesPage
import kpn.api.custom.LocationKey
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.LocationRepository
import org.springframework.stereotype.Component

@Component
class LocationChangesPageBuilder(
  locationRepository: LocationRepository,
  locationService: LocationService,
  changeSetInfoRepository: ChangeSetInfoRepository
) {
  def build(language: Language, locationKey: LocationKey, parameters: ChangesParameters): Option[LocationChangesPage] = {
    if (locationKey == LocationKey(RouteType.cycling, Country.nl, "example")) {
      Some(LocationChangesPageExample.page)
    }
    else {
      buildPage(language, locationKey, parameters)
    }
  }

  private def buildPage(language: Language, locationKeyParam: LocationKey, parameters: ChangesParameters): Option[LocationChangesPage] = {
    val subset = locationService.toSubset(language, locationKeyParam)
    val summary = locationRepository.summary(subset)
    val filterOptions = locationRepository.changesFilter(subset, parameters)
    val changeSets = locationRepository.changes(subset, parameters)
    val changesCount = locationRepository.changesCount(subset, parameters)
    val changeSetIds = changeSets.map(_.key.changeSetId)
    val changeSetInfos = changeSetInfoRepository.all(changeSetIds)
    val locationChangeSetInfos = changeSets.zipWithIndex.map { case (changeSet, index) =>
      val rowIndex = parameters.pageSize * parameters.pageIndex + index
      val comment = changeSetInfos.find(s => s.id == changeSet.key.changeSetId).flatMap(_.tagValue("comment"))
      val locationChangeInfos = changeSet.locationChanges.map { change =>
        val locationNames = change.locationNames.dropWhile(_ != subset.locationIds.head /* TODO supports multiple locationIds !!! */).drop(1)
        val locationInfos = locationService.toInfos(language, change.locationNames, locationNames).map { locationInfo =>
          locationInfo.copy(link = s"${subset.routeType.entryName}/${locationInfo.link}")
        }
        LocationChangesInfo(
          change.routeType,
          locationInfos,
          change.routeChanges,
          change.nodeChanges,
          change.happy,
          change.investigate
        )
      }
      val happy = changeSet.locationChanges.forall(_.happy)
      val investigate = changeSet.locationChanges.exists(_.investigate)
      LocationChangeSetInfo(
        rowIndex,
        changeSet.key,
        comment,
        happy,
        investigate,
        locationChangeInfos
      )
    }
    Some(
      LocationChangesPage(
        summary,
        locationChangeSetInfos,
        changesCount,
        filterOptions
      )
    )
  }
}
