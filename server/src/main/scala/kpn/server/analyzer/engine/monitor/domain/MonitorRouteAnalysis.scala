package kpn.server.analyzer.engine.monitor.domain

import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteDeviation
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.Relation

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
  matchesGeometry: Option[String],
  deviations: Seq[MonitorRouteDeviation],
  relations: Seq[MonitorRouteAnalysisRelation]
)
