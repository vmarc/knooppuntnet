package kpn.server.api.analysis.pages.subset

import kpn.api.common.AnalysisMode
import kpn.api.common.ChangeSetSummary
import kpn.api.common.Language
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

  def build(user: Option[String], language: Language, analysisMode: AnalysisMode, subset: Subset, parameters: ChangesParameters): Option[SubsetChangesPage] = {
    val subsetInfo = subsetRepository.subsetInfo(subset)
    val filter = changeSetRepository.changesFilter(Some(subset), parameters.year, parameters.month, parameters.day)
    val changeCount = filter.currentItemCount(parameters.impact)
    val changeSetSummaries: Seq[ChangeSetSummary] = if (user.isDefined) {
      changeSetRepository.subsetChanges(subset, parameters)
    }
    else {
      Seq.empty
    }
    val changeSetSummariesWithSubsetRelatedChangesOnly = changeSetSummaries.map { changeSetSummary =>
      ChangeSetSummarySubsetFilter.filter(changeSetSummary, subset)
    }
    val changeSetSummaryInfos = changeSetSummaryInfosBuilder.toChangeSetSummaryInfos(language, analysisMode, changeSetSummariesWithSubsetRelatedChangesOnly)
    Some(SubsetChangesPage(subsetInfo, filter, changeSetSummaryInfos, changeCount))
  }
}
