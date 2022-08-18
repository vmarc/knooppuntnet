package kpn.api.common.monitor

import kpn.api.base.ObjectId

case class MonitorRouteChangesPage(
  routeId: ObjectId,
  routeName: String,
  groupName: String,
  groupDescription: String,
  impact: Boolean,
  pageSize: Long,
  pageIndex: Long,
  totalChangeCount: Long,
  changes: Seq[MonitorRouteChangeSummary]
)
