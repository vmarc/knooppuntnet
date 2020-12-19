package kpn.core.tools.monitor

import kpn.api.common.monitor.LongdistanceRouteSegment
import org.locationtech.jts.geom.LineString

case class LongdistanceRouteSegmentData(
  segment: LongdistanceRouteSegment,
  lineString: LineString
)