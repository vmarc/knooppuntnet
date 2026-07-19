package kpn.server.analyzer.engine.monitor.domain

import kpn.api.common.Bounds
import kpn.api.common.Relation
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSegment

case class MonitorRouteAnalysisRelation(
  relation: Relation,
  wayCount: Long,
  osmDistance: Long,
  referenceDistance: Long,
  bounds: Bounds,
  osmSegments: Seq[MonitorRouteSegment],
  referenceGeometry: Option[String],
  deviations: Seq[MonitorRouteDeviation]
)
