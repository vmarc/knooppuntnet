package kpn.server.analyzer.engine.monitor.domain

import kpn.api.custom.Relation

case class MonitorSubRelationData(
  relation: Relation,
  segments: Seq[MonitorRouteSegmentData]
)
