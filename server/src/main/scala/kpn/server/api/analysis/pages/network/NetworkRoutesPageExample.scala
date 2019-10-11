package kpn.server.api.analysis.pages.network

import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.shared.NetworkType
import kpn.shared.network.NetworkRouteRow
import kpn.shared.network.NetworkRoutesPage

object NetworkRoutesPageExample {

  val page: NetworkRoutesPage = {

    val routes: Seq[NetworkRouteRow] = Seq()

    NetworkRoutesPage(
      TimeInfoBuilder.timeInfo,
      NetworkType.hiking,
      NetworkDetailsPageExample.networkSummary(),
      routes
    )
  }

}
