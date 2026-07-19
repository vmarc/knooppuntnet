package kpn.api.common.route;

import kpn.api.common.Bounds;
import kpn.api.common.RouteType;

import java.util.Optional;
import com.google.common.collect.ImmutableList;

public record RouteInfo(
  Long routeId,
  String routeName,
  ImmutableList<RouteType> routeTypes,
  Optional<Bounds> bounds,
  Long memberCount,
  Long pathCount,
  Long segmentCount,
  Long changeCount
) {
}

/*
package kpn.api.common.route

import kpn.api.common.Bounds
import kpn.api.common.RouteType
import kpn.core.doc.Storable

case class RouteInfo(
  routeId: Long,
  routeName: String,
  routeTypes: Seq[RouteType],
  bounds: Option[Bounds],
  memberCount: Long,
  pathCount: Long,
  segmentCount: Long,
  changeCount: Long,
) extends Storable

*/
