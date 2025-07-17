package kpn.server.monitor.route.update

import kpn.api.common.Bounds

case class MonitorRouteDeviationAnalysisSummary(
  relationId: Long,
  referenceBounds: Bounds,
  referenceDistance: Long,
  deviationDistance: Long,
  deviationCount: Long,
)
