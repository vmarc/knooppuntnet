package kpn.server.analyzer.engine.analysis.route.domain

case class RouteTileData(
  tile: String,
  scope: String,
  geometries: Seq[String]
)
