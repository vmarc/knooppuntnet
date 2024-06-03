package kpn.server.api.analysis.pages.network

import kpn.api.common.Bounds
import kpn.api.common.network.NetworkMapPage

object NetworkMapPageExample {

  val page: NetworkMapPage = {
    NetworkMapPage(
      NetworkDetailsPageExample.networkSummary(),
      Seq.empty,
      Seq.empty,
      Seq.empty,
      Seq.empty,
      Seq.empty,
      Bounds()
    )
  }
}
