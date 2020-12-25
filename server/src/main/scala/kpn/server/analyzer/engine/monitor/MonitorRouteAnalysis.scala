package kpn.server.analyzer.engine.monitor

import kpn.api.common.BoundsI
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.Relation

case class MonitorRouteAnalysis(
  relation: Relation,
  wayCount: Long,
  osmDistance: Long,
  gpxDistance: Long,
  bounds: BoundsI,
  osmSegments: Seq[MonitorRouteSegment],
  gpxGeometry: Option[String],
  okGeometry: Option[String],
  nokSegments: Seq[MonitorRouteNokSegment]
)
