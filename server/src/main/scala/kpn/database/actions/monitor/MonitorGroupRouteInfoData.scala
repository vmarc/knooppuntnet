package kpn.database.actions.monitor

import kpn.api.common.Bounds
import kpn.core.doc.Storable

// TODO scala3 move back into using class
case class MonitorGroupRouteInfoData(
  groupId: String,
  monitorRouteId: String,
  bounds: Option[Bounds],
) extends Storable
