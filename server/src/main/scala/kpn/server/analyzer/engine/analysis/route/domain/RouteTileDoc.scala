package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.base.WithStringId
import kpn.api.common.RouteScope
import kpn.api.common.RouteType

case class RouteTileDoc(
  _id: String,
  routeId: Long,
  routeName: String,
  routeTypes: Seq[RouteType],
  z: Long,
  x: Long,
  y: Long,
  layer: String,
  scope: Option[RouteScope],
  survey: Option[String],
  error: Option[String],
  segments: Seq[RouteTileSegment]
) extends WithStringId {
  def tileName: String = {
    s"$z-$x-$y"
  }
}
