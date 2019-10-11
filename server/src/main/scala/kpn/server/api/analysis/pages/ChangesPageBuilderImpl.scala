package kpn.server.api.analysis.pages

import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.ChangeSetRepository
import kpn.shared.ChangeSetSummary
import kpn.shared.ChangesPage
import kpn.shared.changes.filter.ChangesParameters
import org.springframework.stereotype.Component

@Component
class ChangesPageBuilderImpl(
  changeSetRepository: ChangeSetRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends ChangesPageBuilder {

  override def build(user: Option[String], parameters: ChangesParameters): ChangesPage = {
    val changesFilter = changeSetRepository.changesFilter(None, parameters.year, parameters.month, parameters.day)
    val changeCount = changesFilter.currentItemCount(parameters.impact)
    val changeSetSummaries: Seq[ChangeSetSummary] = if (user.isDefined) {
      changeSetRepository.changes(parameters)
    }
    else {
      Seq()
    }
    val changeSetSummaryInfos = new ChangeSetSummaryInfosBuilder(changeSetInfoRepository).toChangeSetSummaryInfos(changeSetSummaries)
    ChangesPage(changesFilter, changeSetSummaryInfos, changeCount)
  }
}
