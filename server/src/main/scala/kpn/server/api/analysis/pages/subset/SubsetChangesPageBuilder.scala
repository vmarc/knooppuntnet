package kpn.server.api.analysis.pages.subset

import kpn.api.common.ChangeSetSummary
import kpn.api.common.EN
import kpn.api.common.NETWORK
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.api.common.subset.SubsetChangesPage
import kpn.api.custom.Subset
import kpn.server.api.analysis.pages.ChangeSetSummaryInfosBuilder
import kpn.server.api.analysis.pages.ChangeSetSummarySubsetFilter
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.SubsetRepository
import org.springframework.stereotype.Component

@Component
class SubsetChangesPageBuilder(
  changeSetRepository: ChangeSetRepository,
  subsetRepository: SubsetRepository,
  changeSetSummaryInfosBuilder: ChangeSetSummaryInfosBuilder
) {

  def build(
    user: Option[String],
    subset: Subset,
    parameters: ChangesParameters
  ): Option[SubsetChangesPage] = {
    val subsetInfo = subsetRepository.subsetInfo(subset)
    val filterOptions = changeSetRepository.changesFilter(Some(subset), parameters.year, parameters.month, parameters.day)
    val changeCount = ChangesFilterOption.changesCount(filterOptions, parameters)
    val changeSetSummaries: Seq[ChangeSetSummary] = if (user.isDefined) {
      changeSetRepository.subsetChanges(subset, parameters)
    }
    else {
      Seq.empty
    }
    val changeSetSummariesWithSubsetRelatedChangesOnly = changeSetSummaries.map { changeSetSummary =>
      ChangeSetSummarySubsetFilter.filter(changeSetSummary, subset)
    }
    val changeSetSummaryInfos = changeSetSummaryInfosBuilder.toChangeSetSummaryInfos(
      EN,
      NETWORK,
      parameters,
      changeSetSummariesWithSubsetRelatedChangesOnly
    )
    Some(
      SubsetChangesPage(
        subsetInfo,
        filterOptions,
        changeSetSummaryInfos,
        changeCount
      )
    )
  }
}
