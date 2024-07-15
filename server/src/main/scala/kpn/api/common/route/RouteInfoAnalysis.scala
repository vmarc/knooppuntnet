package kpn.api.common.route

import kpn.api.common.RouteLocationAnalysis

case class RouteInfoAnalysis(
  expectedName: String,
  map: RouteMap,
  structureStrings: Seq[String],
  geometryDigest: String,
  locationAnalysis: RouteLocationAnalysis
)
