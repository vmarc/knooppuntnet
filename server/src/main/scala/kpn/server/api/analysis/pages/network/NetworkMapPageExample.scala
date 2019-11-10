package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkMapPage
import kpn.api.common.network.NetworkNodeInfo2

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
