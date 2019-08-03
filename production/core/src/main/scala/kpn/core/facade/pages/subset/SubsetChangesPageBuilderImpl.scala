package kpn.core.facade.pages.subset

import kpn.core.db.couch.Couch
import kpn.core.facade.pages.ChangeSetSummaryInfosBuilder
import kpn.core.facade.pages.ChangeSetSummarySubsetFilter
import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetRepository
import kpn.core.repository.OverviewRepository
import kpn.shared.ChangeSetSummary
import kpn.shared.changes.filter.ChangesParameters
import kpn.shared.subset.SubsetChangesPage

class SubsetChangesPageBuilderImpl(
  overviewRepository: OverviewRepository,
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends SubsetChangesPageBuilder {

  override def build(user: Option[String], parameters: ChangesParameters): Option[SubsetChangesPage] = {
    parameters.subset.map { subset =>
      val figures = overviewRepository.figures(Couch.uiTimeout)
      val subsetInfo = SubsetInfoBuilder.newSubsetInfo(subset, figures)
      val filter = changeSetRepository.changesFilter(parameters.subset, parameters.year, parameters.month, parameters.day)
      val totalCount = filter.currentItemCount(parameters.impact)
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
      SubsetChangesPage(subsetInfo, filter, changeSetSummaryInfos, totalCount)
    }
  }
}
