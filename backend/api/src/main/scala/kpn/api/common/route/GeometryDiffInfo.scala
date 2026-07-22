package kpn.api.common.route

case class GeometryDiffInfo(
  common: GeometryDiffInfoDetail,
  added: GeometryDiffInfoDetail,
  removed: GeometryDiffInfoDetail,
)
