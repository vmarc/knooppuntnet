package kpn.server.api.analysis.pages.network

import kpn.api.common.Bounds
import kpn.api.common.network.NetworkMapNode
import kpn.api.common.network.NetworkMapPage

object NetworkMapPageExample {

  val page: NetworkMapPage = {

    val nodes: Seq[NetworkMapNode] = Seq.empty
    val nodeIds: Seq[Long] = Seq.empty
    val routeIds: Seq[Long] = Seq.empty

    NetworkMapPage(
      NetworkDetailsPageExample.networkSummary(),
      nodes,
      nodeIds,
      routeIds,
      Bounds()
    )
  }
}
