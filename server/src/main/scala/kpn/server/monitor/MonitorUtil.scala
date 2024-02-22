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

  def findSubRelation(relation: MonitorRouteRelation, subRelationId: Long): Option[MonitorRouteRelation] = {
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
      case Some(monitorRouteRelation) => monitorRouteRelation.relations.flatMap(subRelationsInRouteRelation)
      case None => Seq.empty
    }
  }

  def allRelationsIn(route: MonitorRoute): Seq[MonitorRouteSubRelation] = {
    route.relation match {
      case Some(monitorRouteRelation) =>
        toMonitorSubRelation(monitorRouteRelation) +: monitorRouteRelation.relations.flatMap(subRelationsInRouteRelation)
      case None => Seq.empty
    }
  }

  def subRelationsInRouteRelation(monitorRouteRelation: MonitorRouteRelation): Seq[MonitorRouteSubRelation] = {
    Seq(toMonitorSubRelation(monitorRouteRelation)) ++
      monitorRouteRelation.relations.flatMap(rel => subRelationsInRouteRelation(rel))
  }

  private def toMonitorSubRelation(monitorRouteRelation: MonitorRouteRelation): MonitorRouteSubRelation = {
    MonitorRouteSubRelation(
      None,
      monitorRouteRelation.relationId,
      monitorRouteRelation.name,
      monitorRouteRelation.osmWayCount
    )
  }
}
