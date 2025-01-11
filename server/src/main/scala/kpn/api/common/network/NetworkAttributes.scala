package kpn.api.common.network

import kpn.api.common.Country
import kpn.api.common.LatLonImpl
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Timestamp

case class NetworkAttributes(
  id: Long,
  country: Option[Country],
  routeType: RouteType,
  routeScope: RouteScope,
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

  def scopedRouteType: ScopedRouteType = {
    ScopedRouteType.from(routeType, routeScope)
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
