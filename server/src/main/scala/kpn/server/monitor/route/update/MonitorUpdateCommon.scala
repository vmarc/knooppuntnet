package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.server.monitor.MonitorUtil
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorUpdateCommon(
  routeRepository: RouteRepository,
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository,
) {

  private val log = Log(classOf[MonitorUpdateCommon])

  def findGroup(args: MonitorUpdateArgs): MonitorGroup = {
    val groupName = args.update.groupName
    monitorGroupRepository.groupByName(groupName).getOrElse {
      throw new IllegalArgumentException(
        s"""Could not find group with name "$groupName""""
      )
    }
  }

  def findRoute(group: MonitorGroup, args: MonitorUpdateArgs): MonitorRoute = {
    val routeName = args.update.routeName
    monitorRouteRepository.routeByName(group._id, routeName).getOrElse {
      throw new IllegalArgumentException(
        s"""Could not find route with name "$routeName" in group "${group.name}""""
      )
    }
  }

  def verifyNewRoute(group: MonitorGroup, args: MonitorUpdateArgs): Unit = {
    val routeName = args.update.routeName
    monitorRouteRepository.routeByName(group._id, routeName) match {
      case None => // OK: no route with this name yet
      case Some(route) =>
        throw new IllegalStateException(
          s"""Could not add route with name "$routeName": already exists (_id=${route._id.oid}) in group with name "${group.name}""""
        )
    }
  }

  def updateSuperRoute(route: MonitorRoute): MonitorRoute = {
    val references = monitorRouteRepository.routeReferences(route._id)
    val referenceDistance = references.map(_.referenceDistance).sum
    val states = monitorRouteRepository.routeStates(route._id)
    val deviationCount = states.map(_.deviations.length).sum
    val deviationDistance = states.map(_.deviations.length).sum
    val matchesDistance = states.map(_.matchesDistance).sum

    val (superSegmentCount: Long, osmDistance: Long) = routeRepository.findRouteById(route.relationId.get) match {
      case Some(routeDoc) =>
        val sc = routeDoc.superSegments.length.toLong
        val di = routeDoc.superDistance
        (sc, di)
      case None => (0L, 0L)
    }
    val happy = Util.isWithinTolerance(matchesDistance.toDouble, osmDistance.toDouble) && deviationCount == 0 && superSegmentCount == 1

    route.copy(
      analysisTimestamp = Some(Time.now),
      referenceDistance = referenceDistance,
      deviationCount = deviationCount,
      deviationDistance = deviationDistance,
      osmSegmentCount = superSegmentCount,
      happy = happy
    )
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

  def isRouteChanged(route: MonitorRoute, args: MonitorUpdateArgs): Boolean = {
    val update = args.update
    update.newGroupName.nonEmpty ||
      update.newRouteName.nonEmpty ||
      !update.description.contains(route.description) ||
      route.comment != update.comment ||
      route.relationId != update.relationId ||
      route.referenceType != update.referenceType ||
      route.referenceTimestamp != update.referenceTimestamp ||
      route.referenceFilename != update.referenceFilename
  }

  def isReferenceChanged(route: MonitorRoute, args: MonitorUpdateArgs): Boolean = {
    val update = args.update
    if (route.referenceType != update.referenceType) {
      true
    }
    else {
      if (update.referenceType == MonitorReferenceType.osm) {
        route.relationId != update.relationId ||
          route.referenceTimestamp != update.referenceTimestamp
      }
      else if (update.referenceType == MonitorReferenceType.gpx) {
        update.referenceGpx.nonEmpty
      }
      else {
        false
      }
    }
  }
}
