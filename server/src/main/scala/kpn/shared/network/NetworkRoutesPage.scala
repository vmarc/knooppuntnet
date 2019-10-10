package kpn.shared.network

import kpn.shared.NetworkType
import kpn.shared.TimeInfo

case class NetworkRoutesPage(
  timeInfo: TimeInfo,
  networkType: NetworkType,
  networkSummary: NetworkSummary,
  routes: Seq[NetworkRouteRow]
)
