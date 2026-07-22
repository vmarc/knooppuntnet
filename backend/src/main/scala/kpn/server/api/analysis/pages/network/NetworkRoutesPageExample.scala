package kpn.server.api.analysis.pages.network

import kpn.api.common.RouteType
import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkRoutesPage
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder

object NetworkRoutesPageExample {

  val page: NetworkRoutesPage = {

    val routes: Seq[NetworkRouteRow] = Seq.empty

    NetworkRoutesPage(
      TimeInfoBuilder.timeInfo,
      SurveyDateInfoBuilder.dateInfo,
      RouteType.hiking,
      NetworkDetailsPageExample.networkSummary(),
      routes
    )
  }
}
