package kpn.server.api.analysis.pages

import kpn.api.common.ChangeSetSummary
import kpn.api.common.ChangeSetSummaryInfo
import kpn.server.repository.ChangeSetInfoRepository

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
