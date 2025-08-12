package kpn.server.analyzer.engine.monitor.domain

import kpn.api.common.Bounds
import kpn.api.common.Relation
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSegment

case class MonitorRouteAnalysis(
  relation: Relation,
  wayCount: Long,
  startNodeId: Option[Long],
  endNodeId: Option[Long],
  osmDistance: Long,
  gpxDistance: Long,
  bounds: Bounds,
  osmSegments: Seq[MonitorRouteSegment],
  gpxGeometry: Option[String],
  matchesDistance: Long,
  matchesLines: Seq[String],
  deviations: Seq[MonitorRouteDeviation],
  relations: Seq[MonitorRouteAnalysisRelation]
)
