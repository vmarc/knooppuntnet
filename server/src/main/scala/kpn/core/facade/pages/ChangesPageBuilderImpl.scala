package kpn.core.facade.pages

import kpn.core.repository.ChangeSetInfoRepository
import kpn.core.repository.ChangeSetRepository
import kpn.shared.ChangeSetSummary
import kpn.shared.ChangesPage
import kpn.shared.changes.filter.ChangesParameters

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
