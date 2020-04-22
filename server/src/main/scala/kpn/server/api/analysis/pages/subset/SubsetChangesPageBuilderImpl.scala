package kpn.server.api.analysis.pages.subset

import kpn.api.common.ChangeSetSummary
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.subset.SubsetChangesPage
import kpn.core.db.couch.Couch
import kpn.server.api.analysis.pages.ChangeSetSummaryInfosBuilder
import kpn.server.api.analysis.pages.ChangeSetSummarySubsetFilter
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.OverviewRepository
import org.springframework.stereotype.Component

@Component
class SubsetChangesPageBuilderImpl(
  overviewRepository: OverviewRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends SubsetChangesPageBuilder {

  override def build(user: Option[String], parameters: ChangesParameters): Option[SubsetChangesPage] = {
    parameters.subset.map { subset =>
      val figures = overviewRepository.figures()
      val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
      val filter = changeSetRepository.changesFilter(parameters.subset, parameters.year, parameters.month, parameters.day)
      val changeCount = filter.currentItemCount(parameters.impact)
      val changeSetSummaries: Seq[ChangeSetSummary] = if (user.isDefined) {
        changeSetRepository.changes(parameters)
      }
      else {
        Seq()
      }
      val changeSetSummariesWithSubsetRelatedChangesOnly = changeSetSummaries.map { changeSetSummary =>
        ChangeSetSummarySubsetFilter.filter(changeSetSummary, subset)
      }
      val changeSetSummaryInfos = new ChangeSetSummaryInfosBuilder(changeSetInfoRepository).toChangeSetSummaryInfos(changeSetSummariesWithSubsetRelatedChangesOnly)
      SubsetChangesPage(subsetInfo, filter, changeSetSummaryInfos, changeCount)
    }
  }
}
