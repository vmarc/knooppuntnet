package kpn.api.common.network

import kpn.api.common.LatLonImpl
import kpn.api.custom.Country
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Timestamp

case class NetworkAttributes(
  id: Long,
  country: Option[Country],
  networkType: NetworkType,
  networkScope: NetworkScope,
  name: String,
  km: Long,
  meters: Long,
  nodeCount: Long,
  routeCount: Long,
  brokenRouteCount: Long,
  brokenRoutePercentage: String,
  integrity: Integrity,
  inaccessibleRouteCount: Long,
  connectionCount: Long,
  lastUpdated: Timestamp,
  relationLastUpdated: Timestamp,
  center: Option[LatLonImpl]
) {

  def scopedNetworkType: ScopedNetworkType = {
    ScopedNetworkType.from(networkScope, networkType)
  }

  def percentageOkString: String = {
    s"${percentageOk.toInt}%"
  }

  private def percentageOk: Double = {
    if (routeCount == 0) {
      0
    }
    else {
      100d * (routeCount - brokenRouteCount) / routeCount
    }
  }

  def happy: Boolean = percentageOk > 95

  def veryHappy: Boolean = percentageOk > 99.9

}
