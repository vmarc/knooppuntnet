package kpn.server.monitor.route.update

import kpn.api.common.monitor.MonitorReferenceType
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.core.common.Time
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.repository.RouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
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

  def updateSuperRoute(route: MonitorRoute): MonitorRoute = {
    val references = monitorRouteRepository.references(route._id)
    val referenceDistance = references.map(_.referenceDistance).sum
    val states = monitorRouteRepository.states(route._id)
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
