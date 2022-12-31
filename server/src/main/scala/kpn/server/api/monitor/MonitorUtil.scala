package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSubRelation
import kpn.server.api.monitor.domain.MonitorRoute

object MonitorUtil {

  def subRelationsIn(route: MonitorRoute): Seq[MonitorRouteSubRelation] = {
    route.relation match {
      case Some(monitorRouteRelation) => monitorRouteRelation.relations.flatMap(xx)
      case None => Seq.empty
    }
  }

  private def xx(monitorRouteRelation: MonitorRouteRelation): Seq[MonitorRouteSubRelation] = {
    Seq(toMonitorSubRelation(monitorRouteRelation)) ++
      monitorRouteRelation.relations.flatMap(rel => xx(rel))
  }

  private def toMonitorSubRelation(monitorRouteRelation: MonitorRouteRelation): MonitorRouteSubRelation = {
    val name = monitorRouteRelation.name match {
      case None => "TODO just one name in MonitorRouteRelation"
      case Some(x) => x
    }
    MonitorRouteSubRelation(monitorRouteRelation.relationId, name)
  }
}
