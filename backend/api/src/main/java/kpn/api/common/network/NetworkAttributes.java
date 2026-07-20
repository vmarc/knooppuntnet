package kpn.api.common.network;

import kpn.api.common.Country;
import kpn.api.common.LatLonImpl;
import kpn.api.common.RouteScope;
import kpn.api.common.RouteType;
import kpn.api.common.network.Integrity;
import kpn.api.custom.Timestamp;

import java.util.Optional;

public record NetworkAttributes(
  Long id,
  Optional<Country> country,
  RouteType routeType,
  RouteScope routeScope,
  Optional<String> name,
  Long km,
  Long meters,
  Long nodeCount,
  Long routeCount,
  Long brokenRouteCount,
  String brokenRoutePercentage,
  Integrity integrity,
  Long inaccessibleRouteCount,
  Long connectionCount,
  Timestamp lastUpdated,
  Timestamp relationLastUpdated,
  Optional<LatLonImpl> center
) {}

/* TODO migrate
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
  name: Option[String],
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

*/
