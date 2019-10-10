package kpn.core.facade.pages

import kpn.core.repository.ChangeSetInfoRepository
import kpn.shared.ChangeSetSummary
import kpn.shared.ChangeSetSummaryInfo

class ChangeSetSummaryInfosBuilder(changeSetInfoRepository: ChangeSetInfoRepository) {

  def toChangeSetSummaryInfos(changeSetSummaries: Seq[ChangeSetSummary]): Seq[ChangeSetSummaryInfo] = {
    val changeSetIds = changeSetSummaries.map(_.key.changeSetId)
    val changeSetInfos = changeSetInfoRepository.all(changeSetIds)
    changeSetSummaries.map { summary =>
      val comment = changeSetInfos.find(s => s.id == summary.key.changeSetId).flatMap { changeSetInfo =>
        changeSetInfo.tags("comment")
      }
      ChangeSetSummaryInfo(summary, comment)
    }
  }
}
