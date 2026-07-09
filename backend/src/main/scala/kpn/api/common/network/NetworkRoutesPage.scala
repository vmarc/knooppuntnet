package kpn.api.common.network

import kpn.api.common.RouteType
import kpn.api.common.SurveyDateInfo
import kpn.api.common.TimeInfo

case class NetworkRoutesPage(
  timeInfo: TimeInfo,
  surveyDateInfo: SurveyDateInfo,
  routeType: RouteType,
  summary: NetworkSummary,
  routes: Seq[NetworkRouteRow]
)
