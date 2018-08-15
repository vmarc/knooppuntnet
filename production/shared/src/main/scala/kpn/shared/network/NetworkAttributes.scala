package kpn.shared.network

import kpn.shared.Country
import kpn.shared.LatLonImpl
import kpn.shared.NetworkType
import kpn.shared.Timestamp
import kpn.shared.common.ToStringBuilder

case class NetworkAttributes(
  id: Long,
  country: Option[Country],
  networkType: NetworkType,
  name: String,
  km: Int,
  meters: Int,
  nodeCount: Int,
  routeCount: Int,
  brokenRouteCount: Int,
  brokenRoutePercentage: String,
  integrity: Integrity,
  unaccessibleRouteCount: Int,
  connectionCount: Int,
  lastUpdated: Timestamp,
  relationLastUpdated: Timestamp,
  center: Option[LatLonImpl]
) {

  def completeness: String = {
    NetworkSizeEstimates.size.get(name) match {
      case Some(estimate) =>
        val estimatedRouteCount = (estimate.nodeCount - 2) * 2 + 1
        val completePercentage = 100 * (nodeCount + routeCount) / (estimate.nodeCount + estimatedRouteCount)
        s"$completePercentage%"
      case None => ""
    }
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

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("id", id).
    field("country", country).
    field("networkType", networkType).
    field("name", name).
    field("km", km).
    field("meters", meters).
    field("nodeCount", nodeCount).
    field("routeCount", routeCount).
    field("brokenRouteCount", brokenRouteCount).
    field("brokenRoutePercentage", brokenRoutePercentage).
    field("integrity", integrity).
    field("unaccessibleRouteCount", unaccessibleRouteCount).
    field("connectionCount", connectionCount).
    field("lastUpdated", lastUpdated).
    build
}
