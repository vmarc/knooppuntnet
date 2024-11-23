package kpn.server.analyzer.engine.analysis.route.domain

case class RouteTileData(
  z: Long,
  x: Long,
  y: Long,
  scope: String,
  geometries: Seq[String]
) {
  def name: String = {
    s"$z-$x-$y"
  }
}
