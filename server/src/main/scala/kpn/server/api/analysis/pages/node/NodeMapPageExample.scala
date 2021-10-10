package kpn.server.api.analysis.pages.node

import kpn.api.common.NodeMapInfo
import kpn.api.common.node.NodeMapPage
import kpn.api.custom.NetworkType

object NodeMapPageExample {

  val page: NodeMapPage = {
    NodeMapPage(
      NodeMapInfo(
        id = 1,
        name = "01 / 02",
        networkTypes = Seq(
          NetworkType.cycling,
          NetworkType.hiking
        ),
        latitude = "51.5291600",
        longitude = "4.297800",
      ),
      123
    )
  }
}
