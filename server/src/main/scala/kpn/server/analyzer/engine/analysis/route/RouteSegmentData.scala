package kpn.server.analyzer.engine.analysis.route

import org.locationtech.jts.geom.LineString

case class RouteSegmentData(
  id: Int,
  segment: OldRouteSegment,
  lineStrings: Seq[LineString]
)
