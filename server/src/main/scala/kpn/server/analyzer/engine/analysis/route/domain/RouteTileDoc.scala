package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.base.WithStringId
import kpn.api.common.NetworkType
import kpn.api.common.RouteScope

case class RouteTileDoc(
  _id: String,
  routeId: Long,
  routeName: String,
  networkTypes: Seq[NetworkType],
  z: Long,
  x: Long,
  y: Long,
  layer: String,
  scope: Option[RouteScope],
  survey: Option[String],
  error: Option[String],
  segments: Seq[RouteTileSegment]
) extends WithStringId
