package kpn.server.analyzer.engine.monitor.domain

import kpn.api.common.monitor.MonitorRouteSegment
import org.locationtech.jts.geom.LineString

case class MonitorRouteSegmentData(
  id: Int,
  segment: MonitorRouteSegment,
  lineStrings: Seq[LineString]
)
