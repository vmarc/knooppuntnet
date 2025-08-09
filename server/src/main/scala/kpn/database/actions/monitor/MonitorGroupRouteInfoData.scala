package kpn.database.actions.monitor

import kpn.api.common.Bounds

// TODO scala3 move back into using class
case class MonitorGroupRouteInfoData(
  groupId: String,
  monitorRouteId: String,
  bounds: Option[Bounds],
)
