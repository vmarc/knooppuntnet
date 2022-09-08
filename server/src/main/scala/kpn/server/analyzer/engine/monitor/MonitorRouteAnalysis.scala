package kpn.server.analyzer.engine.monitor

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.Relation

case class MonitorRouteAnalysis(
  relation: Relation,
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  bounds: Bounds,
  osmSegments: Seq[MonitorRouteSegment],
  gpxGeometry: Option[String],
  matchesGeometry: Option[String],
  deviations: Seq[MonitorRouteDeviation]
)
