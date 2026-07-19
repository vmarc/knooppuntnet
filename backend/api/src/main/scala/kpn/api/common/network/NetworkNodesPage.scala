package kpn.api.common.network

import kpn.api.common.SurveyDateInfo
import kpn.api.common.TimeInfo

case class NetworkNodesPage(
  timeInfo: TimeInfo,
  surveyDateInfo: SurveyDateInfo,
  summary: NetworkSummary,
  nodes: Seq[NetworkNodeRow]
)
