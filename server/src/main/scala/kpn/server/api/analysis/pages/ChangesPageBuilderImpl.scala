package kpn.server.api.analysis.pages

import kpn.api.common.AnalysisMode
import kpn.api.common.ChangeSetSummary
import kpn.api.common.ChangesPage
import kpn.api.common.Language
import kpn.api.common.changes.filter.ChangesParameters
import kpn.server.repository.ChangeSetRepository
import org.springframework.stereotype.Component

@Component
class ChangesPageBuilderImpl(
  changeSetRepository: ChangeSetRepository,
  changeSetSummaryInfosBuilder: ChangeSetSummaryInfosBuilder
) extends ChangesPageBuilder {

  override def build(user: Option[String], language: Language, analysisMode: AnalysisMode, parameters: ChangesParameters): ChangesPage = {
    val changesFilter = changeSetRepository.changesFilter(None, parameters.year, parameters.month, parameters.day)
    val changeCount = changesFilter.currentItemCount(parameters.impact)
    val changeSetSummaries: Seq[ChangeSetSummary] = if (user.isDefined) {
      changeSetRepository.changes(parameters)
    }
    else {
      Seq.empty
    }
    val changeSetSummaryInfos = changeSetSummaryInfosBuilder.toChangeSetSummaryInfos(language, analysisMode, changeSetSummaries)
    ChangesPage(changesFilter, changeSetSummaryInfos, changeCount)
  }
}
