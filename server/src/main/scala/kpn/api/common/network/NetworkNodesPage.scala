package kpn.api.common.network

import kpn.api.common.SurveyDateInfo
import kpn.api.common.TimeInfo
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType

case class NetworkNodesPage(
  timeInfo: TimeInfo,
  surveyDateInfo: SurveyDateInfo,
  summary: NetworkSummary,
  networkType: NetworkType,
  networkScope: NetworkScope,
  nodes: Seq[NetworkNodeDetail],
  routeIds: Seq[Long]
)
