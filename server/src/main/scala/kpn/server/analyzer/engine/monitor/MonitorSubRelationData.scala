package kpn.server.analyzer.engine.monitor

import kpn.api.custom.Relation

case class MonitorSubRelationData(
  relation: Relation,
  segments: Seq[MonitorRouteSegmentData]
)
