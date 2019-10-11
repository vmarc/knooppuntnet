package kpn.server.api.analysis.pages.network

import kpn.shared.network.NetworkMapPage
import kpn.shared.network.NetworkNodeInfo2

object NetworkMapPageExample {

  val page: NetworkMapPage = {

    val nodes: Seq[NetworkNodeInfo2] = Seq()
    val nodeIds: Seq[Long] = Seq()
    val routeIds: Seq[Long] = Seq()

    NetworkMapPage(
      NetworkDetailsPageExample.networkSummary(),
      nodes,
      nodeIds,
      routeIds
    )
  }
}
