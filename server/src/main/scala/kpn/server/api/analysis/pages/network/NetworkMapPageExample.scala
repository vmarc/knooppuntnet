package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkInfoNode
import kpn.api.common.network.NetworkMapPage

object NetworkMapPageExample {

  val page: NetworkMapPage = {

    val nodes: Seq[NetworkInfoNode] = Seq()
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
