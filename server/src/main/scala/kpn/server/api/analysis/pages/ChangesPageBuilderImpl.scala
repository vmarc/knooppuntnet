package kpn.server.api.analysis.pages

import kpn.api.common.AnalysisStrategy
import kpn.api.common.ChangesPage
import kpn.api.common.Language
import kpn.api.common.changes.filter.ChangesFilterOption
import kpn.api.common.changes.filter.ChangesParameters
import kpn.server.config.RequestContext
import kpn.server.repository.ChangeSetRepository
import org.springframework.stereotype.Component

@Component
class ChangesPageBuilderImpl(
  changeSetRepository: ChangeSetRepository,
  changeSetSummaryInfosBuilder: ChangeSetSummaryInfosBuilder
) extends ChangesPageBuilder {

  override def build(
    language: Language,
    strategy: AnalysisStrategy,
    parameters: ChangesParameters
  ): ChangesPage = {

    if (RequestContext.isLoggedIn) {
      val filterOptions = changeSetRepository.changesFilter(None, parameters.year, parameters.month, parameters.day)
      val changeCount = ChangesFilterOption.changesCount(filterOptions, parameters)
      val changeSetSummaries = changeSetRepository.changes(parameters)
      val changeSetSummaryInfos = changeSetSummaryInfosBuilder.toChangeSetSummaryInfos(
        language,
        strategy,
        parameters,
        changeSetSummaries
      )
      ChangesPage(filterOptions, changeSetSummaryInfos, changeCount)
    }
    else {
      ChangesPage()
    }
  }
}
