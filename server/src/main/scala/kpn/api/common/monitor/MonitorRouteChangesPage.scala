package kpn.api.common.monitor

import kpn.api.base.MongoId

case class MonitorRouteChangesPage(
  routeId: MongoId,
  routeName: String,
  groupName: String,
  groupDescription: String,
  impact: Boolean,
  pageSize: Long,
  pageIndex: Long,
  totalChangeCount: Long,
  changes: Seq[MonitorRouteChangeSummary]
)
