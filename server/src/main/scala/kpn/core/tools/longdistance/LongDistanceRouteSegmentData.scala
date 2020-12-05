package kpn.core.tools.longdistance

import kpn.api.common.longdistance.LongDistanceRouteSegment
import org.locationtech.jts.geom.LineString

case class LongDistanceRouteSegmentData(
  segment: LongDistanceRouteSegment,
  lineString: LineString
)