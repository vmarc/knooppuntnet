package kpn.core.tools.monitor

import kpn.api.common.monitor.MonitorRouteSegment
import org.locationtech.jts.geom.LineString

case class MonitorRouteSegmentData(
  segment: MonitorRouteSegment,
  lineString: LineString
)