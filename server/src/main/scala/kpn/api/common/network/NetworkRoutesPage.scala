package kpn.api.common.network

import kpn.api.common.NetworkType
import kpn.api.common.SurveyDateInfo
import kpn.api.common.TimeInfo

case class NetworkRoutesPage(
  timeInfo: TimeInfo,
  surveyDateInfo: SurveyDateInfo,
  networkType: NetworkType,
  summary: NetworkSummary,
  routes: Seq[NetworkRouteRow]
)
