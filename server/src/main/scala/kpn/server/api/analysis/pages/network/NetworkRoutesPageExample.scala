package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkRoutesPage
import kpn.api.custom.NetworkType
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder

object NetworkRoutesPageExample {

  val page: NetworkRoutesPage = {

    val routes: Seq[NetworkRouteRow] = Seq()

    NetworkRoutesPage(
      TimeInfoBuilder.timeInfo,
      SurveyDateInfoBuilder.dateInfo,
      NetworkType.hiking,
      NetworkDetailsPageExample.networkSummary(),
      routes
    )
  }

}
