package kpn.api.common.route

case class GeometryDiff(
  common: Seq[WayGeometry],
  update: Seq[WayGeometryUpdate]
)
