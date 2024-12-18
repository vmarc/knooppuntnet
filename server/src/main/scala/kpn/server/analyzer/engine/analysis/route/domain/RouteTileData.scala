package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.common.RouteScope

case class RouteTileData(
  z: Long,
  x: Long,
  y: Long,
  layer: String,
  scope: Option[RouteScope],
  survey: Option[String],
  error: Option[String],
  segments: Seq[RouteTileSegment]
) {
  def name: String = {
    s"$z-$x-$y"
  }
}
