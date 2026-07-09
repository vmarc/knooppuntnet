package kpn.server.monitor

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSubRelation

object MonitorUtil {

  def subRelationsInRouteRelation(monitorRouteRelation: MonitorRouteRelation): Seq[MonitorRouteSubRelation] = {
    Seq(toMonitorSubRelation(monitorRouteRelation)) ++
      monitorRouteRelation.relations.flatMap(rel => subRelationsInRouteRelation(rel))
  }

  private def toMonitorSubRelation(monitorRouteRelation: MonitorRouteRelation): MonitorRouteSubRelation = {
    MonitorRouteSubRelation(
      None,
      monitorRouteRelation.relationId,
      monitorRouteRelation.name,
    )
  }
}
