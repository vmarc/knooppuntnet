package kpn.api.common.network

import kpn.api.common.SurveyDateInfo
import kpn.api.common.TimeInfo
import kpn.api.custom.NetworkType

case class NetworkRoutesPage(
  timeInfo: TimeInfo,
  surveyDateInfo: SurveyDateInfo,
  networkType: NetworkType,
  networkSummary: NetworkSummary,
  routes: Seq[NetworkRouteRow]
)
