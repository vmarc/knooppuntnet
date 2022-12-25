package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteSegmentData

case class MonitorRouteRelationData(
  relation: MonitorRouteRelation,
  routeSegments: Seq[MonitorRouteSegmentData]
)
