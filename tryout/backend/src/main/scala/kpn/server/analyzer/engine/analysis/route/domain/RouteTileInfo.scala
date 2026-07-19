package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.FeatureLayer
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.core.doc.WithStringId

case class RouteTileInfo(
  _id: String,
  routeId: Long,
  routeName: String,
  routeTypes: Seq[RouteType],
  z: Long,
  x: Long,
  y: Long,
  layer: FeatureLayer,
  scope: Option[RouteScope],
  survey: Option[String],
  error: Option[String],
  proposed: Boolean,
  segments: Seq[RouteTileSegment]
) extends WithStringId {
  def tileName: String = {
    s"$z-$x-$y"
  }
}
