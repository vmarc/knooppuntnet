package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.util.Log
import kpn.server.monitor.MonitorUtil
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateCommon(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
) {

  private val log = Log(classOf[MonitorUpdateCommon])

  def findGroup(context: MonitorContext): Unit = {
    val groupName = context.value.update.groupName
    val group = monitorGroupRepository.groupByName(groupName).getOrElse {
      throw new IllegalArgumentException(
        s"""Could not find group with name "$groupName""""
      )
    }
    context.set(
      context.value.copy(
        group = Some(group)
      )
    )
  }

  def findRoute(context: MonitorContext): MonitorRoute = {
    val routeName = context.value.update.routeName
    val route = monitorRouteRepository.routeByName(context.value.group.get._id, routeName).getOrElse {
      throw new IllegalArgumentException(
        s"""Could not find route with name "$routeName" in group "${context.value.group.get.name}""""
      )
    }
    context.set(
      context.value.copy(
        oldRoute = Some(route)
      )
    )
    route
  }

  def removeObsoleteReferences(context: MonitorContext): MonitorUpdateContext = {
    context.value.newRoute match {
      case None => context.value
      case Some(newRoute) =>
        val oldReferenceType = context.value.oldRoute.map(_.referenceType)
        if (newRoute.referenceType == MonitorReferenceType.multiGpx && !oldReferenceType.contains(MonitorReferenceType.multiGpx)) {
          context.value.oldReferenceIds.foreach { referenceId =>
            context.deleteRouteReferenceById(referenceId._id)
            monitorRouteRepository.deleteRouteReferenceById(referenceId._id)
          }
          context.value.copy(
            newRoute = Some(newRoute.copy(referenceDistance = 0))
          )
        }
        else {
          if (newRoute.referenceType == MonitorReferenceType.osm) {
            val allRelationIds = newRoute.relationId.toSeq ++ MonitorUtil.subRelationsIn(newRoute).map(_.relationId)
            if (allRelationIds.isEmpty) {
              monitorRouteRepository.deleteRouteReferences(newRoute._id)
            }
            else {
              val obsoleteReferenceIds = context.value.oldReferenceIds.filter { oldReferenceId =>
                oldReferenceId.relationId match {
                  case Some(relationId) => !allRelationIds.contains(relationId)
                  case None => true
                }
              }
              obsoleteReferenceIds.foreach { referenceId =>
                context.deleteRouteReferenceById(referenceId._id)
                monitorRouteRepository.deleteRouteReferenceById(referenceId._id)
              }
            }
          }
          context.value
        }
    }
  }

  def removeObsoleteStates(context: MonitorContext): Unit = {
    context.value.newRoute match {
      case None =>
      case Some(newRoute) =>
        val allRelationIds = newRoute.relationId.toSeq ++ MonitorUtil.subRelationsIn(newRoute).map(_.relationId)
        if (allRelationIds.isEmpty) {
          monitorRouteRepository.deleteRouteStates(newRoute._id)
        }
        else {
          val obsoleteStateIds = context.value.oldStateIds.filterNot(id => allRelationIds.contains(id.relationId))
          obsoleteStateIds.foreach { stateId =>
            monitorRouteRepository.deleteRouteStateById(stateId._id)
          }
        }
    }
  }

  def composeProcessList(monitorRouteRelation: MonitorRouteRelation): Seq[MonitorRouteRelation] = {
    if (monitorRouteRelation.relations.isEmpty) {
      Seq(monitorRouteRelation)
    }
    else {
      val subs = monitorRouteRelation.relations.flatMap { subMonitorRouteRelation =>
        composeProcessList(subMonitorRouteRelation)
      }
      subs :+ monitorRouteRelation
    }
  }
}
