package kpn.server.monitor

import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.common.monitor.MonitorRouteSubRelation
import kpn.server.monitor.domain.MonitorRoute

object MonitorUtil {

  def subRelation(route: MonitorRoute, subRelationId: Long): Option[MonitorRouteRelation] = {
    route.relation.flatMap { relation =>
      findSubRelation(relation, subRelationId)
    }
  }

  private def findSubRelation(relation: MonitorRouteRelation, subRelationId: Long): Option[MonitorRouteRelation] = {
    if (relation.relationId == subRelationId) {
      Some(relation)
    }
    else {
      relation.relations.flatMap { sr =>
        findSubRelation(sr, subRelationId)
      }.headOption
    }
  }

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
    MonitorRouteSubRelation(monitorRouteRelation.relationId, monitorRouteRelation.name)
  }
}
