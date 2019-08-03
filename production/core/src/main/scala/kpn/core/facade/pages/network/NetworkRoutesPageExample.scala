package kpn.core.facade.pages.network

import kpn.core.facade.pages.TimeInfoBuilder
import kpn.shared.NetworkType
import kpn.shared.network.NetworkRouteInfo
import kpn.shared.network.NetworkRoutesPage

object NetworkRoutesPageExample {

  val page: NetworkRoutesPage = {

    val routes: Seq[NetworkRouteInfo] = Seq()

    NetworkRoutesPage(
      TimeInfoBuilder.timeInfo,
      NetworkType.hiking,
      NetworkDetailsPageExample.networkSummary(),
      routes
    )
  }

}
