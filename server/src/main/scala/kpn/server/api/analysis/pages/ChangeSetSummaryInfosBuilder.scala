package kpn.server.api.analysis.pages

import kpn.api.common.AnalysisMode
import kpn.api.common.ChangeSetSummary
import kpn.api.common.ChangeSetSummaryInfo
import kpn.api.common.ChangeSetSummaryLocationInfo
import kpn.api.common.ChangeSetSummaryNetworkInfo
import kpn.api.common.LOCATION
import kpn.api.common.Language
import kpn.api.common.NETWORK
import kpn.api.common.changes.filter.ChangesParameters
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.ChangeSetInfoRepository
import org.springframework.stereotype.Component

@Component
class ChangeSetSummaryInfosBuilder(
  changeSetInfoRepository: ChangeSetInfoRepository,
  locationService: LocationService
) {

  def toChangeSetSummaryInfos(
    language: Language,
    analysisMode: AnalysisMode,
    parameters: ChangesParameters,
    changeSetSummaries: Seq[ChangeSetSummary]
  ): Seq[ChangeSetSummaryInfo] = {
    val changeSetIds = changeSetSummaries.map(_.key.changeSetId)
    val changeSetInfos = changeSetInfoRepository.all(changeSetIds)
    changeSetSummaries.zipWithIndex.map { case (summary, index) =>
      val rowIndex = parameters.itemsPerPage * parameters.pageIndex + index
      val comment = changeSetInfos.find(s => s.id == summary.key.changeSetId).flatMap(_.tags("comment"))
      analysisMode match {
        case LOCATION =>

          val changes = summary.locationChanges.map { locationChanges =>
            locationChanges.copy(
              locationNames = locationChanges.locationNames.map(locationName => locationService.name(language, locationName))
            )
          }

          ChangeSetSummaryInfo(
            rowIndex = rowIndex,
            key = summary.key,
            comment = comment,
            subsets = summary.subsets,
            network = None,
            location = Some(ChangeSetSummaryLocationInfo(changes)),
            happy = summary.happy,
            investigate = summary.investigate,
            impact = summary.impact,
          )

        case NETWORK =>

          ChangeSetSummaryInfo(
            rowIndex = rowIndex,
            key = summary.key,
            comment = comment,
            subsets = summary.subsets,
            network = Some(
              ChangeSetSummaryNetworkInfo(
                networkChanges = summary.networkChanges,
                orphanRouteChanges = summary.orphanRouteChanges,
                orphanNodeChanges = summary.orphanNodeChanges,
              )
            ),
            location = None,
            happy = summary.happy,
            investigate = summary.investigate,
            impact = summary.impact,
          )
      }
    }
  }
}
