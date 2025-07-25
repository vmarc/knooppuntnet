package kpn.server.analyzer.engine.monitor.analysis

import org.locationtech.jts.geom.LineString

case class DistanceLineString(
  distance: Long,
  line: LineString
)
